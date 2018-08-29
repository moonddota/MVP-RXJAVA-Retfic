package app.com.skylinservice.manager

import com.blankj.utilcode.util.SPUtils

/**
 * Created by liuxuan on 2017/12/26.
 */
class FirstLachTeamManager {
    companion object {
        val key = "firstlachteammanager2038293348294"
    }

    fun setFirstLanchState(lanched: Boolean) {
        SPUtils.getInstance().put(key, lanched)
    }

    fun getFirstLanchState(): Boolean {
        return !SPUtils.getInstance().getBoolean(key)
    }


}