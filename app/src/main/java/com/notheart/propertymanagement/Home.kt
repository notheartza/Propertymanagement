package com.notheart.propertymanagement

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast


import com.notheart.propertymanagement.Adapter.GroupViewAdapter
import com.notheart.propertymanagement.Adapter.announeAdapter
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import kotlinx.android.synthetic.main.home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList


class Home : AppCompatActivity() {


    var NetworkBroadcast: BroadcastReceiver? = null
    var ID_User: String? = null
    var Add_butt: Button? = null
    var post: ImageView? = null
    var progressDialog: ProgressDialog? = null
    val t = Timer()


    lateinit var Group_item: List<MessageDAO.Group>
    lateinit var Announce_item: List<MessageDAO.Announce>
    lateinit var Status: String
    var Ugroup: ArrayList<String>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        val bundle = intent.extras
        ID_User = bundle!!.getString("ID_User")
        Add_butt = findViewById(R.id.button7)

        progressDialog = ProgressDialog(this@Home)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Downloading...")
        NetworkBroadcast = NetworkChangeReceiver()
        registerNetworkBroadcastForNougat()


        callServerAnnounce(ID_User!!)

        val layoutManager = LinearLayoutManager(this@Home, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager


        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        annouelistview.layoutManager = layoutManager1



        Add_butt!!.setOnClickListener {
            val intent = Intent(this, Increase_Property::class.java)
            intent.putExtra("ID_User", ID_User)
            startActivity(intent)
        }


        Scroll_Home.viewTreeObserver.addOnScrollChangedListener {
            Log.d("Scroll", Scroll_Home.canScrollVertically(-1).toString())
            if (!Scroll_Home.canScrollVertically(1)) {
                // Bottom of scroll view.
                refresh_Home.isEnabled = false
            }
            if (!Scroll_Home.canScrollVertically(-1)) {

                refresh_Home.isEnabled = false
                // Top of scroll view.
            }
        }





        refresh_Home.setOnRefreshListener {
            recreate()
        }


        Search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                callSeverSearch(ID_User!!, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("beforeSearch,", s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Search,", s.toString())
            }
        })

    }


    fun callServerAnnounce(ID: String) {
        val call = MyService().getService().Announce(ID)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerAnnounce(ID)
                } else {
                    this@Home.progressDialog!!.cancel()
                }


            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getAnnounce(response.body()!!.Status(), response.body()!!.getAnnounce())
                    Log.d("Announce", response.body()!!.getAnnounce().toString())
                    Toast.makeText(this@Home, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                this@Home.progressDialog!!.cancel()
            }

        })
    }

    fun callServerAllGroup(ID: String) {
        val call = MyService().getService().AllGroup(ID)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)

                if (t.localizedMessage == "unexpected end of stream") {
                    callServerAllGroup(ID)
                } else {
                    this@Home.progressDialog!!.cancel()
                }

            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getGroup(response.body()!!.Status(), response.body()!!.getGroup())
                    //Log.d("Announce", response.body()!!.getAnnounce().toString())
                    Toast.makeText(this@Home, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                this@Home.progressDialog!!.cancel()
            }

        })
    }

    fun callSeverSearch(ID: String, Word: String) {
        val call = MyService().getService().Search(ID, Word)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callSeverSearch(ID, Word)
                } else {
                    this@Home.progressDialog!!.cancel()
                }


            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getSearch(response.body()!!.Status(), response.body()!!.getSearch())
                    Log.d("Announce", response.body()!!.getSearch().toString())
                    Toast.makeText(this@Home, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                this@Home.progressDialog!!.cancel()
            }

        })
    }


    fun getAnnounce(set: String, set1: List<MessageDAO.Announce>) {
        this.Announce_item = set1
        this.Status = set
        if (Status == "success") {
            val announeAdapter = announeAdapter(this, ID_User!!, Announce_item)
            annouelistview.adapter = announeAdapter
        }
        callServerAllGroup(ID_User!!)
    }

    fun getGroup(set: String, set1: List<MessageDAO.Group>) {
            Log.d("Group", set1.toString())
            this.Status = set
            this.Group_item = set1
        if(recyclerView.adapter!=null){
            recyclerView.adapter!!.notifyDataSetChanged()
        }
        val adapter = GroupViewAdapter(set1, this@Home, ID_User!!)
        recyclerView.adapter = adapter
    }

    fun getSearch(set: String, set1: List<MessageDAO.Announce>) {
        this.Announce_item = set1
        this.Status = set
        if (Status == "success") {
            val announeAdapter = announeAdapter(this, ID_User!!, Announce_item)
            annouelistview.adapter = announeAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.Home_menu -> {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }
            R.id.post_menu -> {
                val intent = Intent(this, Increase_Property::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }
            R.id.profile_menu -> {
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }
            R.id.myAnnounce_menu -> {
                val intent = Intent(this, MyProperty::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }
            R.id.search_menu -> {
                val intent = Intent(this, AdvanceSerach::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }R.id.recommend_menu->{
                val intent = Intent(this, RecommendPage::class.java)
                intent.putExtra("ID_User", ID_User)
                startActivity(intent)
            }
            R.id.logout_menu -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }


        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                NetworkBroadcast,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                NetworkBroadcast,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(NetworkBroadcast)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }

    class NetworkChangeReceiver : BroadcastReceiver() {
        var connection: Boolean? = null
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                connection = isOnline(context)
                Log.d("Connect", connection.toString())
                if (!connection!!) {
                    Toast.makeText(
                        context,
                        "Please check your internet and try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }

        fun isOnline(context: Context?): Boolean {
            return try {
                val connectivityManager =
                    context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo

                (networkInfo != null && networkInfo.isConnected)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }
        }
    }


}
