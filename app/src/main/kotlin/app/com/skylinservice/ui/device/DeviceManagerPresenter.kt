package app.com.skylinservice.ui.device

import app.com.skylinservice.data.DeviceManagerDataSource
import app.com.skylinservice.data.LoginDataSource
import app.com.skylinservice.data.remote.requestmodel.RequestBIndDevices
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfo
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.userrelative.login.LoginContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DeviceManagerPresenter(private val dataSource: DeviceManagerDataSource,
                             private val scheduler: SchedulerContract) : DeviceManagerContract.Presenter {
    override fun getDevices(uid: String, page: Int) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDevices(uid.toInt(), page)
                    .subscribeOn(scheduler.io)
//                    .observeOn(scheduler.ui)
                    .doOnError { _ ->
                        it.setLoadingIndicatorVisible(false)
                    }
                    .doOnComplete {
                        it.setLoadingIndicatorVisible(false)
                    }
                    .subscribe(
                            { list ->
                                if (list.data.isNotEmpty() && list.data[0].data.isNotEmpty())
                                    list.data[0].data[0].device_info.relation
                                            .filter { it.type_no.toInt() == 501 }
                                            .forEach {
                                                list.data[0].data[0].controlId = dataSource.getControlId(it._id).blockingFirst().data[0].device_info.contrl_id
                                            }
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

    var view: DeviceManagerContract.View? = null

    private var posts: RequestBIndDevices? = null

    override fun detachView(view: DeviceManagerContract.View) {
        this.view = null
    }

    override fun attachView(view: DeviceManagerContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: RequestBIndDevices?) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }
}