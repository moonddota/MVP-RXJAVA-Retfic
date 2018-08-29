package app.com.skylinservice.ui.personcenter.choose

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import app.com.skylinservice.R
import app.com.skylinservice.manager.customeview.CustomDatePicker
import app.com.skylinservice.ui.BaseActivity
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_date_choose.*
import java.text.SimpleDateFormat
import java.util.*


class DateChooseActivity : BaseActivity(), View.OnClickListener {

    private var selectDate: RelativeLayout? = null
    private var selectTime: RelativeLayout? = null
    private var currentDate: TextView? = null
    private var currentTime: TextView? = null
    private var customDatePicker1: CustomDatePicker? = null
    private var customDatePicker3: CustomDatePicker? = null


    private var selcetrule = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_choose)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener({
            finish()
        })
        toolbar.title = getString(R.string.DateChooseActivity_tv1)
        toolbar.showOverflowMenu()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.adb_ic_ab_back_material)

        selectTime = findViewById<View>(R.id.selectTime) as RelativeLayout
        selectTime!!.setOnClickListener(this)
        selectDate = findViewById<View>(R.id.selectDate) as RelativeLayout
        selectDate!!.setOnClickListener(this)
        currentDate = findViewById<View>(R.id.currentDate) as TextView
        currentTime = findViewById<View>(R.id.currentTime) as TextView



        time_1.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.primary))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))
            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            timefrome!!.setText(TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 0 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)))
            selcetrule = "-0"

        }

        time_2.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.primary))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))
            var time = TimeUtils.millis2String(TimeUtils.string2Millis(TimeUtils.getNowString(getNowDay()) + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 1 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
            var formattim1 = time.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "/" + time.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] + "/" + time.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
            selcetrule = formattim1 + "-" + formattim1
            timeto!!.setText(time)
            timefrome!!.setText(time)

        }
        time_7.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.primary))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))
            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            timefrome!!.setText(TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 7 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)))
            selcetrule = "-7"

        }
        time_15.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.primary))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))
            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            timefrome!!.setText(TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 15 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)))
            selcetrule = "-15"


        }
        time_30.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.primary))
            time_all.setTextColor(resources.getColor(R.color.text_grey))
            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            timefrome!!.setText(TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 30L * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)))
            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            selcetrule = "-30"
        }

        time_all.setOnClickListener {
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.primary))
//            timefrome!!.setText("2010-01-01")
//            timeto!!.setText(TimeUtils.getNowString(getNowDay()))
            timefrome!!.setText("-")
            timeto!!.setText("-")
            selcetrule = "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))

        }

        timefrome.setOnClickListener {
            if (timefrome.text.toString().equals("-")) {
                timefrome.setTextColor(resources.getColor(R.color.primary))
                timeto.setTextColor(resources.getColor(R.color.text_grey))
                customDatePicker1!!.show("2010-01-01")
            } else {
                timefrome.setTextColor(resources.getColor(R.color.primary))
                timeto.setTextColor(resources.getColor(R.color.text_grey))
                customDatePicker1!!.show(timefrome!!.text.toString())
            }

        }

        timeto.setOnClickListener {
            if (timeto.text.toString().equals("-")) {
                timefrome.setTextColor(resources.getColor(R.color.text_grey))
                timeto.setTextColor(resources.getColor(R.color.primary))
                customDatePicker3!!.show(TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)))
            } else {
                timefrome.setTextColor(resources.getColor(R.color.text_grey))
                timeto.setTextColor(resources.getColor(R.color.primary))
                customDatePicker3!!.show(timeto!!.text.toString())
            }

        }


        timefrome.isCursorVisible = false
        timefrome.isFocusable = false
        timefrome.isFocusableInTouchMode = false

        timeto.isCursorVisible = false
        timeto.isFocusable = false
        timeto.isFocusableInTouchMode = false


        initDatePicker()

        if (intent.getStringExtra("choosedtime") != null) {
            var time = TimeUtils.millis2String(TimeUtils.string2Millis(TimeUtils.getNowString(getNowDay()) + " 00:00:00", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)) - 1 * 24 * 60 * 60 * 1000, SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
            when (intent.getStringExtra("choosedtime")) {
                "-0" -> {
                    time_1.performClick()

                }
                time + "-" + time -> {
                    time_2.performClick()
                }
                "-7" -> {
                    time_7.performClick()
                }
                "-15" -> {
                    time_15.performClick()
                }
                "-30" -> {
                    time_30.performClick()
                }
                "2010/01/01-" + TimeUtils.getNowString(SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) -> {
                    time_all.performClick()
                }
                else -> {
                    time_1.setTextColor(resources.getColor(R.color.text_grey))
                    time_2.setTextColor(resources.getColor(R.color.text_grey))
                    time_7.setTextColor(resources.getColor(R.color.text_grey))
                    time_15.setTextColor(resources.getColor(R.color.text_grey))
                    time_30.setTextColor(resources.getColor(R.color.text_grey))
                    time_all.setTextColor(resources.getColor(R.color.text_grey))
                    var time1 = intent.getStringExtra("choosedtime").split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    var time2 = intent.getStringExtra("choosedtime").split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    var formattim1 = time1.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "-" + time1.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] + "-" + time1.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
                    var formattim2 = time2.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "-" + time2.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] + "-" + time2.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
                    selcetrule = time1 + "-" + time2
                    timefrome.setText(formattim1)
                    timeto.setText(formattim2)
//                    customDatePicker1!!.show(formattim1)
//                    customDatePicker3!!.show(formattim2)

                }
            }
        } else {
            time_all.performClick()

        }


    }

    private fun getNowDay(): SimpleDateFormat? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.selectDate ->
                // 日期格式为yyyy-MM-dd
                customDatePicker1!!.show(currentDate!!.getText().toString())

            R.id.selectTime ->
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker3!!.show(currentTime!!.text.toString())
        }
    }

    private fun initDatePicker() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        val now = sdf.format(Date())

        customDatePicker1 = CustomDatePicker(this, CustomDatePicker.ResultHandler { time ->

            timefrome.setTextColor(resources.getColor(R.color.text_grey))
            timeto.setTextColor(resources.getColor(R.color.text_grey))
            // 回调接口，获得选中的时间
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))



            if (TimeUtils.string2Millis(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0], SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) > TimeUtils.string2Millis(timeto.text.toString(), SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))) {
                if (timefrome.text.toString().equals("-")) {
                    timefrome!!.setText(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])

                } else {
                    ToastUtils.showShort(getString(R.string.DateChooseActivity_tv2))
                    customDatePicker1!!.show(timefrome!!.getText().toString())

                }

            } else {
                timefrome!!.setText(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            }
//            if (timefrome.text.toString() != "-" && timeto.text.toString() != "-") {
//
//                selcetrule = TimeUtils.millis2String(TimeUtils.string2Millis(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
//                        SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) + "-" +
//                        TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
//                                SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
//            }

            if (timefrome.text.toString() != "-" && timeto.text.toString() != "-") {
                selcetrule = TimeUtils.millis2String(TimeUtils.string2Millis(timefrome.text.toString() + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
                        SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) + "-" +
                        TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
                                SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
            }

        }, "2010-01-01 00:00", now) // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1!!.showSpecificTime(false) // 不显示时和分
        customDatePicker1!!.setIsLoop(false) // 不允许循环滚动

        customDatePicker3 = CustomDatePicker(this, CustomDatePicker.ResultHandler { time ->

            timefrome.setTextColor(resources.getColor(R.color.text_grey))
            timeto.setTextColor(resources.getColor(R.color.text_grey))
            // 回调接口，获得选中的时间
            time_1.setTextColor(resources.getColor(R.color.text_grey))
            time_2.setTextColor(resources.getColor(R.color.text_grey))
            time_7.setTextColor(resources.getColor(R.color.text_grey))
            time_15.setTextColor(resources.getColor(R.color.text_grey))
            time_30.setTextColor(resources.getColor(R.color.text_grey))
            time_all.setTextColor(resources.getColor(R.color.text_grey))





            if (TimeUtils.string2Millis(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0], SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) < TimeUtils.string2Millis(timefrome.text.toString(), SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))) {
                if (timeto.text.toString().equals("-")) {
                    timeto!!.setText(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])

                } else {
                    ToastUtils.showShort(getString(R.string.DateChooseActivity_tv2))
                    customDatePicker3!!.show(timeto!!.getText().toString())

                }

            } else {
                timeto!!.setText(time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            }

            if (timefrome.text.toString() != "-" && timeto.text.toString() != "-") {
                selcetrule = TimeUtils.millis2String(TimeUtils.string2Millis(timefrome.text.toString() + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
                        SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)) + "-" +
                        TimeUtils.millis2String(TimeUtils.string2Millis(timeto.text.toString() + " 00:00", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)),
                                SimpleDateFormat("yyyy/MM/dd", Locale.CHINA))
            }


        }, "2010-01-01 00:00", now) // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker3!!.showSpecificTime(false) // 不显示时和分
        customDatePicker3!!.setIsLoop(false) // 不允许循环滚动

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.datemenu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
//        presenter.detachView(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                if (selcetrule == "") {
                    ToastUtils.showLong(getString(R.string.DateChooseActivity_tv3))
                    return true
                }
                if (timefrome.text.toString().equals("-") && !timeto.text.toString().equals("-")) {
                    ToastUtils.showLong(getString(R.string.DateChooseActivity_tv3))
                    return true
                }
                if (timeto.text.toString().equals("-") && !timefrome.text.toString().equals("-")) {
                    ToastUtils.showLong(getString(R.string.DateChooseActivity_tv3))
                    return true
                }
                var itent1 = Intent();
                itent1.putExtra("selecttime", selcetrule)
                setResult(101, itent1)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
