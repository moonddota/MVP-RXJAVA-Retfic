package app.com.skylinservice.ui.teammanager.create

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface CreateTeamContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin) {}
        fun showAPIExcpetion(exception: ApiException)


    }

    interface Presenter : BasePresenter<View> {
        fun createTeam(name:String,keytime:Int)
    }
}