package app.com.skylinservice.ui.set

import android.app.Application
import app.com.skylinservice.AppApplication
import app.com.skylinservice.data.SetDataSource
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.SchedulerContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SetPresenter(private val setDataSource: SetDataSource, private val skyLinDBManager: SkyLinDBManager, private val scheduler: SchedulerContract) : SetContract.Presenter {
    override fun checkUpdate(id: Int) {

        view?.let {
            it.setLoadingIndicatorVisible(true)

            setDataSource.getLatesVersionInfo(id)
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


    override fun logout() {
        skyLinDBManager.deleteUser(UserIdManager().getUserId().toInt())
        showloagout()
    }


    private val compositeDisposable = CompositeDisposable()

    var view: SetContract.View? = null


    private var posts: GetAPPInfoModel? = null

    override fun detachView(view: SetContract.View) {
        this.view = null
    }

    override fun attachView(view: SetContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts() {
        view?.showLatesInfo(posts!!)
    }

    private fun showloagout() {
        view?.showlogout()
    }
}