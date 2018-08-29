package app.com.skylinservice.ui.teammanager.invite

import app.com.skylinservice.data.TeamInviteDataSource
import app.com.skylinservice.data.TeamMangerUpdateDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TeamChangePresenter(private val dataSource: TeamMangerUpdateDataSource,
                          private val scheduler: SchedulerContract) : TeamChangeContract.Presenter {
    override fun change(tid: Long, uid: Int, name: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.updateUser(uid, tid, name)
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
                                showInvite(list)
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

    override fun destroy() {
        compositeDisposable.clear()
    }


    private val compositeDisposable = CompositeDisposable()

    var view: TeamChangeContract.View? = null

    private var deltemodel: VlideNumModel? = null


    override fun detachView(view: TeamChangeContract.View) {
        this.view = null
    }

    override fun attachView(view: TeamChangeContract.View) {
        this.view = view
    }


    private fun showInvite(posts: VlideNumModel) {
        this.deltemodel = posts
        view?.showChange(posts)
    }
}