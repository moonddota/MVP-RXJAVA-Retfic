package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.DeviceInfo

data class RequestBIndDevices(val ret: String,
                              var data: List<RequestMiddelkBIndDevices>,
                              val msg: String
)