package app.com.skylinservice.ui.forauth

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.com.skylinservice.R
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.manager.utils.GsonUtils
import app.com.skylinservice.ui.BaseActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import sjj.alog.Log
import javax.inject.Inject

class ForAuthActivity : BaseActivity() {

    @Inject
    lateinit var skylindb: SkyLinDBManager

    var timeOK = true
    var authOK = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_auth)
        activityComponent.inject(this)


//        var teams = skylindb.getTeam()
//        if (teams.size > 0) {
//
//            teams.forEach { team ->
//                if (team.updateTime == 0L || System.currentTimeMillis() - team.updateTime > TimeUtils.string2Millis(team.keyTime)) {
//                    ToastUtils.showShort("授权时间超时,请刷新天麒服务APP的数据")
//                    timeOK = false
//                    return@forEach
//                }
//            }
//
//            teams.forEach { team ->
//                if (!cotansSN(sn)) {
//                    ToastUtils.showShort("您没有操作此飞机的权限,请到天麒服务查看")
//                    authOK = false
//                    return@forEach
//                }
//            }
//
//        } else {
//            timeOK = false
//            authOK = false
//        }
//
//
//        if (timeOK && authOK) {
//            ToastUtils.showShort("授权成功")
//        } else {
//            ToastUtils.showShort("授权失败")
//        }


//        if (SPUtils.getInstance().getBoolean("isTeamChanged")) {
//            SPUtils.getInstance().put("isTeamChanged", false)
//            setResult(Activity.RESULT_CANCELED, intent.putExtra("itoken", GsonUtils.createGsonString(AuthModel(false, 0, SPUtils.getInstance().getString("token"), "0", SPUtils.getInstance().getString("userName"), SPUtils.getInstance().getString("choosedteamname")))))
//            finish()
//            return
//        } else {
        var teamid = 0L

        if (SPUtils.getInstance().getLong("choosedteamid") != 0L)
            teamid = SPUtils.getInstance().getLong("choosedteamid")

        if (UserIdManager().getUserId() != 0L) {

            val toInt = UserIdManager().getUserId().toInt()
            var aa =  SPUtils.getInstance().getString("token")
            var bb = SPUtils.getInstance().getString("userName")
            var cc =SPUtils.getInstance().getString("choosedteamname")

            Log.e("11  "+toInt +"  "+aa+"  "+   teamid.toString() +"  "+bb+"   "+cc)
            setResult(Activity.RESULT_OK,
                    intent.putExtra("itoken", GsonUtils.createGsonString(AuthModel(true,
                            UserIdManager().getUserId().toInt(), SPUtils.getInstance().getString("token"), teamid.toString(), SPUtils.getInstance().getString("userName"), SPUtils.getInstance().getString("choosedteamname")))))
            finish()
        } else {


            val toInt = UserIdManager().getUserId().toInt()
            var aa =  SPUtils.getInstance().getString("token")
            var bb = SPUtils.getInstance().getString("userName")
            var cc =SPUtils.getInstance().getString("choosedteamname")
            Log.e("22  "+toInt +"  "+aa+"  "+   teamid.toString() +"  "+bb+"   "+cc)


            setResult(Activity.RESULT_CANCELED, intent.putExtra("itoken", GsonUtils.createGsonString(AuthModel(false, 0, SPUtils.getInstance().getString("token"), teamid.toString(), SPUtils.getInstance().getString("userName"), SPUtils.getInstance().getString("choosedteamname")))))
            finish()
        }
//        }


    }

    private fun cotansSN(sn: String?): Boolean {

        return true
    }
}
