package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.requestmodel.GetAPPInfoModel
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import com.blankj.utilcode.util.SPUtils
import io.reactivex.Observable

interface MacDataSource {
    fun getMaclist(id: String): Observable<GetMacListModel>

}

class MacRepository(private val api: Api) : MacDataSource {
    override fun getMaclist(id: String): Observable<GetMacListModel> {

        return api.getMaclist(SPUtils.getInstance().getString("token"),id)
    }

}