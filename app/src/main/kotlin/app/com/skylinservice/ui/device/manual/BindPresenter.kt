package app.com.skylinservice.ui.device.manual

import app.com.skylinservice.data.BindDataSource
import app.com.skylinservice.data.DeviceManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.device.DeviceManagerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class BindPresenter(private val dataSource: BindDataSource,
                    private val scheduler: SchedulerContract) : BindContract.Presenter {
    override fun bind(activenum: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.bind(activenum)
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


    private val compositeDisposable = CompositeDisposable()

    var view: BindContract.View? = null

    private var posts: VlideNumModel? = null

    override fun detachView(view: BindContract.View) {
        this.view = null
    }

    override fun attachView(view: BindContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: VlideNumModel) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }
}