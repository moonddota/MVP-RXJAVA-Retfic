package app.com.skylinservice.ui.userrelative.login.changephone

import app.com.skylinservice.data.ChangePhoneDataSource
import app.com.skylinservice.data.ForgetDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgetContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ChangePresenter(private val dataSource: ChangePhoneDataSource,
                      private val scheduler: SchedulerContract) : ChangePhoneContract.Presenter {
    override fun sendOrigal() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.sendOrigialValidNum()
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
                                sendorigle = list
                                showSendOrigalValid(list)
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


    override fun changePone(orgialChode: String, code: String, newPhone: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.changePhone(orgialChode, code, newPhone)
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
                                change = list
                                showchange(list)
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

    var view: ChangePhoneContract.View? = null

    private var send: VlideNumModel? = null
    private var sendorigle: VlideNumModel? = null

    private var change: VlideNumModel? = null

    override fun detachView(view: ChangePhoneContract.View) {
        this.view = null
    }

    override fun attachView(view: ChangePhoneContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showSendValid(posts: VlideNumModel) {
        this.send = posts
        view?.sendNum(posts)
    }

    private fun showSendOrigalValid(posts: VlideNumModel) {
        this.send = posts
        view?.sendOrigalNum(posts)
    }


    private fun showchange(posts: VlideNumModel) {
        this.change = posts
        view?.changePhone(posts)
    }


}