package app.com.skylinservice.ui.device.deviceunbindfg

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import app.com.skylinservice.R
import app.com.skylinservice.sqlite.DeviceTable
import app.com.skylinservice.ui.BaseFragment
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dilogfg_deviceunbind.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by liuxuan on 2018/1/2.
 */
class DeviceUnbindFragment() : DialogFragment() {

    companion object {
        var key = "DeviceUnbindFragment_KEY"
    }

    var phoenumberReady = false
    var validnumReady = false

    private var mObservable: Observable<Long>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setCanceledOnTouchOutside(false)
        val rootView = inflater.inflate(R.layout.dilogfg_deviceunbind, container, false)
        //Do something
        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog.window
        window!!.setBackgroundDrawableResource(R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 0)
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.width = ScreenUtils.getScreenWidth() - 100
        wlp.height = ScreenUtils.getScreenHeight() - 600
        window.attributes = wlp
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var deviceTable = arguments.getSerializable(key)
        main_activity_devicemanager_item_fly_name.text = (deviceTable as DeviceTable).flyname
        main_activity_devicemanager_item_sn.text = deviceTable.SN.toString()
        main_activity_devicemanager_item_regist_date.text = deviceTable.productdate


        devicemanger_close_unbind_dgfg.setOnClickListener {
            this@DeviceUnbindFragment.dismiss()
        }
        email_regsit_in_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var builder = AlertDialog.Builder(activity)
                builder.setIcon(R.mipmap.icon_dialog_warn)
                builder.setTitle(getString(R.string.DeviceManagerActivity_tv11))
                        .setMessage(getString(R.string.DeviceManagerActivity_tv12))
                builder.setPositiveButton(getString(R.string.activity_teammanager_btn_createteam_btn_name)) { _, _ ->
                    ToastUtils.showShort(getString(R.string.main_activity_devicemanager_dialog_btn_name))
                    this@DeviceUnbindFragment.dismiss()
                }
                builder.setNegativeButton(getString(R.string.cancle)) { _, _ ->

                }
                builder.show()
            }

        })





        regit_tv_getvalid.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                launch(CommonPool) {
                    Thread.sleep(1000)
                    var timeTotal = 60
                    mObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                    mObservable!!.observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                regist_tips.visibility = View.INVISIBLE
                                if (timeTotal > 0) {
                                    regit_tv_getvalid.isEnabled = false
                                    regit_tv_getvalid.text = timeTotal--.toString()
                                } else {
                                    regit_tv_getvalid.isEnabled = true
                                    regit_tv_getvalid.text = getString(R.string.action_sign_getvalidnum)
                                }
                            }, { e ->
                                regist_tips?.visibility = View.VISIBLE
                                regist_tips?.text = e.message
                                mObservable!!.unsubscribeOn(AndroidSchedulers.mainThread())
                            })

                }
            }

        })

        cellphone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                phoenumberReady = p0.toString().length == 11
                handleGetValidNumTVState(p0.toString())
                handleCommitBtnState()
                if (phoenumberReady && !cellphoneMatches(p0.toString()))
                    cellphone.setError(getString(R.string.title_activity_error_phoneformat), null)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        reigist_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                validnumReady = p0.toString().length == 4
                handleCommitBtnState()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun handleCommitBtnState() {
        email_regsit_in_button.isEnabled = phoenumberReady && validnumReady
    }

    private fun handleGetValidNumTVState(phone: String) {
        regit_tv_getvalid.isEnabled = phoenumberReady && cellphoneMatches(phone)
    }

    private fun cellphoneMatches(phone: String): Boolean {

        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(1[0-9][0-9]))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }

    


}