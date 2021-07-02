package com.notheart.propertymanagement.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kbeanie.multipicker.api.ImagePicker
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.Data
import com.notheart.propertymanagement.GroupPage
import com.notheart.propertymanagement.NewGroup
import com.notheart.propertymanagement.R
import de.hdodenhof.circleimageview.CircleImageView



class GroupViewAdapter(Group: List<MessageDAO.Group>?, context: Context ,id_user: String) : RecyclerView.Adapter<GroupViewAdapter.ViewHolder>() {


    val TAG = "RecyclerViewAdapter"

    var Group : List<MessageDAO.Group>? = null
    var rcontext: Context? = null
    var ID_Uesr: String? = null
    var Count: Int? = null

    init {
        this.Group = Group
        this.rcontext = context
        this.ID_Uesr = id_user
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder: celled")

        val view = LayoutInflater.from(p0.context).inflate(R.layout.groupitem, p0,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        Log.d("Group", Group.toString())
        Log.d("Group_Size",Group!!.size.toString())
        return if (Group!![0].ID_Group.isNullOrEmpty()&&Group!!.size==1){
            Log.d("Size","0")
            1
        }else{
            Log.d("Size", (Group!!.size+1).toString())
            Group!!.size+1
        }


    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Log.d("Size",p1.toString())
        if(!Group!![0].ID_Group.isNullOrEmpty()&&p1<Group!!.size){
            Glide.with(rcontext!!)
                .load("http://www.landvist.xyz/images/"+Group!![p1].Img).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        p0.imageitem!!.setImageResource(R.drawable.no_image)
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                })
                .into(p0.imageitem!!)

            p0.nameitem!!.text = Group!![p1].Name


        }else{
            Glide.with(rcontext!!)
                .load("http://www.landvist.xyz/images/img_5cdd23cb1c48c.png").listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        p0.imageitem!!.setImageResource(R.drawable.no_image)
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                })
                .into(p0.imageitem!!)
            p0.nameitem!!.text = "สร้างใหม่"
        }



        p0.imageitem!!.setOnClickListener {

            if(p0.nameitem!!.text=="สร้างใหม่"){
                val intent = Intent(rcontext, NewGroup::class.java)
                intent.putExtra("ID_User",ID_Uesr)
                intent.putExtra("Type","G")
                startActivity(rcontext!!,intent, Bundle.EMPTY)

            }
            else{
                val intent = Intent(rcontext, GroupPage::class.java)
                intent.putExtra("ID_User",ID_Uesr)
                intent.putExtra("ID_Group",Group!![p1].ID_Group)
                startActivity(rcontext!!,intent, Bundle.EMPTY)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageitem: CircleImageView? = null
        var nameitem: TextView? = null

        init {
            imageitem = itemView.findViewById(R.id.imageGroup)
            nameitem = itemView.findViewById(R.id.nameGroup)
        }


    }

}