package app.com.skylinservice.manager

import com.blankj.utilcode.util.SPUtils

/**
 * Created by liuxuan on 2017/12/26.
 */
class UserIdManager {
    companion object {
        val key = "userIDsladnfsafklsfdh230200"
    }

    fun pustUserID(userid: Long) {
        SPUtils.getInstance().put(key, userid)
    }

    fun getUserId(): Long {
        return SPUtils.getInstance().getLong(key, 0)
    }


}