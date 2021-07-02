package com.notheart.propertymanagement

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import com.notheart.propertymanagement.Adapter.announeAdapter
import kotlinx.android.synthetic.main.activity_my_property.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProperty : AppCompatActivity() {
    lateinit var ID_User: String
    lateinit var Announce_item: List<MessageDAO.Announce>
    lateinit var Status: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_property)
        val bundle = intent.extras
        ID_User = bundle!!.getString("ID_User")!!

        if (intent!=null && intent.extras!=null){
            val b = intent.extras
        if (b!!.getStringArray("Search") != null){
            val Search = b.getStringArray("Search")
            callServerAdvanceSearch(ID_User,Search!![0],Search[1],Search[2],Search[3],Search[4])
        }else{
            callServerAnnounce(ID_User)
        }
        }

        AdvanceSearch.setOnClickListener {
            val intent = Intent(this, AdvanceSerach::class.java)
            intent.putExtra("ID_User", ID_User)
            startActivity(intent)
        }


    }

    fun callServerAnnounce(ID: String){
        val call = MyService().getService().Announce(ID)

        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                if(t.localizedMessage=="unexpected end of stream"){
                    callServerAnnounce(ID)
                }


            }
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful){
                    getMyAnnounce(response.body()!!.Status(),response.body()!!.getAnnounce())
                    //Toast.makeText(this@MyProperty, "Downloaded", Toast.LENGTH_SHORT).show()
                }

            }

        })
    }

    fun getMyAnnounce(set: String,set1: List<MessageDAO.Announce>){
        this.Announce_item = set1
        this.Status = set
        Log.d("MyAnnounce", "$Status:$Announce_item")
        val view = LinearLayoutManager(this)
        view.orientation = LinearLayoutManager.VERTICAL
            val myannouneAdapter = announeAdapter(this,ID_User, set1)
            myannouelistview.layoutManager = view
            myannouelistview.adapter = myannouneAdapter

    }

    fun callServerAdvanceSearch(ID: String,PPStatus: String,Type: String,Word: String,MinPrice: String, MaxPrice: String) {
        Log.d("Adfilter", "${ID}, ${PPStatus}, ${Type}, ${Word}, ${MinPrice}, ${MaxPrice}")
        val call = MyService().getService().AdvenceSearch(ID,PPStatus,Type, Word, MinPrice, MaxPrice)

        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerAdvanceSearch(ID,PPStatus,Type, Word, MinPrice, MaxPrice)
                }


            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getAnnounce(response.body()!!.Status(), response.body()!!.getSearch())
                    Log.d("Announce", response.body()!!.getSearch().toString())
                    //Toast.makeText(this@MyProperty, "Downloaded", Toast.LENGTH_SHORT).show()

                }

            }

        })
    }

    fun getAnnounce(set: String,set1: List<MessageDAO.Announce>){
        this.Announce_item = set1
        this.Status = set
        if (Status=="success"){
            val view = LinearLayoutManager(this)
            view.orientation = LinearLayoutManager.VERTICAL
            val myannouneAdapter = announeAdapter(this,ID_User, set1)
            myannouelistview.layoutManager = view
            myannouelistview.adapter = myannouneAdapter
        }else{
            Toast.makeText(this, "ไม่พบสิ่งที่คุณค้นหากรุณาลองใหม่", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu ,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
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
