package app.com.skylinservice.data.remote.requestmodel

data class GetALlOperaterModel(val ret: String,
                               val data: MutableList<GetALlOperaterMiddelModel>?,
                               val msg: String
)

class GetALlOperaterMiddelModel(val user_id: String,
                                val user_phone: String,
                                val user_name: String
)

