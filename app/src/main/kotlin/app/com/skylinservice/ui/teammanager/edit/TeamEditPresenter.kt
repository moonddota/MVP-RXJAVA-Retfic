package app.com.skylinservice.ui.teammanager.edit

import app.com.skylinservice.data.CreateTeamDataSource
import app.com.skylinservice.data.TeamEditDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.teammanager.create.CreateTeamContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TeamEditPresenter(private val dataSource: TeamEditDataSource,
                        private val scheduler: SchedulerContract) : TeamEditContract.Presenter {
    override fun updateTeam(name: String, keytime: Int, tid: Long) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.updateTeam(tid, name, keytime)
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
                                delte = list
                                showUpdate(list)
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

    override fun deleteTeam(uid: Int, tid: Long) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.deleteTeam(tid)
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
                                delte = list
                                showdelete(list)
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

    var view: TeamEditContract.View? = null

    private var delte: VlideNumModel? = null
    private var update: VlideNumModel? = null

    override fun detachView(view: TeamEditContract.View) {
        this.view = null
    }

    override fun attachView(view: TeamEditContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }

    fun showdelete(posts: VlideNumModel) {
        this.delte = posts
        view?.showTeamDeleted(posts, 0, 0)
    }

    fun showUpdate(posts: VlideNumModel) {
        this.update = posts
        view?.showTeamUpdate(posts, 0, 0)
    }


}