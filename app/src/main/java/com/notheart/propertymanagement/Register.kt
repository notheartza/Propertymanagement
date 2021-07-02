package com.notheart.propertymanagement

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import okhttp3.MediaType
import okhttp3.RequestBody
import org.mindrot.jbcrypt.BCrypt
import org.mindrot.jbcrypt.BCrypt.gensalt
import retrofit2.Callback
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class Register : AppCompatActivity() {


    class request {
        @Synchronized
        fun requestread(context: Context) {

            Log.d("Upload","requestPermissions=>read")
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)

        }
        @Synchronized
        fun requestwrite(context: Context){
            Log.d("Upload","requestPermissions=>write")
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)

        }
    }



    private var backbutton: Button? = null
    private var confirm: Button? = null
    private var cannel: Button? = null
    private var email: EditText? = null
    private var name: EditText? = null
    private var password: EditText? = null
    private var conpassword: EditText? = null
    private var lastname: EditText? = null
    private var birthday: EditText? = null
    private var location: EditText? = null
    private var phone: EditText? = null
    private var profile: String? = null
    private var selectedImage: Button? = null
    private var imagePicker: ImagePicker? = null
    private var image: ImageView? = null
    private var file: File? = null
    private lateinit var Status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        backbutton = findViewById(R.id.button5)
        selectedImage = findViewById(R.id.button10)
        confirm = findViewById(R.id.button2)
        cannel = findViewById(R.id.button)
        email = findViewById(R.id.editText2)
        name = findViewById(R.id.editText)
        lastname = findViewById(R.id.editText8)
        password = findViewById(R.id.editText3)
        conpassword = findViewById(R.id.editText4)
        birthday = findViewById(R.id.editText9)
        location = findViewById(R.id.editText10)
        phone = findViewById(R.id.editText11)
        image = findViewById(R.id.imageView4)

        imagePicker = ImagePicker(this)

        val needread= ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        val needwrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED


        imagePicker!!.setImagePickerCallback(object : ImagePickerCallback {
            override fun onImagesChosen(list: List<ChosenImage>) {
                val path: String = list[0].originalPath
                file = File(path)

                if (file!!.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(file!!.absolutePath)
                    image!!.setImageBitmap(myBitmap)
                    image!!.visibility = View.VISIBLE
                }

            }

            override fun onError(message: String) {
                Log.d("upload",message)
            }
        })

        selectedImage!!.setOnClickListener {
            if (needwrite)
                request().requestwrite(this@Register)
            @Synchronized
            if (needread)
                request().requestread(this@Register)
            @Synchronized
            if (!needread&&!needwrite)
                imagePicker!!.pickImage()
        }


        confirm!!.setOnClickListener {
            Log.d("signup", "confirm" + "\n" + email!!.text)

            if (email!!.text.isEmpty() || name!!.text.isEmpty() || password!!.text.isEmpty() || conpassword!!.text.isEmpty()) {
                Log.d("signup", "null")
                val alertdialog: Dialog? = Dialog(this)
                alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                //alertdialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertdialog.setContentView(R.layout.alertsignup)
                alertdialog.setCancelable(false)

                val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
                messagealert.text = "กรุณากรอกข้อมูลให้ครบก่อน"
                val okbutton = alertdialog.findViewById<Button>(R.id.button6)
                okbutton!!.setOnClickListener {
                    alertdialog.cancel()
                }
                alertdialog.show()

            } else {

                Log.d("signup", (password!!.text).toString() + ":" + conpassword!!.text.toString())

                if (password!!.text.toString() != conpassword!!.text.toString()) {
                    val alertdialog: Dialog? = Dialog(this)
                    alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    //alertdialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertdialog.setContentView(R.layout.alertsignup)
                    alertdialog.setCancelable(false)

                    val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
                    messagealert.text = "กรุณาใส่รหัสผ่านให้ถูกต้อง"
                    val okbutton = alertdialog.findViewById<Button>(R.id.button6)
                    okbutton!!.setOnClickListener {
                        alertdialog.cancel()
                    }
                    alertdialog.show()
                } else {

                    val img = RequestBody.create(MediaType.parse("image/*"), file as File)
                    callServerUploadImageProfile(img)

                    /*if (profile!=null) {
                        val alertdialog: Dialog? = Dialog(this@Register)
                        alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        alertdialog.setContentView(R.layout.alertsignup)
                        alertdialog.setCancelable(false)

                        val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
                        messagealert.text = "สมัครเสร็จสิ้น"
                        val okbutton = alertdialog.findViewById<Button>(R.id.button6)
                        okbutton!!.setOnClickListener {
                            alertdialog.cancel()

                            val intent = Intent(this@Register, Login::class.java)
                            startActivity(intent)
                        }
                        alertdialog.show()

                    }else{
                        Toast.makeText(this@Register, "กรุณารอรูปประมวลผลให้เสร็จก่อน", Toast.LENGTH_LONG).show()
                    }*/
                }


            }
        }



        backbutton!!.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Picker.PICK_IMAGE_DEVICE&& Activity.RESULT_OK==resultCode){
            imagePicker!!.submit(data)
        }
    }

    fun callServerUploadImageProfile(img: RequestBody){
        val call= MyService().getService().updateImageProfile(img)
        call.enqueue(object : Callback<MessageDAO> {

            override fun onResponse(call: retrofit2.Call<MessageDAO>, response: retrofit2.Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Register, "Uploaded", Toast.LENGTH_SHORT).show()
                    getUpload(response.body()!!.Status(),response.body()!!.getImagepath())

                }
            }

            override fun onFailure(call: retrofit2.Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
            }

        })



    }

    fun callServerSingup(email: String,Name: String,Surname: String, Password: String, birthday: String, location: String, phone: String, profile: String){
        val day: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        val call = MyService().getService().Singup(email,Name,Surname,Password,birthday,location,phone,profile,day)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onResponse(call: retrofit2.Call<MessageDAO>, response: retrofit2.Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Register, "Uploaded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
            }

        })
    }

    fun getUpload(set: String,set1: List<MessageDAO.Upload_path>){
        if (set=="failed"){
            callServerSingup(
                email!!.text.toString(), name!!.text.toString(), lastname!!.text.toString(),
                BCrypt.hashpw(password!!.text.toString(), gensalt(10)), birthday!!.text.toString(), location!!.text.toString(),
                phone!!.text.toString(),"null")
        }else{
            callServerSingup(
                email!!.text.toString(), name!!.text.toString(), lastname!!.text.toString(),
                BCrypt.hashpw(password!!.text.toString(), gensalt(10)), birthday!!.text.toString(), location!!.text.toString(),
                phone!!.text.toString(),set1[0].imagePath)
        }


        val alertdialog: Dialog? = Dialog(this@Register)
        alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertdialog.setContentView(R.layout.alertsignup)
        alertdialog.setCancelable(false)

        val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
        messagealert.text = "สมัครเสร็จสิ้น"
        val okbutton = alertdialog.findViewById<Button>(R.id.button6)
        okbutton!!.setOnClickListener {
            alertdialog.cancel()

            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
        alertdialog.show()
    }



}
