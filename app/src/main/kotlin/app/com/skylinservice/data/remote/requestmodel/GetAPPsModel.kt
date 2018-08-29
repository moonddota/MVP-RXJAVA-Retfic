package app.com.skylinservice.data.remote.requestmodel

data class GetAPPsModel(val ret: String,
                        val data: List<List<SoftApps>>,
                        val msg: String
)