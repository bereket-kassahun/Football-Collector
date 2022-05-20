package com.collect22allfootball.ui.collection

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.collect22allfootball.R
import java.io.IOException
import java.io.InputStream


class GridViewAdapter(val context: Context, val level: Int) : BaseAdapter() {
    override fun getCount(): Int {
        return 12
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View? {

        if (view == null) {

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val ret = inflater.inflate(R.layout.collection_item, null, false)

            val imageView: ImageView? = ret?.findViewById(R.id.img)
            val questionMark: TextView? = ret?.findViewById(R.id.question_mark)
            if(i < level-1){
                try {
                    val ims: InputStream = context.assets.open("${i+1}.png")
                    val d = Drawable.createFromStream(ims, null)
                    imageView?.setImageResource(0)
                    imageView?.setImageDrawable(d)
                    questionMark?.visibility = View.INVISIBLE
                } catch (ex: IOException) {

                }
            }else{
                imageView?.setImageResource(R.drawable.blank)
                questionMark?.visibility = View.VISIBLE
            }
            return ret
        }else{
            val imageView: ImageView? = view.findViewById(R.id.img)
            val questionMark: TextView? = view.findViewById(R.id.question_mark)
            if(i < level-1){
                try {
                    val ims: InputStream = context.assets.open("${i+1}.png")
                    val d = Drawable.createFromStream(ims, null)
                    imageView?.setImageResource(0)
                    imageView?.setImageDrawable(d)
                    questionMark?.visibility = View.INVISIBLE
                } catch (ex: IOException) {

                }
            }else{
                imageView?.setImageResource(R.drawable.blank)
                questionMark?.visibility = View.VISIBLE
            }

            return view
        }


    }
}