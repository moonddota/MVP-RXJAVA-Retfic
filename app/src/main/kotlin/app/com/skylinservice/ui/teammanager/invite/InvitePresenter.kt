package app.com.skylinservice.ui.teammanager.invite

import app.com.skylinservice.data.TeamInviteDataSource
import app.com.skylinservice.data.TeamListDataSource
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.teammanager.teamlist.TeamListContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class InvitePresenter(private val dataSource: TeamInviteDataSource,
                      private val scheduler: SchedulerContract) : InviteContract.Presenter {
    override fun destroy() {
        compositeDisposable.clear()
    }


    override fun invite(tid: Long, name: String, telphone: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.inviteteam(tid, name, telphone)
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


    private val compositeDisposable = CompositeDisposable()

    var view: InviteContract.View? = null

    private var deltemodel: VlideNumModel? = null


    override fun detachView(view: InviteContract.View) {
        this.view = null
    }

    override fun attachView(view: InviteContract.View) {
        this.view = view
    }


    private fun showInvite(posts: VlideNumModel) {
        this.deltemodel = posts
        view?.invite(posts)
    }
}