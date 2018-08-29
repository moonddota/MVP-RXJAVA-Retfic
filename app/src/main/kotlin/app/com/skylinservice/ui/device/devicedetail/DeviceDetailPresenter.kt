package app.com.skylinservice.ui.device.devicedetail

import app.com.skylinservice.data.DeviceDetailDataSource
import app.com.skylinservice.data.DeviceManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.GeneralModel
import app.com.skylinservice.data.remote.requestmodel.GeneralModelData
import app.com.skylinservice.data.remote.requestmodel.GetControlIdModel
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.device.DeviceManagerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DeviceDetailPresenter(private val dataSource: DeviceDetailDataSource,
                            private val scheduler: SchedulerContract) : DeviceDetailContract.Presenter {
    override fun getDeviceDetial(_id: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDeviceDetail(_id)
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
                                showPosts(posts)
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

    var view: DeviceDetailContract.View? = null

    private var posts: GetControlIdModel? = null

    override fun detachView(view: DeviceDetailContract.View) {
        this.view = null
    }

    override fun attachView(view: DeviceDetailContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: GetControlIdModel?) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }
}