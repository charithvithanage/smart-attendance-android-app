package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import lnbti.charithgtp01.smartattendanceuserapp.model.User

class UserSpinnerAdapter(context: Context, userList: List<User>) :
    ArrayAdapter<User>(context, 0, userList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)
        getItem(position)?.let {
            view.findViewById<TextView>(android.R.id.text1).text =
                "$it.firstName $it.lastName"
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        getItem(position)?.let {
            view.findViewById<TextView>(android.R.id.text1).text =
                "$it.firstName $it.lastName"
        }
        return view
    }
}