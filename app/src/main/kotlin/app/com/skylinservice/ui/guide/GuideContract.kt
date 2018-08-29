package app.com.skylinservice.ui.guide

import android.app.Fragment
import app.com.skylinservice.data.remote.model.Post
import app.com.skylinservice.ui.BasePresenter
import app.com.skylinservice.ui.BaseView

interface GuideContract {
    interface View : BaseView {
        fun showMain()
        fun showGuide()
    }

    interface Presenter : BasePresenter<View> {
        fun touchEndPage(
                position: Int,
                isdrag: Boolean,
                list: List<out Any>
        )

        fun chargeGuideView()


    }
}