package app.com.skylinservice.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blankj.utilcode.util.Utils;

import app.com.skylinservice.manager.UserIdManager;

/**
 * Created by liuxuan on 2018/3/29.
 */

public class DB extends SQLiteOpenHelper {
    public DB(Context context, String name) {
        super(context, name, null, 1);
    }

    public DB(Context context, String name, boolean init) {
        super(context, name, null, 1);
        Utils.init(context);
    }

    /**
     * 首次创建 数据库的时候调用
     * 一般 把建库 和 键表操作 放在这里
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 一般 把建库 和 键表操作 放在这里


//        db.execSQL("create table if not exists labelnet(_id integer primary key autoincrement,userid integer,teamchanged integer,tid integer,package integer)");
//
//        if (new UserIdManager().getUserId() != 0) {
//            //初始化 20 个值
//            ContentValues values = new ContentValues();
//            values.put("_id", new UserIdManager().getUserId());
//            values.put("teamchanged", 0);
//            values.put("com.skylin.uav.v02.dev", 0);
//            values.put("com.skylin.uav.v02", 0);
//            values.put("com.skylin.uav.zbzy.xinshiqi", 0);
//            values.put("com.skylin.uav.zbzy", 0);
//            values.put("com.skylin.uav.basestation", 0);
//            values.put("uav.skylin.com.upgradeforfirmware", 0);
//            values.put("com.skylin.uav.rtkjizhan", 0);
//            values.put("com.skylin.uav.nongtiancehui", 0);
//
//            values.put("tid", 0);
//            db.insert("labelnet", null, values);
//            values.clear();
//        }
    }

    /**
     * 当数据库版本发送改名的时候，自动调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当数据库版本发送改名的时候，自动调用

    }
}
