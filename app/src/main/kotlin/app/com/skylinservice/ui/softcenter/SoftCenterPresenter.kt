package app.com.skylinservice.ui.softcenter

import app.com.skylinservice.data.SofCentertDataSource
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import timber.log.Timber

class SoftCenterPresenter(private val dataSource: SofCentertDataSource,
                          private val scheduler: SchedulerContract) : SoftCenterContract.Presenter {
    override fun getSoftApp() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getNativeApp()
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
                                softapps = list
                                showSoftAPPs(list)
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

    override fun getSoftAppDetail(id: Int) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDetials(id)
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
                                details = list
                                showAPPDetials(list)
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

    override fun etFinalApps() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getNativeApp()
                    .subscribeOn(scheduler.io)
//                    .observeOn(scheduler.ui)
//                    .doOnError { _ ->
//                        it.setLoadingIndicatorVisible(false)
//                    }
//                    .doOnComplete {
//                        it.setLoadingIndicatorVisible(false)
//                    }
                    .subscribe(
                            { list ->
                                softapps = list
                                for (soft in list.data[0]) {
                                    soft.detials = dataSource.getDetials(soft.id.toInt()).blockingFirst().data
                                }

                                showSoftAPPs(list)
                            },
                            { e ->
                                when (e) {
                                    is NoNetworkException -> view?.showNoConnectivityError()
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

    var view: SoftCenterContract.View? = null

    private var softapps: GetAPPsModel? = null
    private var details: GetAPPInfoModel? = null
    private var finalapps: List<NativeAPP>? = null

    override fun detachView(view: SoftCenterContract.View) {
        this.view = null
    }

    override fun attachView(view: SoftCenterContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private var posts: GetAPPInfoModel? = null

    private fun showFinalApps(posts: List<NativeAPP>) {
        this.finalapps = posts
        view?.showgetFinalApps(posts)
    }

    private fun showSoftAPPs(posts: GetAPPsModel) {
        this.softapps = posts
        view?.showgetSoftApp(posts)
    }

    private fun showAPPDetials(posts: GetAPPInfoModel) {
        this.details = posts
        view?.showgetSoftAppDetail(posts)
    }


}