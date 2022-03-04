package com.massa844853.stockstracker.adapter

import android.app.Activity
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.News
import com.squareup.picasso.Picasso

class NewsListViewBaseAdapter(private val newsList: List<News>?, private val activity: Activity) : BaseAdapter() {
    override fun getCount(): Int {
        return newsList?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return newsList!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
        }
        val textViewNewsTitle = convertView!!.findViewById<TextView>(R.id.news_title)
        val textViewPublisher = convertView.findViewById<TextView>(R.id.news_publisher)
        val imageViewMainImage = convertView.findViewById<ImageView>(R.id.main_image)
        textViewNewsTitle.text = newsList!![position].title.toUpperCase()
        textViewPublisher.text = newsList[position].publisher
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        imageViewMainImage.minimumHeight = height / 3
        imageViewMainImage.maxHeight = height / 3
        try {
            Picasso.get().load(Uri.parse(newsList[position].main_image)).resize(width, height / 3).into(imageViewMainImage)
        } catch (ignored: Exception) {
            imageViewMainImage.minimumWidth = width
            imageViewMainImage.maxWidth = width
            imageViewMainImage.minimumHeight = height / 3
            imageViewMainImage.maxHeight = height / 3
        }
        return convertView
    }
}