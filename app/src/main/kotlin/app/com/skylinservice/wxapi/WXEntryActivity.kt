package app.com.skylinservice.wxapi

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import app.com.skylinservice.AppApplication
import app.com.skylinservice.R
import app.com.skylinservice.data.remote.requestmodel.VlideNumModel
import app.com.skylinservice.data.remote.requestmodel.WeixinBindModel
import app.com.skylinservice.data.remote.requestmodel.WeixinConnectModel
import app.com.skylinservice.manager.CustomerGsonConvertFactory.ApiException
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.TrueNameAuthedManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.main.MainActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import app.com.skylinservice.ui.userrelative.login.LoginContract
import app.com.skylinservice.ui.userrelative.login.regist.RegistAccountActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_weixinback.*
import sjj.alog.Log
import javax.inject.Inject


class WXEntryActivity : BaseActivity(), IWXAPIEventHandler, LoginContract.View {


    override fun showweixinConnect(post: WeixinConnectModel) {
        dialog?.dismiss()
        if (post.ret.toInt() == 200) {
            presenter.weixin_bind(post.data.unionid, post.data.code, "", "")
        } else {
            ToastUtils.showShort(post.msg)

        }


    }

    override fun showweixinBind(post: WeixinBindModel) {
        dialog?.dismiss()
        when {
            post.ret.toInt() == 200 -> {
                UserIdManager().pustUserID(post.data.id.toLong())
                skylindb.update(post.data)

                Log.e("aaa   "+  post.data.toString())

                SPUtils.getInstance().put("userName",post.data.name)
                SPUtils.getInstance().put("token", post.data.token)
                TrueNameAuthedManager().setFirstLanchState(post.data.relStatus.toInt())
                startActivity(Intent(this@WXEntryActivity, MainActivity::class.java))
                finishAffinity()
                val intent = Intent()
                intent.action = "closeLogin"
                sendBroadcast(intent)
            }
            post.ret.toInt() == 404 -> {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv37))
                var intent = Intent(WXEntryActivity@ this, RegistAccountActivity::class.java)
                intent.putExtra("code", wechatCode)
                startActivity(intent)

            }
            else -> ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv28))
        }
    }


    override fun showSend(posts: VlideNumModel) {
    }


    override fun showAPIExcpetion(exception: ApiException) {
        dialog?.dismiss()
        if (!exception.isTokenExpried) {
            ToastUtils.showShort(exception.message)
            if (exception.mErrorCode == 404) {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv37))
                var intent = Intent(WXEntryActivity@ this, RegistAccountActivity::class.java)
                intent.putExtra("code", wechatCode)
                startActivity(intent)
                finish()
            }

        } else {
            finishAffinity()
            startActivity(Intent(this@WXEntryActivity, LoginActivity::class.java))
        }
    }

    var dialog: ProgressDialog? = null

    override fun setLoadingIndicatorVisible(loadingVisible: Boolean) {
        if (loadingVisible) {
            dialog?.setCancelable(false)
            dialog?.show()
        } else {
            dialog?.dismiss()
        }


    }

    override fun showNoConnectivityError() {
        ToastUtils.showShort(getString(R.string.DeviceManagerActivity_tv3))
        dialog?.dismiss()
        finish()
    }

    override fun showUnknownError() {
//        ToastUtils.showShort("未知错误")
        dialog?.dismiss()
        finish()
    }


    @Inject
    lateinit var presenter: LoginContract.Presenter

    @Inject
    lateinit var skylindb: SkyLinDBManager

    private var wechatCode = ""

    private val RETURN_MSG_TYPE_LOGIN = 1
    private val RETURN_MSG_TYPE_SHARE = 2

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_weixinback)
        activityComponent.inject(this)
        presenter.attachView(this)

        dialog = ProgressDialog(this)
        //如果没回调onResp，八成是这句没有写
        AppApplication.mWxApi?.handleIntent(intent, this)

    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    override fun onResp(resp: BaseResp) {

        when (resp.errCode) {

            BaseResp.ErrCode.ERR_AUTH_DENIED, BaseResp.ErrCode.ERR_USER_CANCEL -> if (RETURN_MSG_TYPE_SHARE == resp.type) {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv39))
                finish()
            } else {
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv40))
                finish()
            }
            BaseResp.ErrCode.ERR_OK -> when (resp.type) {
                RETURN_MSG_TYPE_LOGIN -> {
                    //拿到了微信返回的code,立马再去请求access_token
                    wechatCode = (resp as SendAuth.Resp).code
                    presenter.weixin_connect(wechatCode)

                }

                RETURN_MSG_TYPE_SHARE -> {
                    ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv41))
                    finish()
                }
            }//就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
            else -> {
                finish()
                ToastUtils.showShort(getString(R.string.TeamManagerActivity_tv42))
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detachView(this)
        if (!configPersistDelegate.instanceSaved) {
            presenter.destroy()
        }
    }

}
