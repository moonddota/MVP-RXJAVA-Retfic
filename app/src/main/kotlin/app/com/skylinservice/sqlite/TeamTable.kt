package app.com.skylinservice.sqlite

import app.com.skylinservice.data.remote.requestmodel.basedata.MemberInfo
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table

/**
 * Created by liuxuan on 2017/4/20.
 */
@Table(name = "TeamTable")
class TeamTable {

    @Column(name = "id", isId = true, autoGen = false)
    var id: Long = 0

    @Column(name = "name")
    var name: String = ""

    @Column(name = "createUid")
    var createUid: String = ""

    @Column(name = "createTime")
    var createTime: String = ""

    @Column(name = "num")
    var num: String = ""

    @Column(name = "keyTime")
    var keyTime: String = ""


    @Column(name = "updateTime")
    var updateTime: Long = 0

    @Column(name = "is_default")
    var is_default: Int = 0


    var teamMembers: List<MemberInfo>? = null
}