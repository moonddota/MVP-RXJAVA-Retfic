package app.com.skylinservice.data.remote.requestmodel

import com.skylin.uav.zbzy.model.LoginTable

data class VlideLogin(val ret: String,
                      val data: LoginTable,
                      val msg: String
)