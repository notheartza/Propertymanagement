package com.notheart.propertymanagement.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.R
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.Property_Info
import java.text.DecimalFormat


class announeAdapter(context: Context,ID_User: String, Announce: List<MessageDAO.Announce>?): RecyclerView.Adapter<announeAdapter.ViewHolder>() {

    val formatter = DecimalFormat("#,###")
    var annnoune: List<MessageDAO.Announce>? = null
    var acontext: Context? = null
    var id_user: String? = null

    init {
        this.annnoune = Announce
        this.acontext = context
        this.id_user = ID_User
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.announeview, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  annnoune!!.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.price!!.text = formatter.format(annnoune!![p1].MarketPrice.toDouble())
        p0.header!!.text = annnoune!![p1].AnnounceTH
        p0.status!!.text = annnoune!![p1].PPStatus

        if(!annnoune!![p1].Photo.isNullOrEmpty())
        Glide.with(acontext!!).load("http://www.landvist.xyz/images/"+annnoune!![p1].Photo[0].URL).listener(object :RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                p0.imageannounce!!.setImageResource(R.drawable.no_image)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
               return false
            }
        }).into(p0.imageannounce!!)

        p0.imageannounce!!.setOnClickListener{
            val intent = Intent(acontext, Property_Info::class.java)
            intent.putExtra("ID_Announce", annnoune!![p1].ID_Announce)
            intent.putExtra("ID_User", id_user)
            startActivity(acontext!!, intent, Bundle.EMPTY)
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageannounce: ImageView? = null
        var header: TextView? = null
        var price: TextView? = null
        var status: TextView? = null

        init {
            imageannounce = itemView.findViewById(R.id.imageannounce)
            header = itemView.findViewById(R.id.textView64)
            price = itemView.findViewById(R.id.textView66)
            status = itemView.findViewById(R.id.textView11)
        }
    }
}