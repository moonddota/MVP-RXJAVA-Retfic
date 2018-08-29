package app.com.skylinservice.ui

import android.view.View

class DataBindModel<out T>(val layoutResId: Int, val variableId: Int, val model: T) {

//    var binding: ViewDataBinding? = null
//    var layoutInflater: LayoutInflater? = null
//
//    override fun buildView(parent: ViewGroup?): View {
//        val li = layoutInflater ?: LayoutInflater.from(parent?.context).apply { layoutInflater = this }
//
//        binding = DataBindingUtil.inflate(li, layoutResId, parent, false)
//
//        return binding?.root!!
//    }
//
//    override fun bind(view: View?) {
//        super.bind(view)
//
//        binding?.let {
//            it.setVariable(variableId, model)
//            it.executePendingBindings()
//        }
//    }
}