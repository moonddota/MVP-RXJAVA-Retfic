package app.com.skylinservice.data.remote.requestmodel

data class WeixinConnectModel(val ret: String,
                              var data: WeixinConnectMiddleModel,
                              val msg: String
)


data class WeixinConnectMiddleModel(var openid: String,
                                    var nickname: String,
                                    var province: String,
                                    var city: String,
                                    var country: String,
                                    var headimgurl: String,
                                    var unionid: String,
                                    var privilege: List<String>,
                                    var sex: String,
                                    var code: String
)