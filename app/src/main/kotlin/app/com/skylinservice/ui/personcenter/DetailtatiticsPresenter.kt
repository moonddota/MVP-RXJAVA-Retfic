package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.DetailStatitcisDatasource
import app.com.skylinservice.data.DetailStatitcisRepository
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DetailtatiticsPresenter(private val dataSource: DetailStatitcisDatasource,
                              private val scheduler: SchedulerContract) : DetailStatisticsContract.Presenter {
    override fun getAllOperater(device_sn: String) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getAllOperaqter(device_sn)
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
                                allOperater = list
                                showAllOperater(list)


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

    override fun getmaclist() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getMaclist()
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
                                allv02 = list
                                showAllv02(list)


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

    override fun getIJoin() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getIJoin()
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
                                ijoin = list
                                showIJoin(list)
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

    var view: DetailStatisticsContract.View? = null


    private var ijoin: Imaketeaminfo? = null


    private var allv02: GetMacListModel? = null


    private var allOperater: GetALlOperaterModel? = null


    private var posts: GetDeviceDetailsssModel? = null


    private var statistic: GetDeviceStatisticsModel? = null


    override fun detachView(view: DetailStatisticsContract.View) {
        this.view = null
    }

    override fun attachView(view: DetailStatisticsContract.View) {
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

    private fun showIJoin(posts: Imaketeaminfo) {
        this.ijoin = posts
        view?.showIJoinBack(posts)
    }


    private fun showAllv02(posts: GetMacListModel) {
        this.allv02 = posts
        view?.showMaclist(posts)
    }


    private fun showAllOperater(posts: GetALlOperaterModel) {
        this.allOperater = posts
        view?.showAllOperatier(posts)
    }


}