package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfo
import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfoContaner

data class RequestMiddelkBIndDevices(val count: String,
                                     var data: List<DeviceInfoContaner>

)