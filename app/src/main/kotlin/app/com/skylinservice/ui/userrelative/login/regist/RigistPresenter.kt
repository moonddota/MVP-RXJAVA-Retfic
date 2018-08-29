package app.com.skylinservice.ui.userrelative.login.regist

import app.com.skylinservice.data.RegistDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.VlideRegist
import app.com.skylinservice.data.remote.requestmodel.WeixinBindModel
import app.com.skylinservice.data.remote.requestmodel.WeixinConnectModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class RigistPresenter(private val dataSource: RegistDataSource,
                      private val scheduler: SchedulerContract) : RigistContract.Presenter {


    override fun weixin_connect(code: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.weixin_connect(code)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.ui)
                    .doOnError { _ ->
                        it.setLoadingIndicatorVisible(false)
                    }
                    .doOnComplete {
                        it.setLoadingIndicatorVisible(false)
                    }
                    .subscribe(
                            { list ->
                                weixinconnect = list
                                showweixinConnect(list)
                            },
                            { e ->
                                when (e) {
                                    is NoNetworkException -> view?.showNoConnectivityError()
                                    is ApiException -> view?.showAPIExcpetion(e)

                                    else -> {
                                        Timber.e(e)
                                        view?.showUnknownError()
                                    }
                                }
                            }
                    )
                    .addToCompositeDisposable(compositeDisposable)
        }
    }

    override fun register(pwd: String, telphone: String, code: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.regist(pwd, telphone, code)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.ui)
                    .doOnError { _ ->
                        it.setLoadingIndicatorVisible(false)
                    }
                    .doOnComplete {
                        it.setLoadingIndicatorVisible(false)
                    }
                    .subscribe(
                            { list ->
                                rigst = list
                                showRigst(list)
                            },
                            { e ->
                                when (e) {
                                    is NoNetworkException -> view?.showNoConnectivityError()
                                    is ApiException -> view?.showAPIExcpetion(e)

                                    else -> {
                                        Timber.e(e)
                                        view?.showUnknownError()
                                    }
                                }
                            }
                    )
                    .addToCompositeDisposable(compositeDisposable)
        }
    }

    override fun sendValidNum(telPhone: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.sendValidNum(telPhone)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.ui)
                    .doOnError { _ ->
                        it.setLoadingIndicatorVisible(false)
                    }
                    .doOnComplete {
                        it.setLoadingIndicatorVisible(false)
                    }
                    .subscribe(
                            { list ->
                                send = list
                                showSend(list)
                            },
                            { e ->
                                when (e) {
                                    is NoNetworkException -> view?.showNoConnectivityError()
                                    is ApiException -> view?.showAPIExcpetion(e)

                                    else -> {
                                        Timber.e(e)
                                        view?.showUnknownError()
                                    }
                                }
                            }
                    )
                    .addToCompositeDisposable(compositeDisposable)
        }
    }

    override fun weixin_bind(wechatId: String, wechatCode: String, telphone: String, telphoneCode: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.weixin_bind(wechatId, wechatCode, telphone, telphoneCode)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.ui)
                    .doOnError { _ ->
                        it.setLoadingIndicatorVisible(false)
                    }
                    .doOnComplete {
                        it.setLoadingIndicatorVisible(false)
                    }
                    .subscribe(
                            { list ->
                                weinBind = list
                                showweixinBind(list)
                            },
                            { e ->
                                when (e) {
                                    is NoNetworkException -> view?.showNoConnectivityError()
                                    is ApiException -> view?.showAPIExcpetion(e)

                                    else -> {
                                        ToastUtils.showLong(e.message)
                                        Timber.e(e)
                                        view?.showUnknownError()
                                    }
                                }
                            }
                    )
                    .addToCompositeDisposable(compositeDisposable)
        }
    }

    private val compositeDisposable = CompositeDisposable()

    var view: RigistContract.View? = null

    private var send: VlideNumModel? = null

    private var weixinconnect: WeixinConnectModel? = null
    private var weinBind: WeixinBindModel? = null


    private var rigst: VlideRegist? = null


    override fun detachView(view: RigistContract.View) {
        this.view = null
    }

    override fun attachView(view: RigistContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showSend(posts: VlideNumModel) {
        this.send = posts
        view?.showSend(posts)
    }

    private fun showRigst(posts: VlideRegist) {
        this.rigst = posts
        view?.showRigst(posts)
    }

    private fun showweixinConnect(posts: WeixinConnectModel) {
        this.weixinconnect = posts
        view?.showweixinConnect(posts)
    }

    private fun showweixinBind(posts: WeixinBindModel) {
        this.weinBind = posts
        view?.showweixinBind(posts)
    }
}