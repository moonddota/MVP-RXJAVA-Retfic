package app.com.skylinservice.data.remote.requestmodel

data class GetAPPInfoModel(val ret: String,
                           val data: List<APPInfo>,
                           val msg: String
)