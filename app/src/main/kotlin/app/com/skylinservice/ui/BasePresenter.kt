package app.com.skylinservice.ui
interface BasePresenter<in V : BaseView> {
    fun detachView(view: V)
    fun attachView(view: V)

    fun destroy()
}