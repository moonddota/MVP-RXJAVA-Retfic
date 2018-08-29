package app.com.skylinservice.ui.teammanager.create

import app.com.skylinservice.data.CreateTeamDataSource
import app.com.skylinservice.data.TeamManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.teammanager.TeamMangerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CreateTeamPresenter(private val dataSource: CreateTeamDataSource,
                          private val scheduler: SchedulerContract) : CreateTeamContract.Presenter {
    override fun createTeam(name: String, keytime: Int) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            dataSource.createTeam(name, keytime)
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


    private val compositeDisposable = CompositeDisposable()

    var view: CreateTeamContract.View? = null

    private var posts: VlideLogin? = null

    override fun detachView(view: CreateTeamContract.View) {
        this.view = null
    }

    override fun attachView(view: CreateTeamContract.View) {
        this.view = view

        posts?.let(this::showPosts)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(posts: VlideLogin) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }


}