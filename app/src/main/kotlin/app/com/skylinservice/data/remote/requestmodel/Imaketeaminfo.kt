package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo

data class Imaketeaminfo(val ret: String,
                         var data: GetMyTeamData,
                         val msg: String
)