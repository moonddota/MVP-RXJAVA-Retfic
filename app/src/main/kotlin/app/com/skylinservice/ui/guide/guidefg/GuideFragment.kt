package app.com.skylinservice.ui.guide.guidefg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.skylinservice.R
import app.com.skylinservice.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.fragment_guide.view.*

/**
 * Created by liuxuan on 2017/12/26.
 */
class GuideFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_guide, null)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
//            fragment_title.text = (arguments.getString(TITLE_KEY))
//            fragment_des.text = (arguments.getString(DES_KEY))

            fragment_src.setImageResource(arguments.getInt(IMAGE_KEY))
        }
    }


}