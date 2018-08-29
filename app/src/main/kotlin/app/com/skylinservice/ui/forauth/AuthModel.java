package app.com.skylinservice.ui.forauth;

/**
 * Created by liuxuan on 2018/1/24.
 */

public class AuthModel {

    public AuthModel(boolean authed, int userId, String token, String tid, String userNmae, String teamName) {
        this.authed = authed;
        this.userId = userId;
        this.token = token;
        this.tid = tid;
        this.userName = userNmae;
        this.teamName = teamName;

    }

    private boolean authed;
    private int userId = 0;
    private String token = "";
    private String tid = "";
    private String userName = "";
    private String teamName = "";


}
