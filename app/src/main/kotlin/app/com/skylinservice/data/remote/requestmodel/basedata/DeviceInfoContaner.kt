package app.com.skylinservice.data.remote.requestmodel.basedata

import java.io.Serializable

data class DeviceInfoContaner(val _id: String,
                              val device_info: DeviceInfo,
                              val type_no: String,
                              val one_level: String,
                              val two_level: String,
                              val device_type_name: String,
                              var controlId: String

) : Serializable