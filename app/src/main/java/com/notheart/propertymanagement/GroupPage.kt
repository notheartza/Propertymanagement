package com.notheart.propertymanagement

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import com.notheart.propertymanagement.Adapter.FolderAdapter
import com.notheart.propertymanagement.Adapter.GroupViewAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.folderview.*
import kotlinx.android.synthetic.main.group.*
import kotlinx.android.synthetic.main.home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GroupPage : AppCompatActivity() {
    var ID_Uesr: String? = null
    var ID_Group: String? = null
    var progressDialog: ProgressDialog? = null
    var Status: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group)
        val bundle = intent.extras
        ID_Uesr = bundle!!.getString("ID_User")
        ID_Group = bundle.getString("ID_Group")
        progressDialog = ProgressDialog(this@GroupPage)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Downloading...")

        Log.d("Group", ID_Group)


        callServerGroup(ID_Group!!)

        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        folderView.layoutManager = layoutManager1


        refresh_Group.setOnRefreshListener {
            recreate()
        }



    }

    fun callServerGroup(ID: String){
        val call = MyService().getService().Group(ID)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {

                if (t.localizedMessage == "unexpected end of stream") {
                    callServerGroup(ID)
                } else {
                    this@GroupPage.progressDialog!!.cancel()
                }
                progressDialog!!.cancel()
            }
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful){
                    getGroup(response.body()!!.Status(),response.body()!!.getGroup())
                    //Log.d("Announce", response.body()!!.getAnnounce().toString())
                    Toast.makeText(this@GroupPage, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                progressDialog!!.cancel()
            }

        })
    }

    fun getGroup(set: String,set1: List<MessageDAO.Group>){
        val Group_item = set1 as MutableList<MessageDAO.Group>
        this.Status = set
        //Log.d("Group",folder_name.text.toString())
        NameG.text = Group_item[0].Name

        Glide.with(this).load("http://www.landvist.xyz/images/"+Group_item[0].Img).listener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                ImgG.setImageResource(R.drawable.no_image)
                return true
            }
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(ImgG)


        if(folderView.adapter!=null){
            folderView.adapter!!.notifyDataSetChanged()

        }
        val adapter = FolderAdapter(Group_item[0].ID_Group, Group_item[0].Name, Group_item[0].Img, Group_item[0].Folders, this, this.ID_Uesr!!)
        folderView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.Home_menu -> {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("ID_User", this.ID_Uesr)
                startActivity(intent)
            }
            R.id.post_menu -> {
                val intent = Intent(this, Increase_Property::class.java)
                intent.putExtra("ID_User", this.ID_Uesr)
                startActivity(intent)
            }
            R.id.profile_menu -> {
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("ID_User", this.ID_Uesr)
                startActivity(intent)
            }
            R.id.myAnnounce_menu -> {
                val intent = Intent(this, MyProperty::class.java)
                intent.putExtra("ID_User", this.ID_Uesr)
                startActivity(intent)
            }
            R.id.search_menu -> {
                val intent = Intent(this, AdvanceSerach::class.java)
                intent.putExtra("ID_User", this.ID_Uesr)
                startActivity(intent)
            }R.id.recommend_menu->{
            val intent = Intent(this, RecommendPage::class.java)
            intent.putExtra("ID_User", this.ID_Uesr)
            startActivity(intent)
        }
            R.id.logout_menu -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }


        return super.onOptionsItemSelected(item)
    }


}
