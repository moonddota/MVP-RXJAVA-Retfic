package app.com.skylinservice.ui.main.mainfg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by liuxuan on 2017/12/26.
 */
class MainFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, null)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            fragment_title.text = (arguments.getString(TITLE_KEY))
            fragment_des.text = (arguments.getString(DES_KEY))
        }
    }


}