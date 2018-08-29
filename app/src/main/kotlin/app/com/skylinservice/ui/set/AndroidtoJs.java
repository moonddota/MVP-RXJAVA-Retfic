package app.com.skylinservice.ui.set;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.SPUtils;
import com.skylin.uav.zbzy.model.LoginTable;

import app.com.skylinservice.BuildConfig;
import app.com.skylinservice.manager.SkyLinDBManager;
import app.com.skylinservice.manager.TrueNameAuthedManager;
import app.com.skylinservice.manager.UserIdManager;

/**
 * Created by liuxuan on 2018/1/18.
 */

public class AndroidtoJs {

    private SkyLinDBManager dbManager;
    private Activity activity;

    public AndroidtoJs(SkyLinDBManager dbManager, Activity activity) {
        this.dbManager = dbManager;
        this.activity = activity;
    }

    @JavascriptInterface
    public void close(String msg) {
        activity.setResult(107);
        activity.finish();
    }

    @JavascriptInterface
    public String getBaseUrl() {
        return BuildConfig.API_URL + BuildConfig.SUF_URL_V2;
    }


    @JavascriptInterface
    public void getAnswer(int msg) {
        SPUtils.getInstance().put(TrueNameAuthedManager.Companion.getKey(), msg);
        LoginTable loginTable = dbManager.getUserById(new UserIdManager().getUserId());
        loginTable.setRelStatus(msg + "");
        dbManager.update(loginTable);
    }
}
