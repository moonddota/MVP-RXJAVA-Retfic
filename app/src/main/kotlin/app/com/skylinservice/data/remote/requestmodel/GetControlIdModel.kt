package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfo

data class GetControlIdModel(val ret: String,
                             val data: List<GetControlIdMiddelModel>,
                             val msg: String
)

data class GetControlIdMiddelModel(val _id: String,
                                   var device_info: DeviceInfo
)


//data class IdModel(val contrl_id: String
//)