package app.com.skylinservice.ui.guide

import android.os.Handler
import android.support.annotation.UiThread
import app.com.skylinservice.data.PostDataSource
import app.com.skylinservice.data.remote.model.Post
import app.com.skylinservice.manager.FirstLachManager
import app.com.skylinservice.ui.SchedulerContract
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.*

class GuidePresenter(private val dataSource: PostDataSource,
                     private val scheduler: SchedulerContract) : GuideContract.Presenter {
    override fun chargeGuideView() {


        launch(CommonPool) {
            //            Thread.sleep(1000L)
            if (FirstLachManager().getFirstLanchState() || SPUtils.getInstance().getInt("version", 0) < AppUtils.getAppVersionCode())
                view?.showGuide()
            else
                view?.showMain()
        }
    }

    override fun touchEndPage(position: Int, isdrag: Boolean, list: List<out Any>) {
        if (position == list.size - 1 && isdrag) {
            FirstLachManager().setFirstLanchState(true)
            SPUtils.getInstance().put("version", AppUtils.getAppVersionCode())
            view?.showMain()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    var view: GuideContract.View? = null


    override fun detachView(view: GuideContract.View) {
        this.view = null
    }

    override fun attachView(view: GuideContract.View) {
        this.view = view
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


}