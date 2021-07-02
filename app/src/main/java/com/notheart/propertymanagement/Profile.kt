package com.notheart.propertymanagement

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import kotlinx.android.synthetic.main.profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*


class Profile : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    val t = Timer()
    var account: MessageDAO.User? = null
    lateinit var ID_User : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        //var editButton = findViewById<Button>(R.id.button9)
        progressDialog = ProgressDialog(this@Profile)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Downloading...")




        val bundle = intent.extras
         ID_User = bundle!!.getString("ID_User")!!

        callServerLogin(ID_User)


    }
    fun callServerLogin(id: String){
        val call = MyService().getService().FindUser(id)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                progressDialog!!.cancel()
            }
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful){
                    CheckAccess(response.body()!!.Status(),response.body()!!.getLogin())
                    //Log.d("Error", response.body()!!.getError().toString())
                    Toast.makeText(this@Profile, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                progressDialog!!.cancel()
            }

        })
    }

    fun CheckAccess(status: String,set: List<MessageDAO.User>){
        this.account = set[0]
        Glide.with(this).load("http://landvist.xyz/images/"+account!!.ProfileImg).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(imageView2)




        val name = account!!.Firstname+" "+account!!.Lastname

        textView22.text = name
        textView24.text = account!!.Birthday
        textView26.text = account!!.LocationU
        textView28.text = account!!.Email
        textView30.text = account!!.Phone


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

