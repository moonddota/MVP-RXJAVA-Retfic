package app.com.skylinservice.data.remote.requestmodel;

import java.io.Serializable;

/**
 * Created by liuxuan on 2017/6/6.
 */

public class WebApp implements Serializable {
    public String name;
    public String iconUrl;
    public int authority;
    public String url;
    public Boolean isWeb = true;

}
