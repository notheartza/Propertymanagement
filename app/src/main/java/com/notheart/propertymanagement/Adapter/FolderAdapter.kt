package com.notheart.propertymanagement.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.FolderPage
import com.notheart.propertymanagement.GroupPage
import com.notheart.propertymanagement.NewGroup
import com.notheart.propertymanagement.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.alertsignup.view.*

class FolderAdapter(ID_Group: String, NameG: String, Img: String,Folder: List<MessageDAO.Folder>?, context: Context, id_user: String): RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    val TAG = "RecyclerViewAdapter"

    var Folder : List<MessageDAO.Folder>? = null
    var rcontext: Context? = null
    var ID_Uesr: String? = null
    var ID_Group: String? = null
    var NameG: String? = null
    var Img: String? = null

    init {
        this.ID_Group = ID_Group
        this.Folder = Folder
        this.rcontext = context
        this.ID_Uesr = id_user
        this.NameG = NameG
        this.Img = Img
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder: celled")

        val view = LayoutInflater.from(p0.context).inflate(R.layout.folderview, p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Folder!!.size+1
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            if (p1==0){
                p0.imageitem!!.setImageResource(R.drawable.new_folder_group)
                p0.nameitem!!.text = "สร้างแฟ้มใหม่"
            }else{
                p0.imageitem!!.setImageResource(R.drawable.folder_group)
                p0.nameitem!!.text = Folder!![p1-1].NameF
            }



        p0.Card!!.setOnClickListener {
            if(p0.nameitem!!.text=="สร้างแฟ้มใหม่"){
                val intent = Intent(rcontext, NewGroup::class.java)
                intent.putExtra("ID_User",ID_Uesr)
                intent.putExtra("ID_Group", ID_Group)
                intent.putExtra("Type", "F")
                ContextCompat.startActivity(rcontext!!, intent, Bundle.EMPTY)
                Log.d("Group", "สร้างใหม่")

            }
            else{
                val intent = Intent(rcontext, FolderPage::class.java)
                intent.putExtra("ID_User",ID_Uesr)
                intent.putExtra("ID_Folder",Folder!![p1-1].ID_Folder)
                intent.putExtra("ID_Group",Folder!![p1-1].ID_Group)
                intent.putExtra("NameG", NameG)
                intent.putExtra("Img", Img)
                ContextCompat.startActivity(rcontext!!, intent, Bundle.EMPTY)
            }
        }
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageitem: CircleImageView? = null
        var nameitem: TextView? = null
        var Card: CardView? = null

        init {
            Card = itemView.findViewById(R.id.Group_Folder_Card)
            imageitem = itemView.findViewById(R.id.folder_img)
            nameitem = itemView.findViewById(R.id.folder_name)
        }
    }

}
