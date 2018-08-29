package app.com.skylinservice.data.remote.requestmodel.basedata

import java.io.Serializable

data class DeviceInfo(val is_bind: Int,
                      val bind_user: String,
                      val device_status: String,
                      val create_time: String,
                      val device_name: String,
                      val bind_time: String,
                      val update_time: String,
                      var contrl_id: String,
                      val relation: List<Relation>
) : Serializable