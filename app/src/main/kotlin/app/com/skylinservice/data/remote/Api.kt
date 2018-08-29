package app.com.skylinservice.data.remote

import app.com.skylinservice.BuildConfig.*
import app.com.skylinservice.data.remote.model.Post
import app.com.skylinservice.data.remote.requestmodel.*
import app.com.skylinservice.data.remote.requestmodel.basedata.GetMacListModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface Api {

    @GET("/posts")
    fun getPosts(): Observable<List<Post>>

    @GET("/posts")
    fun getReadyData(): Observable<List<String>>


    @GET("$API_URL$SUF_URL_V2" + "Public/mapping?service=User.login")
    fun logina(@Query("username") name: String, @Query("password") pwd: String): Observable<VlideLogin>


    //给自己手机发验证码
    @GET("$API_URL$SUF_URL_V2" + "Public?service=User.verificationCode")
    fun sendValideNumForYourSelf(@Query("token") token: String): Observable<VlideNumModel>

    //给某位手机发验证码
    @GET("$API_URL$SUF_URL_V2" + "Public/mapping?service=User.verificationCodeByPhone")
    fun sendValideNumForPhone(@Query("token") token: String, @Query("telphone") telphone: String): Observable<VlideNumModel>


    //给某位手机发验证码,解绑设备
    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.UserUnBindSendSms")
    fun sendValideNumForPhoneUnbind(@Query("token") token: String, @Query("_id") _id: String, @Query("telphone") telphone: String): Observable<VlideNumModel>


    //注册
    @GET("$API_URL$SUF_URL_V2" + "Public/mapping?service=User.upuser")
    fun upUser(@Query("token") token: String, @Query("name") name: String): Observable<VlideNumModel>


    //注册
    @GET("$API_URL$SUF_URL_V2" + "Public/mapping?service=User.register")
    fun register(@Query("telphone") telphone: String, @Query("password") password: String, @Query("code") code: String): Observable<VlideRegist>


    //忘记密码
    @GET("$API_URL$SUF_URL_V2" + "Public?service=User.forgetPassword")
    fun forGotPWD(@Query("token") token: String, @Query("telphone") telphone: String, @Query("code") code: String, @Query("newPassword") newPassword: String): Observable<VlideForget>

    @GET("$API_URL$SUF_URL_V2" + "Public?service=User.setNewTelphone")
    fun changeCellPhone(@Query("token") token: String, @Query("telphone") telphone: String, @Query("code") code: String, @Query("newCode") newCode: String): Observable<VlideNumModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.myTeam")
    fun imakeTeamInfo(@Query("token") token: String): Observable<Imaketeaminfo>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.joinTeam")
    fun ijoinTeamInfo(@Query("token") token: String): Observable<Imaketeaminfo>


    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.createTeam")
    fun ceateTeam(@Query("token") token: String, @Query("name") name: String, @Query("keyTime") keyTime: Int): Observable<VlideLogin>


    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.updateTeam")
    fun updateTeam(@Query("token") token: String, @Query("tid") tid: String, @Query("name") name: String, @Query("keyTime") keyTime: Int): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.deleteTeam")
    fun deleteTeam(@Query("token") token: String, @Query("tid") tid: Long): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.addUser")
    fun addTeamMember(@Query("token") token: String, @Query("tid") tid: Long, @Query("name") name: String, @Query("telphone") telphone: String): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.deleteUser")
    fun deleteTeamMember(@Query("token") token: String, @Query("tid") tid: Long, @Query("uid") uid: Int): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.updateUser")
    fun upDateTeamMember(@Query("token") token: String, @Query("tid") tid: Long, @Query("uid") uid: Int, @Query("name") name: String): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.teamUser")
    fun getTeamMembers(@Query("token") token: String, @Query("tid") tid: Long): Observable<TeamMemeberModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/team/?service=Team.teamUser")
    fun getIJoinTeamMembers(@Query("token") token: String, @Query("tid") tid: Long): Observable<TeamIJoinMemeberModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.GetUserDevices")
    fun getBindDevices(@Query("token") token: String, @Query("user_id") user_id: String,
                       @Query("page") page: Int
                       , @Query("order") order: String): Observable<RequestBIndDevices>


    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.GetDevice")
    fun getDeviceDetail(@Query("token") token: String, @Query("_id") _id: String
    ): Observable<GetControlIdModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.GetDevice")
    fun getControlId(@Query("token") token: String, @Query("_id") _id: String
    ): Observable<GetControlIdModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/device?service=Device.updateDeviceInfoTQ")
    fun upDateDeviceInfo(@Query("token") token: String, @Query("_id") _id: String, @Query("info[device_info.device_name]]") name: String): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.GetBannerConfig")
    fun getBannerConfig(@Query("token") token: String
                        , @Query("BannerName") BannerName: String): Observable<GetBannerModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.RecordDevice")
    fun recordDevice(@Query("token") token: String,
                     @Query("_id") _id: Int
                     , @Query("device_info") device_info: String): Observable<TeamMemeberModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.UserBindDevice")
    fun bindDevice(@Query("token") token: String
                   , @Query("activation_code") activation_code: String): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.UserUnBindDevice")
    fun unBindDevice(@Query("token") token: String, @Query("_id") _id: String,
                     @Query("telphone") telphone: String
                     , @Query("sms_code") sms_code: String): Observable<VlideNumModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/device/?service=Device.UserUnBindSendSms")
    fun devicesendSMS(@Query("token") token: String, @Query("_id") _id: String,
                      @Query("telphone") telphone: Int
    ): Observable<TeamMemeberModel>


    @GET("$API_URL$SUF_URL_V3" + "api/user/team/set_work_team_default")
    fun setDefaultTieam(@Query("token") token: String, @Query("team_id") team_id: String
    ): Observable<VlideNumModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/store?service=App.getAppInfo")
    fun newestAppInfo(@Query("id") id: Int): Observable<GetAPPInfoModel>

    @GET("$API_URL$SUF_URL_V2" + "Public/store/?service=App.downApp")
    fun downLoadApk(@Query("id") id: Int): Observable<Any>


    @GET("$API_URL$SUF_URL_V2" + "Public/store/?service=App.getAppList")
    fun getNativeApp(): Observable<GetAPPsModel>


    //微信
    @GET("$API_URL$SUF_URL_V2" + "Public/?")
    fun weixin_connect(@Query("code") code: String, @Query("appId") appId: String): Observable<WeixinConnectModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/?service=User.loginWithWechat")
    fun weixin_bind(@Query("wechatId") wechatId: String, @Query("wechatCode") wechatCode: String,
                    @Query("telphone") telphone: String, @Query("telphoneCode") telphoneCode: String
    ): Observable<WeixinBindModel>


    @FormUrlEncoded
    @POST("$API_URL$SUF_URL_V2" + "Public/?service=Log.upDebug")
    fun uploadCrashFile(@Query("token") token: String, @FieldMap map: Map<String, String>): Call<GeneralModel<Any>>


    @GET("$API_URL$SUF_URL_V3" + "api/user/device/team_aircraft_list")
    fun getMaclist(@Query("token") token: String, @Query("team_id") team_id: String
    ): Observable<GetMacListModel>


    @GET("$API_URL$SUF_URL_V3" + "api/user/device/aircraft_list")
    fun getMaclistuser(@Query("token") token: String
    ): Observable<GetMacListModel>


    @GET("$API_URL$SUF_URL_V3" + "api/device/details")
    fun getDeviceDetailssss(@Query("token") token: String, @Query("device_sn") device_sn: String
    ): Observable<GetDeviceDetailsssModel>


//    @FormUrlEncoded
//    @POST("$API_URL$SUF_URL_V3" + "api/statistic/work")
//    fun getDeviceStatistics(@Query("token") token: String, @Query("page") page: Int
//                            , @Query("page_size") page_size: Int
//                            , @Query("statistic_time") statistic_time: String
//                            , @Query("work_user_id") work_user_id: String
//                            , @Query("work_team_id") work_team_id: String
//                            , @Query("device_sn") device_sn: String
//    ): Observable<GetDeviceStatisticsModel>

    @FormUrlEncoded
    @POST("$API_URL$SUF_URL_V3" + "api/statistic/work")
    fun getDeviceStatisticsPost(@Query("token") token: String, @FieldMap map: Map<String, String>
    ): Observable<GetDeviceStatisticsModel>


    @GET("$API_URL$SUF_URL_V3" + "api/statistic/work")
    fun getDeviceStatisticsNoteam(@Query("token") token: String, @Query("page") page: Int
                                  , @Query("page_size") page_size: Int
                                  , @Query("statistic_time") statistic_time: String
                                  , @Query("work_user_id") work_user_id: String
                                  , @Query("device_sn") device_sn: String
    ): Observable<GetDeviceStatisticsModel>


    @GET("$API_URL$SUF_URL_V3" + "api/device/operator")
    fun getDetialAllOperater(@Query("token") token: String, @Query("device_sn") device_sn: String
    ): Observable<GetALlOperaterModel>


    @GET("$API_URL$SUF_URL_V2" + "Public/store/?service=App.getAppList2")
    fun getApp(): Observable<GetAPPsModel>


}


