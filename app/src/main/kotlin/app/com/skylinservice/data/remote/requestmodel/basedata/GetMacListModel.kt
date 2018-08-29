package app.com.skylinservice.data.remote.requestmodel.basedata

import app.com.skylinservice.ui.maclist.ReqeustAuth

data class GetMacListModel(val ret: String,
                           val data: GetMacListMiddleModel,
                           val msg: String
)


data class GetMacListMiddleModel(val server_time: String,
                                 val aircraft_list: List<ReqeustAuth>
)