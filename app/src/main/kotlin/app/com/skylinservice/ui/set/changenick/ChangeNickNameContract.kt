package app.com.skylinservice.ui.set.changenick

import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.VlideLogin
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface ChangeNickNameContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun showRequestAnswer(posts: VlideLogin?) {}
        fun showChangeUser(posts: VlideNumModel, name: String) {}
        fun showChangeFly(posts: VlideNumModel, name: String) {}

        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun changgeUser(name: String)
        fun changeFly(_id: String,name: String)

    }
}