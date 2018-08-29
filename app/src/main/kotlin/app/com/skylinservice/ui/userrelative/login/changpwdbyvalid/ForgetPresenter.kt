package app.com.skylinservice.ui.userrelative.login.changpwdbyvalid

import app.com.skylinservice.data.ForgetDataSource
import app.com.skylinservice.data.RegistDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideForget
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.userrelative.login.regist.RigistContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ForgetPresenter(private val dataSource: ForgetDataSource,
                      private val scheduler: SchedulerContract) : ForgetContract.Presenter {
    override fun forget(telPhone: String, code: String, newpwd: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.fprget(telPhone, code, newpwd)
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
                                forget = list
                                showForget(list)
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
                                showSendValid(list)
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

    var view: ForgetContract.View? = null

    private var send: VlideNumModel? = null
    private var forget: VlideForget? = null

    override fun detachView(view: ForgetContract.View) {
        this.view = null
    }

    override fun attachView(view: ForgetContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showSendValid(posts: VlideNumModel) {
        this.send = posts
        view?.showSendValid(posts)
    }


    private fun showForget(posts: VlideForget) {
        this.forget = posts
        view?.showForget(posts)
    }


}