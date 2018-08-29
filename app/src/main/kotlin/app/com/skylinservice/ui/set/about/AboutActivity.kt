package app.com.skylinservice.ui.set.about

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseActivity
import com.blankj.utilcode.util.AppUtils
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.setting_activity_about)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

//        version.text = AppUtils.getAppVersionName()

    }
}
