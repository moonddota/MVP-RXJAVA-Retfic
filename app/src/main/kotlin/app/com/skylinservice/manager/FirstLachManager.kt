package app.com.skylinservice.manager

import com.blankj.utilcode.util.SPUtils

/**
 * Created by liuxuan on 2017/12/26.
 */
class FirstLachManager {
    companion object {
        val key = "firstlach981273918279147198"
    }

    fun setFirstLanchState(lanched: Boolean) {
        SPUtils.getInstance().put(key, lanched)
    }

    fun getFirstLanchState(): Boolean {
        return !SPUtils.getInstance().getBoolean(key)
    }


}