package app.com.skylinservice.ui.teammanager

import app.com.skylinservice.data.TeamManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TeamManagerPresenter(private val dataSource: TeamManagerDataSource,
                           private val scheduler: SchedulerContract) : TeamMangerContract.Presenter {
    override fun deleteUser(uid: Int, tid: Long) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.deleteUser(uid, tid)
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
                                deltemodel = list
                                showDelete(list, tid, uid)
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

    override fun getIMakeMemers(tid: Long) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getImakeMemebers(tid)
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
                                imakemembersInfo = list
                                showImakeMember(list, tid)
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

    override fun getIJoinMemers(tid: Long) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getImakeMemebers(tid)
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
                                ijoinmembersInfo = list
                                showImakeMember(list, tid)
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

    override fun getIMake() {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.getIMake()
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
                                posts = list
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


    private val compositeDisposable = CompositeDisposable()

    var view: TeamMangerContract.View? = null

    private var deltemodel: VlideNumModel? = null

    private var posts: Imaketeaminfo? = null
    private var ijoin: Imaketeaminfo? = null

    private var imakemembersInfo: TeamMemeberModel? = null
    private var ijoinmembersInfo: TeamMemeberModel? = null

    override fun detachView(view: TeamMangerContract.View) {
        this.view = null
    }

    override fun attachView(view: TeamMangerContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: Imaketeaminfo) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }

    private fun showIJoin(posts: Imaketeaminfo) {
        this.ijoin = posts
        view?.showIJoinBack(posts)
    }

    private fun showImakeMember(posts: TeamMemeberModel, tid: Long) {
        this.imakemembersInfo = posts
        view?.showImakeMembers(posts, tid)
    }

    private fun showIJoinMember(posts: TeamMemeberModel, tid: Long) {
        this.ijoinmembersInfo = posts
        view?.showIJoinMembers(posts, tid)
    }

    private fun showDelete(posts: VlideNumModel, tid: Long, uid: Int) {
        this.deltemodel = posts
        view?.showUserDeleted(uid, tid)
    }
}