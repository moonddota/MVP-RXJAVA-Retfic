package app.com.skylinservice.ui.set.setfg

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import android.view.WindowManager
import android.view.Gravity
import android.R.color.transparent
import android.text.Editable
import android.text.TextWatcher
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.dilogfg_editnickname.*


/**
 * Created by liuxuan on 2018/1/2.
 */
class EditNickNameFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setCanceledOnTouchOutside(false)
        val rootView = inflater.inflate(R.layout.dilogfg_editnickname, container, false)
        //Do something
        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog.window
        window!!.setBackgroundDrawableResource(R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 50)
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.width = ScreenUtils.getScreenWidth() - 100
        wlp.height = (ScreenUtils.getScreenHeight() / 1.5).toInt()
        window.attributes = wlp
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            fragment_title.text = (arguments.getString(BaseFragment.TITLE_KEY))
            fragment_des.text = (arguments.getString(BaseFragment.DES_KEY))
        }
        edit_nickname_dialog_edittv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edit_nickname_dialog_savebtn.isEnabled = p0?.toString()!!.length > 1
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

}