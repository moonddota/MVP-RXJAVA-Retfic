package app.com.skylinservice.ui.teammanager.edit

import app.com.skylinservice.data.remote.requestmodel.Imaketeaminfo
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface TeamEditContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)

        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideNumModel) {}
        fun showTeamDeleted(posts: VlideNumModel, uid: Int, tid: Int) {}
        fun showTeamUpdate(posts: VlideNumModel, uid: Int, tid: Int) {}
        fun showAPIExcpetion(exception: ApiException)


    }

    interface Presenter : BasePresenter<View> {
        fun deleteTeam(uid: Int, tid: Long)
        fun updateTeam(name: String, keytime: Int, tid: Long)
    }
}