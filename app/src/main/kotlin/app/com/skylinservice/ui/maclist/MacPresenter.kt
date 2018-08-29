package app.com.skylinservice.ui.maclist

import app.com.skylinservice.data.MacDataSource
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MacPresenter(private val macDataSource: MacDataSource, private val scheduler: SchedulerContract) : MacContract.Presenter {
    override fun getmaclist(tid: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            macDataSource.getMaclist(tid)
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


    private val compositeDisposable = CompositeDisposable()

    var view: MacContract.View? = null


    private var posts: GetMacListModel? = null

    override fun detachView(view: MacContract.View) {
        this.view = null
    }

    override fun attachView(view: MacContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts() {
        view?.showMaclist(posts)
    }


}