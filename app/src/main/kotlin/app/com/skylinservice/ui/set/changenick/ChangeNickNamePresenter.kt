package app.com.skylinservice.ui.set.changenick

import app.com.skylinservice.data.ChangNickNameDataSource
import app.com.skylinservice.data.ChangePhoneDataSource
import app.com.skylinservice.data.SetDataSource
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.exception.NoNetworkException
import app.com.skylinservice.extensions.addToCompositeDisposable
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.SchedulerContract
import app.com.skylinservice.ui.set.SetContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ChangeNickNamePresenter(private val setDataSource: ChangNickNameDataSource, private val scheduler: SchedulerContract) : ChangeNickNameContract.Presenter {
    override fun changeFly(_id: String, name: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            setDataSource.upFly(_id, name)
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
                                postsfly = list
                                showChangeFly(name)


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

    override fun changgeUser(name: String) {
        view?.let {
            it.setLoadingIndicatorVisible(true)

            setDataSource.upuser(name)
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
                                showPosts(name)


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

    var view: ChangeNickNameContract.View? = null


    private var posts: VlideNumModel? = null

    private var postsfly: VlideNumModel? = null


    override fun detachView(view: ChangeNickNameContract.View) {
        this.view = null
    }

    override fun attachView(view: ChangeNickNameContract.View) {
        this.view = view

    }

    override fun destroy() {
        compositeDisposable.clear()
    }


    private fun showPosts(name: String) {
        view?.showChangeUser(posts!!, name)
    }

    private fun showChangeFly(name: String) {
        view?.showChangeFly(postsfly!!, name)
    }

}