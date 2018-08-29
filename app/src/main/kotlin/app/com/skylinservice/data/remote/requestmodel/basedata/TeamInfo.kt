package app.com.skylinservice.data.remote.requestmodel.basedata

data class TeamInfo(val id: String,
                    var name: String?,
                    val createUid: String,
                    val createTime: String,
                    val num: String,
                    var keyTime: String?,
                    var teamMembers: List<MemberInfo>?
)