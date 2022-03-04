package com.massa844853.stockstracker.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.Statistic

class StatisticsListViewBaseAdapter(private val eleStatistic: List<Statistic>?, private val activity: Activity) : BaseAdapter() {
    override fun getCount(): Int {
        return eleStatistic?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return eleStatistic!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.statistics_list_item, parent, false)
        }
        (convertView!!.findViewById<View>(R.id.textViewName) as TextView).text = eleStatistic!![position].name
        (convertView.findViewById<View>(R.id.textViewValue) as TextView).text = eleStatistic[position].value
        return convertView
    }
}