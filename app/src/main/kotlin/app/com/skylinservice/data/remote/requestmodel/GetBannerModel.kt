package app.com.skylinservice.data.remote.requestmodel

data class GetBannerModel(val ret: String,
                          val data: MutableList<GetBannerMiddleModel>,
                          val msg: String
)

class GetBannerMiddleModel(val _id: String,
                           val content: List<BannerModel>
)

class BannerModel(val title: String,
                  val image: String,
                  val url: String
)