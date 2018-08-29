package app.com.skylinservice.ui.userrelative.login.changephone

import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface ChangePhoneContract {
    interface View : BaseView {
        fun setLoadingIndicatorVisible(loadingVisible: Boolean)


        fun showNoConnectivityError()
        fun showUnknownError()
        fun sendOrigalNum(posts: VlideNumModel) {}
        fun sendNum(posts: VlideNumModel) {}
        fun changePhone(posts: VlideNumModel) {}
        fun showAPIExcpetion(exception: ApiException)

    }

    interface Presenter : BasePresenter<View> {
        fun sendValidNum(telPhone: String)
        fun sendOrigal()
        fun changePone(orgialChode: String, code: String, newPhone: String)
    }
}