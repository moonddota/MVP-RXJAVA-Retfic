package app.com.skylinservice.data.remote.requestmodel.basedata

import java.io.Serializable

data class Relation(val _id: String,
                    val type_no: String,
                    val device_type_name: String
) : Serializable