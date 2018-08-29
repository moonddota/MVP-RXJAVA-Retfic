package app.com.skylinservice.data.remote.requestmodel

import com.skylin.uav.zbzy.model.LoginTable

data class WeixinBindModel(val ret: String,
                           var data: LoginTable,
                           val msg: String
)