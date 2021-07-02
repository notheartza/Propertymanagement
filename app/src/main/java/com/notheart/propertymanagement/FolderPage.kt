package com.notheart.propertymanagement

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MessageDAO1
import com.notheart.propertymanagement.API.MyService
import com.notheart.propertymanagement.Adapter.FolderAdapter
import com.notheart.propertymanagement.Adapter.announeAdapter
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.group.*
import kotlinx.android.synthetic.main.home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FolderPage : AppCompatActivity() {
    lateinit var ID_User: String
    lateinit var ID_Folder: String
    lateinit var ID_Group: String
    lateinit var NameG: String
    lateinit var Img: String
    lateinit var Property: List<MessageDAO.Announce>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)
        val bundle = intent.extras
        ID_User = bundle!!.getString("ID_User")!!
        ID_Folder = bundle.getString("ID_Folder")!!
        ID_Group = bundle.getString("ID_Group")!!
        NameG = bundle.getString("NameG")!!
        Img = bundle.getString("Img")!!


        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        inFolderView.layoutManager = layoutManager1
        callServerFolder(ID_Folder)

        Insert_PropertyInF.setOnClickListener {

            for (i in Property){
                if (List_Property.selectedItem.toString()==i.AnnounceTH){
                    callInsertProperty(ID_Folder,i.ID_Announce)
                }
            }

        }

    }

    fun  callAnnounce(ID: String){
        val call = MyService().getService().Announce(ID)
        call.enqueue(object :  Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                if (t.localizedMessage == "unexpected end of stream"){
                    callAnnounce(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
               if (response.isSuccessful){
                   getAnnounce(response.body()!!.Status(),response.body()!!.getAnnounce())
               }
            }

        })

    }

    fun getAnnounce(set: String, set1: List<MessageDAO.Announce>){
        this.Property = set1
        val array: Array<String?>  = arrayOfNulls(set1.size)
        for (i in set1.indices) {
            array[i] = set1[i].AnnounceTH
        }
       List_Property.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            array
        )

    }

    fun callServerFolder(ID: String){
        val call = MyService().getService().Folder(ID)
        call.enqueue(object : Callback<MessageDAO1>{
            override fun onFailure(call: Call<MessageDAO1>, t: Throwable) {
                if (t.localizedMessage == "unexpected end of stream"){
                    callServerFolder(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO1>, response: Response<MessageDAO1>) {
                if (response.isSuccessful){
                  getFolder( response.body()!!.getFolader())
                }
            }

        })
    }

    fun getFolder(Folder: List<MessageDAO1.Folder>){
        Folder_Folder.text = Folder[0].NameF
        Glide.with(this).load("http://www.landvist.xyz/images/$Img").listener(object :
            RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                ImgG.setImageResource(R.drawable.no_image)
                return true
            }
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(ImgG_Folder)
        NG_folder.text = NameG
        if(inFolderView.adapter!=null){
            inFolderView.adapter!!.notifyDataSetChanged()
        }
        if (Folder[0].Announce.isNotEmpty()){
            val adapter = announeAdapter(this, this.ID_User, Folder[0].Announce)
            inFolderView.adapter = adapter
        }

        callAnnounce(ID_User)

    }


    fun callInsertProperty(ID_F: String,ID_P: String){
        val day: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val call = MyService().getService().AddPropertyInFolder(ID_P,ID_F,day)
        call.enqueue(object : Callback<MessageDAO>{
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                if (t.localizedMessage == "unexpected end of stream"){
                    callServerFolder(ID_Folder)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful){
                    getPropertyInF(response.body()!!.Status())
                }
            }

        })


    }


    fun getPropertyInF(set :String){
        callServerFolder(ID_Folder)
        Toast.makeText(this, "เพิ่มสำเร็จ", Toast.LENGTH_LONG).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.Home_menu -> {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("ID_User", this.ID_User)
                startActivity(intent)
            }
            R.id.post_menu -> {
                val intent = Intent(this, Increase_Property::class.java)
                intent.putExtra("ID_User", this.ID_User)
                startActivity(intent)
            }
            R.id.profile_menu -> {
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("ID_User", this.ID_User)
                startActivity(intent)
            }
            R.id.myAnnounce_menu -> {
                val intent = Intent(this, MyProperty::class.java)
                intent.putExtra("ID_User", this.ID_User)
                startActivity(intent)
            }
            R.id.search_menu -> {
                val intent = Intent(this, AdvanceSerach::class.java)
                intent.putExtra("ID_User", this.ID_User)
                startActivity(intent)
            }R.id.recommend_menu->{
            val intent = Intent(this, RecommendPage::class.java)
            intent.putExtra("ID_User", this.ID_User)
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
