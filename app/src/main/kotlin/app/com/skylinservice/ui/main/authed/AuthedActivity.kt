package app.com.skylinservice.ui.main.authed

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_authed.*

class AuthedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authed)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.MainActivity_tv1)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)
    }
}
