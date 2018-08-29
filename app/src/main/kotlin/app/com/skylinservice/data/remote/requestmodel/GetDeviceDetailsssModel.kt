package app.com.skylinservice.data.remote.requestmodel

data class GetDeviceDetailsssModel(val ret: String,
                                   val data: GetDeviceDetailsssMiddelModel,
                                   val msg: String
)

class GetDeviceDetailsssMiddelModel(val device_name: String,
                                    val device_type_name: String,
                                    val device_sn: String,
                                    val device_control_id: String,
                                    val device_created_at: String,
                                    val device_register_at: String,
                                    val device_fly_time: String,
                                    val device_up_down_count: String,
                                    val device_team: String
)

