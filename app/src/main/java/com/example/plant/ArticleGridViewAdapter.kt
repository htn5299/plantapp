package com.example.plant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.plant.models.Article
import com.squareup.picasso.Picasso

internal class ArticleGridViewAdapter (
    private val articleList: List<Article>,
    private val context: Context
) : BaseAdapter(){
    private var layoutInflater: LayoutInflater? = null
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var owner: TextView
    private lateinit var img: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return articleList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.article_item, null)
        }
        title = convertView!!.findViewById(R.id.txtTitle)
        owner = convertView!!.findViewById(R.id.txtOwner)
        date = convertView!!.findViewById(R.id.txtDate)
        img = convertView!!.findViewById(R.id.imgPlant)

        title.setText(articleList.get(position).title)
        owner.setText(articleList.get(position).owner)
        date.setText(articleList.get(position).date)
        Picasso.get().load(articleList.get(position).image).into(img)
        return convertView
    }
}
