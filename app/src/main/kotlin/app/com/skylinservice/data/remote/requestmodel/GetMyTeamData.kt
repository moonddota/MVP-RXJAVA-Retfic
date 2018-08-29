package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo
import app.com.skylinservice.sqlite.TeamTable
import com.skylin.uav.zbzy.model.LoginTable

data class GetMyTeamData(var data: MutableList<TeamTable>,
                         val count: Int

)