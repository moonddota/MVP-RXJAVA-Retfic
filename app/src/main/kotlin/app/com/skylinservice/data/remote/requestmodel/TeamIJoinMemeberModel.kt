package app.com.skylinservice.data.remote.requestmodel

import app.com.skylinservice.data.remote.requestmodel.basedata.TeamInfo

data class TeamIJoinMemeberModel(val ret: String,
                                 val data: List<TeamInfo>,
                                 val msg: String
)