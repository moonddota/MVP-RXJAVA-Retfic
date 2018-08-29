package app.com.skylinservice.ui.guide

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.WindowManager
import app.com.skylinservice.R
import app.com.skylinservice.manager.FirstLachManager
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.manager.UserIdManager
import app.com.skylinservice.ui.BaseActivity
import app.com.skylinservice.ui.BaseFragment
import app.com.skylinservice.ui.guide.guidefg.GuideFragment
import app.com.skylinservice.ui.main.MainActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*
import javax.inject.Inject

class GuideActivity : BaseActivity(), GuideContract.View {


    @Inject
    lateinit var presenter: GuideContract.Presenter

    @Inject
    lateinit var skylinDbManager: SkyLinDBManager

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    private var isLastPage = false
    private var isDragPage = false

    private var isGo = false

//    private var mViewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
//            finish()
//            return
//        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guide)

        activityComponent.inject(this)

        presenter.attachView(this)

        presenter.chargeGuideView()

    }

    override fun showMain() {
        runOnUiThread {
            synchronized(isGo)
            {
                var x = UserIdManager().getUserId()
                if (!isGo && x != 0L && skylinDbManager.getUserById(x).token.length > 1) {
                    isGo = true
                    startActivity(Intent(this@GuideActivity, MainActivity::class.java))
                    finish()
                } else if (!isGo) {
                    isGo = true
                    startActivity(Intent(this@GuideActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

    }

    override fun showGuide() {
        runOnUiThread {
            iv_welcome.visibility = View.INVISIBLE
            mViewPager!!.visibility = View.VISIBLE

            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)


            mViewPager!!.setAdapter(mSectionsPagerAdapter)


            mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    presenter.touchEndPage(position, isDragPage, mSectionsPagerAdapter!!.list)
                }

                override fun onPageSelected(position: Int) {
                    isLastPage = position == mSectionsPagerAdapter!!.list.size - 1

                }

                override fun onPageScrollStateChanged(state: Int) {
                    isDragPage = state == 1
                }
            })
        }

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        internal var list = ArrayList<Fragment>(4)

        init {
            for (i in 0..3) {
                when (i) {
                    0 -> {
                        createGuideFragment(R.mipmap.page_guide_1)
                    }
                    1 -> {
                        createGuideFragment(R.mipmap.page_guide_2)
                    }
                    2 -> {
                        createGuideFragment(R.mipmap.page_guide_3)
                    }
                    3 -> {
                        createGuideFragment(R.mipmap.page_guide_4)
                    }
                }


            }
        }

        private fun createGuideFragment(srcId: Int) {
            val fragment = GuideFragment()
            val data = Bundle()
//            data.putString(BaseFragment.TITLE_KEY, title)
//            data.putString(BaseFragment.DES_KEY, des)
            data.putInt(BaseFragment.IMAGE_KEY, srcId)

            fragment.setArguments(data)
            list.add(fragment)
        }


        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }
}