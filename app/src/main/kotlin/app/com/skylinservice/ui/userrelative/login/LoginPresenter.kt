package app.com.skylinservice.ui.userrelative.login

import app.com.skylinservice.data.LoginDataSource
import app.com.skylinservice.data.RegistDataSource
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.userrelative.login.regist.RigistContract
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import sjj.alog.Log
import timber.log.Timber

class LoginPresenter(private val dataSource: LoginDataSource,
                     private val scheduler: SchedulerContract) : LoginContract.Presenter {
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

                                        Log.e("nnnnnn", e)
                                        Timber.e(e)
                                        ToastUtils.showShort(e.message)
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
                                Log.e(list)

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

    override fun login(telPhone: String, pwd: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.logining(telPhone, pwd)
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
                                posts = list
                                (posts as VlideLogin).data.password = pwd
                                showPosts(list)
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

    override fun checkUpdate(id: Int) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getLatesVersionInfo(id)
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
                                latestapp = list
                                 showlatest(list)


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

    private val compositeDisposable = CompositeDisposable()

    var view: LoginContract.View? = null

    private var posts: VlideLogin? = null

    private var latestapp: GetAPPInfoModel? = null


    private var send: VlideNumModel? = null


    private var weixinconnect: WeixinConnectModel? = null
    private var weinBind: WeixinBindModel? = null


    override fun detachView(view: LoginContract.View) {
        this.view = null
    }

    override fun attachView(view: LoginContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }

    private fun showSend(posts: VlideNumModel) {
        this.send = posts
        view?.showSend(posts)
    }

    private fun showPosts(posts: VlideLogin) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }

    private fun showlatest(posts: GetAPPInfoModel) {
        this.latestapp = posts
        view?.showLatesInfo(posts)
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