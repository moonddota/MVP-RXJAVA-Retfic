package app.com.skylinservice.ui.main

import app.com.skylinservice.data.MainDataSource
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainPresenter(private val dataSource: MainDataSource,
                    private val scheduler: SchedulerContract) : MainContract.Presenter {
    override fun takeDufatltTeam(tid: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getSetDefualtTeam(tid)
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
                                setteamid = list
                                showSetTeamId(list)
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
                                posts = list
                                showPosts()


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

    override fun getBindDevices(userid: Int, page: Int) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getDevices(UserIdManager().getUserId().toInt(), page)
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
                                bindBackData = list
                                showBindDevice(list)
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

    override fun getUnUpdateAPPs() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getUnUpdateApps()
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

    override fun getAuhtes(userid: Int, page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBanners(bannername: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getBanner(bannername)
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
                                banner = list
                                showBanners(list)
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

    private var softapps: GetAPPsModel? = null


    private var posts: GetAPPInfoModel? = null
    private var ijoin: Imaketeaminfo? = null


    private val compositeDisposable = CompositeDisposable()

    var view: MainContract.View? = null

    private var bindBackData: RequestBIndDevices? = null
    private var unUpdateApps: RequestBIndDevices? = null
    private var authes: RequestBIndDevices? = null
    private var setteamid: VlideNumModel? = null

    private var banner: GetBannerModel? = null

    override fun detachView(view: MainContract.View) {
        this.view = null
    }

    override fun attachView(view: MainContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showBindDevice(posts: RequestBIndDevices) {
        this.bindBackData = posts
        view?.showDevicesNum(posts)
    }


    private fun showBanners(posts: GetBannerModel) {
        this.banner = posts
        view?.showBanner(posts)
    }

    private fun showPosts() {
        view?.showLatesInfo(posts!!)
    }

    private fun showSetTeamId(setteamid: VlideNumModel) {
        this.setteamid = setteamid
        view?.showSetDefaultTeam(setteamid)
    }

    private fun showAuthes(posts: RequestBIndDevices) {
        this.authes = posts
        view?.showAuth(posts)
    }

    private fun showSoftAPPs(posts: GetAPPsModel) {
        this.softapps = posts
        view?.showApps(posts)
    }

    private fun showIJoin(posts: Imaketeaminfo) {
        this.ijoin = posts
        view?.showIJoinBack(posts)
    }

}