package app.com.skylinservice.ui.device.unbind

import app.com.skylinservice.data.BindDataSource
import app.com.skylinservice.data.UnBindDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.device.manual.BindContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class UnBindPresenter(private val dataSource: UnBindDataSource,
                      private val scheduler: SchedulerContract) : UnBindContract.Presenter {
    override fun sendValidNum(deviceId: String, telPhone: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.sendValidNum(deviceId, telPhone)
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
                                sendback = list
                                showSendValidNum(list)
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


    override fun unbind(deviceId: String, telPhone: String, sms_code: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.unbind(deviceId, telPhone, sms_code)
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
                                unbindback = list
                                showUnbind(list)
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

    var view: UnBindContract.View? = null

    private var unbindback: VlideNumModel? = null
    private var sendback: VlideNumModel? = null

    override fun detachView(view: UnBindContract.View) {
        this.view = null
    }

    override fun attachView(view: UnBindContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showUnbind(posts: VlideNumModel) {
        this.unbindback = posts
        view?.showunbind(posts)
    }

    private fun showSendValidNum(posts: VlideNumModel) {
        this.sendback = posts
        view?.showsendValidNum(posts)
    }
}