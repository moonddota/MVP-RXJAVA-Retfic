package app.com.skylinservice.data.remote.requestmodel

data class GetDeviceStatisticsModel(val ret: String,
                                    val data: GetDeviceStatisticsMiddelModel,
                                    val msg: String
)

class GetDeviceStatisticsMiddelModel(val summary: StatisticsModelTotal,
                                     val list: MutableList<WorkData>
)


class StatisticsModelTotal(val work_area: String,
                           val fly_hour: String,
                           val work_num: String
)

class WorkData(val work_at: String,
               val work_area: String,
               val work_time: String,
               val work_device: String,
               val work_address: String,
               val work_team: String,
               val work_user: String

)
