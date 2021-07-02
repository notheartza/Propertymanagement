package com.notheart.propertymanagement.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.R

class ImageAdpter(context: Context, Picture: List<MessageDAO.Photo>) : PagerAdapter() {

    var picture: List<MessageDAO.Photo>? = null
    var pcontext: Context? = null

    init {
        this.picture = Picture
        this.pcontext = context
    }


    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return picture!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(pcontext)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(pcontext!!).load("http://www.landvist.xyz/images/"+picture!![position].URL).listener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                imageView.setImageResource(R.drawable.no_image)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(imageView)

        container.addView(imageView,0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, objects: Any) {
        container.removeView(objects as ImageView)
    }
}