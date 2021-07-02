package com.notheart.propertymanagement

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import kotlinx.android.synthetic.main.login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.*


class Login : AppCompatActivity() {



    var NetworkBroadcast: BroadcastReceiver? = null
    private var signup: Button? = null
    private var login: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    var account: String? = null
    var progressDialog: ProgressDialog? = null
    val t = Timer()
    var user: List<MessageDAO.User>? = null



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        progressDialog = ProgressDialog(this@Login)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Downloading...")

        NetworkBroadcast = NetworkChangeReceiver()
        registerNetworkBroadcastForNougat()

        Log.d("Get",user.toString())



        Log.d("JSON", "Welcome")
        signup = findViewById(R.id.button4)
        login = findViewById(R.id.button3)
        email = findViewById(R.id.editText5)
        password = findViewById(R.id.editText6)

        email!!.setText("tony@hotmail.com")
        password!!.setText("666666")

        textView64.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        refresh.setOnRefreshListener {
            recreate()
        }

        login!!.setOnClickListener {


            if (email!!.text.isEmpty() || password!!.text.isEmpty()) {
                val alertdialog: Dialog? = Dialog(this)
                alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                alertdialog.setContentView(R.layout.alertsignup)
                alertdialog.setCancelable(false)

                val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
                messagealert.text = "กรุณาใส่รหัสผ่านหรืออีเมลให้ถูกต้อง"
                val okbutton = alertdialog.findViewById<Button>(R.id.button6)
                okbutton!!.setOnClickListener {
                    alertdialog.cancel()
                }
                alertdialog.show()
            } else {
                callServerLogin(email!!.text.toString(),password!!.text.toString())

            }
        }


        signup!!.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        refresh.setOnRefreshListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            refresh.isRefreshing = false
        }

    }



    @SuppressLint("ObsoleteSdkInt")
    fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(NetworkBroadcast, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(NetworkBroadcast, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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
                    Toast.makeText(context, "Please check your internet and try again", Toast.LENGTH_LONG).show()
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



    fun callServerLogin(email: String,password: String){
        val call = MyService().getService().checkLogin(email,password)
        this.progressDialog!!.isShowing
        call.enqueue(object : Callback<MessageDAO>{
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                if(t.localizedMessage=="unexpected end of stream"){
                    callServerLogin(email,password)
                }
                this@Login.progressDialog!!.cancel()
            }
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful){
                    CheckAccess(response.body()!!.Status(),response.body()!!.getLogin())
                    //Log.d("Error", response.body()!!.getError().toString())
                    Toast.makeText(this@Login, "Downloaded", Toast.LENGTH_SHORT).show()

                }
                this@Login.progressDialog!!.cancel()
            }

        })
    }



    fun CheckAccess(status: String,set: List<MessageDAO.User>){
        Log.d("Login", set[0].toString())
        if (status == "failed") {
            val alertdialog: Dialog? = Dialog(this@Login)
            alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertdialog.setContentView(R.layout.alertsignup)
            alertdialog.setCancelable(false)
            val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
            messagealert.text = "กรุณาใส่รหัสผ่านหรืออีเมลให้ถูกต้อง"
            val okbutton = alertdialog.findViewById<Button>(R.id.button6)
            okbutton!!.setOnClickListener {
                alertdialog.cancel()
            }
            alertdialog.show()
        }else{
            account = set[0].ID_User
            Log.d("Login", "Login finish")
            val intent = Intent(this, Home::class.java)
            intent.putExtra("ID_User", account)
            startActivity(intent)
        }
    }


}




