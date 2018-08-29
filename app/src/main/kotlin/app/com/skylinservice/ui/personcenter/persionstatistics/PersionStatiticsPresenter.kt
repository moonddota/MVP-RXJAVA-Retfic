package app.com.skylinservice.ui.personcenter

import app.com.skylinservice.data.TeamManagerDataSource
import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.TeamMemeberModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class PersionStatiticsPresenter(private val dataSource: TeamManagerDataSource,
                                private val scheduler: SchedulerContract) : PersionStatisticsContract.Presenter {
    override fun getBaseDataStatistics(uid: Int, tid: Long) {
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


    private val compositeDisposable = CompositeDisposable()

    var view: PersionStatisticsContract.View? = null

    private var deltemodel: VlideNumModel? = null

    private var posts: Imaketeaminfo? = null
    private var ijoin: Imaketeaminfo? = null

    private var imakemembersInfo: TeamMemeberModel? = null
    private var ijoinmembersInfo: TeamMemeberModel? = null

    override fun detachView(view: PersionStatisticsContract.View) {
        this.view = null
    }

    override fun attachView(view: PersionStatisticsContract.View) {
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

    private fun showBaseStatitices(posts: Imaketeaminfo) {
        this.posts = posts
        view?.showRequestAnswer(posts)
    }


}