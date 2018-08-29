package app.com.skylinservice.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import app.com.skylinservice.AppApplication
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseActivity
import com.blankj.utilcode.util.ToastUtils
import com.liulishuo.filedownloader.FileDownloader
import java.util.*

class MainRefactActivity : BaseActivity() {

//    var mainrefactfg: MainRefactFragment? = null
//    var personrefactfg: PersonCenterRefactFragment? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main_refact)


//        skylininditor.setRelativeViewPager(mViewPager, null)
//
//        mViewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
//
//        skylininditor.setTitle("首页", "我的")

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var fgList = ArrayList<Fragment>(4)

        init {
            for (i in 0..1) {
                when (i) {
                    0 -> {
                        createIMakeFragment("使用天麒服务,所有应用账户同步", "使用天麒服务登录,所有天麒app均无需再次登录,直接进入使用")
                    }
                    1 -> {
                        createIJoinFragment("保持应用为最新版", "新版功能,即使使用")
                    }
                }


            }
        }

        private fun createIMakeFragment(title: String, des: String) {
//            mainrefactfg = MainRefactFragment()
//            val data = Bundle()
//            data.putString(BaseFragment.TITLE_KEY, title)
//            data.putString(BaseFragment.DES_KEY, des)
//            (mainrefactfg as MainRefactFragment).arguments = data
//            fgList.add((mainrefactfg as MainRefactFragment))
        }

        private fun createIJoinFragment(title: String, des: String) {
//            personrefactfg = PersonCenterRefactFragment()
//            val data = Bundle()
//            data.putString(BaseFragment.TITLE_KEY, title)
//            data.putString(BaseFragment.DES_KEY, des)
//            (personrefactfg as PersonCenterRefactFragment).arguments = data
//            fgList.add((personrefactfg as PersonCenterRefactFragment))
        }


        override fun getItem(position: Int): Fragment {
            return fgList[position]
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    private var isExit = false
    override fun onBackPressed() {
        exit()
    }


    private fun exit() {
        if (!isExit) {
            isExit = true
            ToastUtils.showShort(getString(R.string.MainActivity_tv17))
            // 利用handler延迟发送更改状态信息
            Handler().postDelayed({ isExit = false }, 2000)
        } else {
            FileDownloader.getImpl().unBindService()
            AppApplication.app.tmpList.clear()
            finish()
            System.exit(0)
        }
    }


}
