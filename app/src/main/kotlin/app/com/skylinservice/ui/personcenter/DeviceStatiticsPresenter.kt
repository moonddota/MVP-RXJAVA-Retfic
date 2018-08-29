package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.DeviceStatitcisDatasource
import app.com.skylinservice.data.TeamManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DeviceStatiticsPresenter(private val dataSource: DeviceStatitcisDatasource,
                               private val scheduler: SchedulerContract) : DeviceStatisticsContract.Presenter {
    override fun getBaseDataStatistics(page: Int, pageSize: Int, statisticTime: String, userid: String, teamnid: String, devicesn: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDeviceStatistics(page, pageSize, statisticTime, userid, teamnid, devicesn)
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
                                statistic = list
                                showBaseStatitices(list)
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

    override fun getDeviceDetail(sn: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDeviceDetail(sn)
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
                                showDeviceDetail(list)
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

    var view: DeviceStatisticsContract.View? = null


    private var posts: GetDeviceDetailsssModel? = null


    private var statistic: GetDeviceStatisticsModel? = null


    override fun detachView(view: DeviceStatisticsContract.View) {
        this.view = null
    }

    override fun attachView(view: DeviceStatisticsContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: GetDeviceDetailsssModel) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }

    private fun showBaseStatitices(posts: GetDeviceStatisticsModel) {
        this.statistic = posts
        view?.showStatistics(posts)
    }


    private fun showDeviceDetail(posts: GetDeviceDetailsssModel) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }


}