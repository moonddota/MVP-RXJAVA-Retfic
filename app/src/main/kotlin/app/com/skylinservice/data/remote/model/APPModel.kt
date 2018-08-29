package app.com.skylinservice.data.remote.model


//state :0 显示下载, 1 显示打开 : 2:显示更新 3:显示暂停
data class APPModel(val id: Long,
                    val url: String, val title: String, val des: String, var state: Int, val packageName: String)