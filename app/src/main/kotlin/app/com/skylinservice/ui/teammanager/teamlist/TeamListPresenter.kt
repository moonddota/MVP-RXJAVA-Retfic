package app.com.skylinservice.ui.teammanager.teamlist

import app.com.skylinservice.data.TeamListDataSource
import app.com.skylinservice.data.TeamManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.teammanager.TeamMangerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TeamListPresenter(private val dataSource: TeamListDataSource,
                        private val scheduler: SchedulerContract) : TeamListContract.Presenter {
    override fun destroy() {
        compositeDisposable.clear()
    }


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

    private var imakemembersInfo: TeamMemeberModel? = null


    private val compositeDisposable = CompositeDisposable()

    var view: TeamListContract.View? = null

    private var deltemodel: VlideNumModel? = null


    override fun detachView(view: TeamListContract.View) {
        this.view = null
    }

    override fun attachView(view: TeamListContract.View) {
        this.view = view
    }


    private fun showImakeMember(posts: TeamMemeberModel, tid: Long) {
        this.imakemembersInfo = posts
        view?.showImakeMembers(posts, tid)
    }

    private fun showDelete(posts: VlideNumModel, tid: Long, uid: Int) {
        this.deltemodel = posts
        view?.showUserDeleted(posts, uid, tid)
    }
}