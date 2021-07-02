package com.notheart.propertymanagement

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import kotlinx.android.synthetic.main.increase_property.*
import kotlinx.android.synthetic.main.new_group.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NewGroup : AppCompatActivity() {



    private var imagePicker: ImagePicker? = null
    private var imageG: ImageView? = null
    private var select: Button? = null
    private var name: EditText? = null
    private var gokbutton: Button? = null
    private var file: File? = null
    private var groupImg: String? = null
    private var ID_Uesr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_group)
        val bundle = intent.extras
        ID_Uesr = bundle!!.getString("ID_User")
        val Type = bundle.getString("Type")

        Log.d("User", ID_Uesr)

        if (Type=="G"){
            New_Folder.visibility = View.GONE
            imagePicker = ImagePicker(this)
            imageG = findViewById(R.id.Image_Group_Select)
            select = findViewById(R.id.button19)
            name = findViewById(R.id.editText56)
            gokbutton = findViewById(R.id.button20)



            val needread= ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            val needwrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

            imagePicker!!.setImagePickerCallback(object : ImagePickerCallback {
                override fun onImagesChosen(list: List<ChosenImage>) {
                    val path: String = list[0].originalPath
                    file = File(path)

                    if (file!!.exists()) {
                        val myBitmap = BitmapFactory.decodeFile(file!!.absolutePath)
                        imageG!!.setImageBitmap(myBitmap)
                        imageG!!.visibility = View.VISIBLE

                    }

                }
                override fun onError(message: String) {
                    Log.d("upload",message)
                }
            })

            select!!.setOnClickListener {
                if (needwrite)
                    Register.request().requestwrite(this@NewGroup)
                @Synchronized
                if (needread)
                    Register.request().requestread(this@NewGroup)
                @Synchronized
                if (!needread&&!needwrite)
                    imagePicker!!.pickImage()
            }

            gokbutton!!.setOnClickListener {

                if(imageG!!.drawable.isVisible){
                    val img = RequestBody.create(MediaType.parse("image/*"), file as File)
                    callServerUploadImageProfile(img)
                }else{
                    val day: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    callServerNewGroup(name!!.text.toString(), "Defult/Group.png", day, ID_Uesr!!)
                }

            }
        }else{
            New_Group.visibility = View.GONE
            val ID_Group: String = bundle.getString("ID_Group")!!

            button8.setOnClickListener {
                if(editText7.text.isNotEmpty()){
                    callServerNewFolder(ID_Group,editText7.text.toString(),ID_Uesr!!)
                }else{
                    Toast.makeText(this,"กรุณาใส่ชื่อแฟ้ม",Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Picker.PICK_IMAGE_DEVICE&& Activity.RESULT_OK==resultCode){
            imagePicker!!.submit(data)
        }
    }

    fun callServerUploadImageProfile(img: RequestBody){
        val call= MyService().getService().updateImageProfile(img)
        call.enqueue(object : Callback<MessageDAO> {

            override fun onResponse(call: retrofit2.Call<MessageDAO>, response: retrofit2.Response<MessageDAO>) {
                if (response.isSuccessful) {
                    groupImg = response.body()!!.getImagepath()[0].Name_Photo
                    Toast.makeText(this@NewGroup, "Uploaded", Toast.LENGTH_SHORT).show()
                    val day: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                    callServerNewGroup(name!!.text.toString(), groupImg!!, day, ID_Uesr!!)
                }
            }

            override fun onFailure(call: retrofit2.Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                if (t.localizedMessage=="unexpected end of stream"){
                        callServerUploadImageProfile(img)
                }
            }

        })



    }

    fun callServerNewGroup(Name: String,Img: String, Created: String,ID: String){
        val call = MyService().getService().AddGroup(Name,Img,Created,ID)
        call.enqueue(object : Callback<MessageDAO> {

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@NewGroup, "Uploaded", Toast.LENGTH_SHORT).show()
                   if(response.body()!!.Status()=="success"){
                       getNewGroup()
                   }
                }
            }

            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream"){
                    getNewGroup()
                }
            }

        })
    }

    fun getNewGroup(){
        val intent = Intent(this@NewGroup, Home::class.java)
        this.finish()
        intent.putExtra("ID_User",ID_Uesr)
        ContextCompat.startActivity(this@NewGroup, intent, Bundle.EMPTY)
    }

    fun callServerNewFolder(ID_Group: String,NameF: String, ID: String){
        val call = MyService().getService().AddFolderInGroup(ID_Group,NameF,ID)
        call.enqueue(object : Callback<MessageDAO>{
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                if (t.localizedMessage == "unexpected end of stream"){
                    getNewFolder(ID_Group)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
               if (response.isSuccessful){
                   if (response.body()!!.Status()=="success"){
                       getNewFolder(ID_Group)
                   }
               }
            }

        })
    }

    fun getNewFolder(ID_Group: String){
        this.finish()
        val intent = Intent(this, GroupPage::class.java)
        intent.putExtra("ID_User",ID_Uesr)
        intent.putExtra("ID_Group",ID_Group)
        ContextCompat.startActivity(this, intent, Bundle.EMPTY)

    }

}
