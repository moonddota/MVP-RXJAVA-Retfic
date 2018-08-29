package app.com.skylinservice.manager

import com.blankj.utilcode.util.SPUtils

/**
 * Created by liuxuan on 2017/12/26.
 */
class TrueNameAuthedManager {
    companion object {
        val key = "TrueNameAuthedManager09348208403234203482"
    }

    fun setFirstLanchState(lanched: Int) {
        SPUtils.getInstance().put(key, lanched)
    }

    fun getFirstLanchState(): Int {
        return SPUtils.getInstance().getInt(key, 0)
    }


}