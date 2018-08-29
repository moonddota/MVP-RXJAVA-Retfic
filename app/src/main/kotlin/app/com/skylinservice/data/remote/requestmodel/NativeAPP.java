package app.com.skylinservice.data.remote.requestmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxuan on 2017/6/6.
 */

public class NativeAPP implements Serializable {
    public String name;
    public String iconUrl;
    public int authority;
    public String path;
    public String version;
    public String packageName;
    public int versionCode;
    public String icon;
    public String createTime;
    public String time;

    public String info;
    public String type;
    public String size;


    public long id;

    public List<UpdateInfo> items;
    public String signId;
    public boolean isWeb = false;

    //0:未安装，未下载，1：已下载，未安装，2：已安装不需更新，3：已安装需更新，4,已下载，未安装，且是更新
    public int install_state;


}
