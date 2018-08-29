package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.*
import io.reactivex.Observable

interface SofCentertDataSource {
    fun getNativeApp(): Observable<GetAPPsModel>
    fun getDetials(id: Int): Observable<GetAPPInfoModel>
    fun getFinalApps(): Observable<List<NativeAPP>>?


}

class SoftCenterRepository(private val api: Api) : SofCentertDataSource {
    override fun getFinalApps(): Observable<List<NativeAPP>>? {
        return null
    }

    override fun getDetials(id: Int): Observable<GetAPPInfoModel> {

        return api.newestAppInfo(id)
    }

    override fun getNativeApp(): Observable<GetAPPsModel> {
        return api.getNativeApp()

    }

}