package com.skylin.uav.zbzy.model

import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table

/**
 * Created by liuxuan on 2017/4/20.
 */
@Table(name = "LoginTable")
class LoginTable {

    @Column(name = "id", isId = true, autoGen = false)
    var id: Int = 0

    @Column(name = "username")
    var username: String = ""

    @Column(name = "name")
    var name: String = ""

    @Column(name = "ugroup")
    var ugroup: String = ""

    @Column(name = "authority")
    var authority: Int = 0

    @Column(name = "gauthority")
    var gauthority: Int = 0

    @Column(name = "telphone")
    var telphone: String = ""

    @Column(name = "region")
    var region: String = ""

    @Column(name = "info")
    var info: String = ""


    @Column(name = "headerImg")
    var headerImg: String = ""

    @Column(name = "relStatus")
    var relStatus: String = ""

    @Column(name = "hadUavStatus")
    var hadUavStatus: String = ""

    @Column(name = "token")
    var token: String = ""

    @Column(name = "lastLoginTime")
    var lastLoginTime: String = ""

    @Column(name = "password")
    var password: String = ""

    @Column(name = "wechatId")
    var wechatId: String = ""

    override fun toString(): String {
        return "LoginTable(id=$id, username='$username', name='$name', ugroup='$ugroup', authority=$authority, gauthority=$gauthority, telphone='$telphone', region='$region', info='$info', headerImg='$headerImg', relStatus='$relStatus', hadUavStatus='$hadUavStatus', token='$token', lastLoginTime='$lastLoginTime', password='$password', wechatId='$wechatId')"
    }


}

