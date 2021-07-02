package com.notheart.propertymanagement


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.*
import android.widget.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import kotlinx.android.synthetic.main.increase_property.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.AppCompatRadioButton
import android.text.Editable
import android.text.TextWatcher
import com.google.android.gms.location.*
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File




class Increase_Property : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback,
    AppCompatActivity() {


    var Latitude: Double = 0.00
    var Longitude: Double = 0.00
    lateinit var Location: LatLng

    lateinit var ID_User: String
    lateinit var Deeded: String
    lateinit var Groundleveled: String
    lateinit var RoadType: String
    lateinit var Province: List<MessageDAO.Province>
    lateinit var Amphur: List<MessageDAO.Amphur>
    lateinit var District: List<MessageDAO.District>
    lateinit var Zipcode: List<MessageDAO.Zipcode>
    lateinit var ContactS: List<MessageDAO.Contact>
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    lateinit var mMap: GoogleMap
    private var imagePicker: ImagePicker? = null
    private var file: File? = null
    lateinit var ID_Announce: String
    lateinit var getPricePM: String
    lateinit var getCostestimate: String
    lateinit var getSellprice: String
    lateinit var getCostestmateB: String
    lateinit var getMarketprice: String

    class request {
        @Synchronized
        fun requestread(context: Context) {
            Log.d("Upload", "requestPermissions=>read")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )

        }

        @Synchronized
        fun requestwrite(context: Context) {
            Log.d("Upload", "requestPermissions=>write")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1000
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Picker.PICK_IMAGE_DEVICE && Activity.RESULT_OK == resultCode) {
            imagePicker!!.submit(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.increase_property)
        val bundle = intent.extras!!
        ID_User = bundle.getString("ID_User")!!
        imagePicker = ImagePicker(this)


        val needread = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
        val needwrite = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED

        imagePicker!!.setImagePickerCallback(object : ImagePickerCallback {
            override fun onImagesChosen(list: List<ChosenImage>) {
                val path: String = list[0].originalPath
                file = File(path)

                if (file!!.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(file!!.absolutePath)
                    image_Selected!!.setImageBitmap(myBitmap)
                    image_Selected!!.visibility = View.VISIBLE
                }

            }

            override fun onError(message: String) {
                Log.d("upload", message)
            }
        })

        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()


        val mapFragment = MapFragment.newInstance()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_map_container, mapFragment)
        fragmentTransaction.commit()
        mapFragment.getMapAsync(this)


        Select_Image.setOnClickListener {
            if (needwrite)
                request().requestwrite(this)
            @Synchronized
            if (needread)
                request().requestread(this)
            @Synchronized
            if (!needread && !needwrite)
                imagePicker!!.pickImage()
        }




        pricePM.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                getPricePM = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (getPricePM != s.toString() && s.toString().isNotEmpty()) {
                    val money = s.toString().toMoney()
//                    if (s.toString() != money) {
//                        pricePM.setText(money)
//                        pricePM.setSelection(money.length)
//                    }
                }

            }

        })



        callServerProvince()

        val year = (2510..2562).toList().toTypedArray()


        val fYear: ArrayAdapter<Int> =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, year)

        buildFY.adapter = fYear
        buildFM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val day = when {
                    buildFM.getItemAtPosition(position).toString().contains("คม") -> Array(31) { i: Int -> (i + 1) * 1 }
                    buildFM.getItemAtPosition(position) == "กุมภาพันธ์" -> Array(29) { i: Int -> (i + 1) * 1 }
                    else -> Array(30) { i: Int -> (i + 1) * 1 }
                }
                val fday: ArrayAdapter<Int> = ArrayAdapter(
                    this@Increase_Property,
                    android.R.layout.simple_dropdown_item_1line,
                    day
                )
                buildFD.adapter = fday
            }


        }






        typedase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                pricePM.visibility = View.VISIBLE
                facilities.visibility = View.GONE
                facilitiesCondo.visibility = View.GONE
                facilitiesproperty.visibility = View.GONE
            }


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when {
                    typedase.getItemAtPosition(position) == "บ้าน" -> {
                        deedtext6.visibility = View.GONE
                        typeland.visibility = View.GONE
                        codedeed.visibility = View.VISIBLE
                        pricePM.visibility = View.GONE
                        costestimate.visibility = View.GONE
                        descriptionPropertys.visibility = View.VISIBLE
                        distanceUse.hint = "พื้นที่ตัวบ้าน (ตร.ม.)"
                        levelHabitation.hint = "จำนวนชั้นของบ้าน"
                        commonFee.visibility = View.GONE
                        distance.visibility = View.GONE
                        statusText.visibility = View.VISIBLE /////
                        statusText.text = "สถาพบ้าน"
                        status.visibility = View.VISIBLE
                        statusNew.text = "บ้านใหม่"
                        statusOld.text = "บ้านมือ 2"
                        codeProperty.visibility = View.GONE
                        numberDeed.visibility = View.GONE
                        deedText.visibility = View.GONE
                        Deed.visibility = View.GONE
                        tenureAge.visibility = View.VISIBLE
                        Age.visibility = View.VISIBLE
                        Age.hint = "อายุบ้าน(ปี)"
                        dateText.visibility = View.VISIBLE
                        Date.visibility = View.VISIBLE
                        directionText.visibility = View.VISIBLE
                        direction.visibility = View.VISIBLE  ////
                        facilities.visibility = View.VISIBLE
                        facilitiesCondo.visibility = View.VISIBLE
                        pool.visibility = View.VISIBLE
                        Fitness.visibility = View.VISIBLE
                        ShuttleBus.visibility = View.VISIBLE
                        WVmachine.visibility = View.GONE
                        Elevator.visibility = View.GONE
                        Lobby.visibility = View.GONE
                        facilitiesproperty.visibility = View.GONE
                    }
                    typedase.getItemAtPosition(position) == "คอนโด" -> {
                        deedtext6.visibility = View.GONE
                        typeland.visibility = View.GONE
                        codedeed.visibility = View.VISIBLE
                        pricePM.visibility = View.GONE
                        costestimate.visibility = View.GONE
                        descriptionPropertys.visibility = View.VISIBLE
                        distanceUse.hint = "พื้นที่ใช้สอย (ตร.ม.)"
                        levelHabitation.hint = "ชั้นที่อยู่"
                        commonFee.visibility = View.VISIBLE
                        distance.visibility = View.GONE
                        statusText.visibility = View.VISIBLE /////
                        statusText.text = "สถาพคอนโด"
                        status.visibility = View.VISIBLE
                        statusNew.text = "คอนโดใหม่"
                        statusOld.text = "คอนโดเมือ 2"
                        codeProperty.visibility = View.GONE
                        numberDeed.visibility = View.GONE
                        deedText.visibility = View.GONE
                        Deed.visibility = View.GONE
                        tenureAge.visibility = View.GONE
                        Age.visibility = View.VISIBLE
                        Age.hint = "อายุคอนโด(ปี)"
                        dateText.visibility = View.VISIBLE
                        Date.visibility = View.VISIBLE
                        directionText.visibility = View.VISIBLE
                        direction.visibility = View.VISIBLE  ////
                        facilities.visibility = View.VISIBLE
                        facilitiesCondo.visibility = View.VISIBLE
                        pool.visibility = View.VISIBLE
                        Fitness.visibility = View.VISIBLE
                        ShuttleBus.visibility = View.VISIBLE
                        WVmachine.visibility = View.VISIBLE
                        Elevator.visibility = View.VISIBLE
                        Lobby.visibility = View.VISIBLE
                        facilitiesproperty.visibility = View.VISIBLE
                        Kitchen.visibility = View.VISIBLE
                        LivingR.visibility = View.VISIBLE
                        EventR.visibility = View.VISIBLE
                        MeetingR.visibility = View.VISIBLE
                        Balcony.visibility = View.VISIBLE
                    }
                    typedase.getItemAtPosition(position) == "อาคารพานิชย์" -> {
                        deedtext6.visibility = View.GONE
                        typeland.visibility = View.GONE
                        codedeed.visibility = View.VISIBLE
                        pricePM.visibility = View.GONE
                        costestimate.visibility = View.GONE
                        descriptionPropertys.visibility = View.VISIBLE
                        distanceUse.hint = "พื้นที่ใช้สอย (ตร.ม.)"
                        levelHabitation.hint = "จำนวนชั้น"
                        commonFee.visibility = View.VISIBLE
                        distance.visibility = View.GONE
                        statusText.visibility = View.VISIBLE /////
                        statusText.text = "สถาพอาคาร"
                        status.visibility = View.VISIBLE
                        statusNew.text = "อาคารใหม่"
                        statusOld.text = "อาคารมือ 2"
                        codeProperty.visibility = View.GONE
                        numberDeed.visibility = View.GONE
                        deedText.visibility = View.GONE
                        Deed.visibility = View.GONE
                        tenureAge.visibility = View.GONE
                        Age.visibility = View.VISIBLE
                        Age.hint = "อายุอาคาร(ปี)"
                        dateText.visibility = View.VISIBLE
                        Date.visibility = View.VISIBLE
                        directionText.visibility = View.VISIBLE
                        direction.visibility = View.VISIBLE  ////
                        facilities.visibility = View.VISIBLE
                        facilitiesCondo.visibility = View.VISIBLE
                        pool.visibility = View.GONE
                        Fitness.visibility = View.GONE
                        ShuttleBus.visibility = View.GONE
                        WVmachine.visibility = View.GONE
                        Elevator.visibility = View.GONE
                        Lobby.visibility = View.GONE
                        facilitiesproperty.visibility = View.VISIBLE
                        Kitchen.visibility = View.GONE
                        LivingR.visibility = View.GONE
                        EventR.visibility = View.GONE
                        MeetingR.visibility = View.GONE
                        Balcony.visibility = View.GONE
                    }
                    else -> {
                        deedtext6.visibility = View.VISIBLE
                        typeland.visibility = View.VISIBLE
                        codedeed.visibility = View.GONE
                        pricePM.visibility = View.VISIBLE
                        costestimate.visibility = View.VISIBLE
                        distance.visibility = View.VISIBLE
                        descriptionPropertys.visibility = View.GONE
                        facilities.visibility = View.GONE
                        facilitiesproperty.visibility = View.GONE
                        facilitiesCondo.visibility = View.GONE
                        statusText.visibility = View.GONE
                        status.visibility = View.GONE
                        codeProperty.visibility = View.VISIBLE
                        numberDeed.visibility = View.VISIBLE
                        Deed.visibility = View.VISIBLE
                        tenureAge.visibility = View.VISIBLE
                        Age.visibility = View.GONE
                        dateText.visibility = View.GONE
                        Date.visibility = View.GONE
                        directionText.visibility = View.GONE
                        direction.visibility = View.GONE

                    }
                }
            }


        }

        province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val Province_Name = province.getItemAtPosition(position)
                var ID: String? = null
                for (i in Province.indices) {
                    if (Province_Name.equals(Province[i].PROVINCE_NAME)) {
                        ID = Province[i].PROVINCE_ID
                        break
                    }
                }
                callServerAmphur(ID!!)
            }

        }

        amphur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val Amphur_Name = amphur.selectedItem.toString()
                var ID: String? = null
                for (i in Amphur.indices) {
                    if (Amphur_Name.equals(Amphur[i].AMPHUR_NAME)) {
                        ID = Amphur[i].AMPHUR_ID
                        break
                    }
                }

                callServerDistrict(ID!!)
            }

        }

        district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val District_Name = district.selectedItem.toString()
                var ID: String? = null
                for (i in District.indices) {
                    if (District_Name.equals(District[i].DISTRICT_NAME)) {
                        ID = District[i].DISTRICT_ID
                        break
                    }
                }

                callServerZipcode(ID!!)
            }

        }

        newContact.setOnClickListener {
            val alertdialog: Dialog? = Dialog(this)
            alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertdialog.setContentView(R.layout.alertcontact)
            alertdialog.setCancelable(false)
            val name = alertdialog.findViewById<EditText>(R.id.nameContact)
            val email = alertdialog.findViewById<EditText>(R.id.emailContact)
            val phone = alertdialog.findViewById<EditText>(R.id.phoneContact)
            val line = alertdialog.findViewById<EditText>(R.id.lineContact)
            val cannelbutton = alertdialog.findViewById<Button>(R.id.cannel_contactDialog)
            cannelbutton!!.setOnClickListener {
                alertdialog.cancel()
            }
            val confirmbutton = alertdialog.findViewById<Button>(R.id.confirm_contactDialog)
            confirmbutton!!.setOnClickListener {
                if (name.text.isNotEmpty() and email.text.isNotEmpty() and phone.text.isNotEmpty() and line.text.isNotEmpty()) {
                    callServerInsertContact(
                        name.text.toString(),
                        email.text.toString(),
                        phone.text.toString(),
                        line.text.toString(),
                        ID_User
                    )
                    alertdialog.cancel()
                } else {
                    Toast.makeText(
                        this@Increase_Property,
                        "กรุณากรอกข้อมูลให้ครบถ้วน",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            alertdialog.show()
        }


        Comfirm_land.setOnClickListener {

            if (announce.text.isNotEmpty() && contact1.selectedItem.toString() != "-กรุณาเลือกผู้ติดต่อ-" && contact2.selectedItem.toString() != "-กรุณาเลือกผู้ติดต่อ-" && contact3.selectedItem.toString() != "-กรุณาเลือกผู้ติดต่อ-") {

                val roadwide =
                    if (roadwide.selectedItem != "--ความกว้างถนน--") roadwide.selectedItem else "NULL"

                val Deed: Int = status.checkedRadioButtonId
                Deeded = if (Deed != -1) {
                    val radio: RadioButton = findViewById(Deed)
                    if (radio.text == statusNew) {
                        "มี"
                    } else {
                        "ไม่มี"
                    }
                } else {
                    "NULL"
                }

                val Groundlevel: Int = Groundlevel.checkedRadioButtonId
                Groundleveled = if (Groundlevel != -1) {
                    val radio: RadioButton = findViewById(Groundlevel)
                    if (radio.text == GroundlevelH) {
                        "สูงกว่าถนน"
                    } else {
                        "ต่ำกว่าถนน"
                    }
                } else {
                    "NULL"
                }

                val roadtype: Int = roadtype.checkedRadioButtonId
                RoadType = if (roadtype != -1) {
                    val radio: RadioButton = findViewById(roadtype)
                    if (radio.text == mud) {
                        "โคลน"
                    } else if (radio.text == asphalt) {
                        "ยางมะตอย"
                    } else if (radio.text == concrete) {
                        "คอนกรีต"
                    } else {
                        "อื่นๆ"
                    }
                } else {
                    "NULL"
                }

                val SellPrice = if (sellprice.text.isNotEmpty()) {
                    sellprice.text.toString()
                } else {
                    "0"
                }
                val Cost = if (costestimateB.text.isNotEmpty()) {
                    costestimateB.text.toString()
                } else {
                    "0"
                }
                val MarketPrice = if (marketprice.text.isNotEmpty()) {
                    marketprice.text.toString()
                } else {
                    "0"
                }
                val PricePM = if (pricePM.text.isNotEmpty()) {
                    pricePM.text.toString()
                } else {
                    "0"
                }
                val CostB = if (costestimateB.text.isNotEmpty()) {
                    costestimateB.text.toString()
                } else {
                    "0"
                }

                val statusProperty = if (status.checkedRadioButtonId != -1) {
                    if (statusNew.isChecked) {
                        statusNew.text.toString()
                    } else {
                        statusOld.text.toString()
                    }
                } else {
                    ""
                }


                val day: String =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                var contacts1 = "null"
                for (i in ContactS.indices) {
                    if (contact1.selectedItem.toString() == ContactS[i].Name) {
                        contacts1 = ContactS[i].ID_Contact
                        break
                    }
                }
                var contacts2 = "null"
                for (i in ContactS.indices) {
                    if (contact1.selectedItem.toString() == ContactS[i].Name) {
                        contacts2 = ContactS[i].ID_Contact
                        break
                    }
                }
                var contacts3 = "null"
                for (i in ContactS.indices) {
                    if (contact1.selectedItem.toString() == ContactS[i].Name) {
                        contacts3 = ContactS[i].ID_Contact
                        break
                    }
                }



                if (typedase.selectedItem == "ที่ดิน") {
                    CallServerInsertLand(
                        typeland.selectedItem.toString(),
                        announce.text.toString(),
                        codedeed.text.toString(),
                        "",
                        SellPrice,
                        SellPrice,
                        Cost,
                        CostB,
                        MarketPrice,
                        PricePM,
                        distanceR.text.toString(),
                        distanceG.text.toString(),
                        distanceWA.text.toString(),
                        distance.text.toString(),
                        Deeded,
                        RoadType,
                        roadwide.toString(),
                        Groundleveled,
                        Groundvalue.text.toString(),
                        moredetail.text.toString(),
                        Latitude.toString(),
                        Longitude.toString(),
                        assStatus.selectedItem.toString(),
                        observationpoint.text.toString(),
                        location.text.toString(),
                        province.selectedItem.toString(),
                        amphur.selectedItem.toString(),
                        district.selectedItem.toString(),
                        zipcode.selectedItem.toString(),
                        contacts1,
                        conStatus1.selectedItem.toString(),
                        contacts2,
                        conStatus2.selectedItem.toString(),
                        contacts3,
                        conStatus3.selectedItem.toString(),
                        "",
                        blind.isChecked.toInt(),
                        neareducation.isChecked.toInt(),
                        cenmarket.isChecked.toInt(),
                        market.isChecked.toInt(),
                        river.isChecked.toInt(),
                        mainroad.isChecked.toInt(),
                        insoi.isChecked.toInt(),
                        letcN.isChecked.toInt(),
                        "0",
                        tenureAge.text.toString(),
                        "",
                        ID_User,
                        day
                    ).toString()
                } else {
                    CallServerInsertProperty(
                        typedase.selectedItem.toString(),
                        announce.text.toString(),
                        codedeed.text.toString(),
                        SellPrice,
                        Cost,
                        CostB,
                        MarketPrice,
                        bathroomN.text.toString(),
                        bedroom.text.toString(),
                        parkcount.text.toString(),
                        distanceUse.text.toString(),
                        levelHabitation.text.toString(),
                        distanceR.text.toString(),
                        distanceG.text.toString(),
                        distanceWA.text.toString(),
                        distance.text.toString(),
                        statusProperty,
                        Age.text.toString(),
                        buildFD.selectedItem.toString(),
                        buildFM.selectedItem.toString(),
                        buildFY.selectedItem.toString(),
                        direction.selectedItem.toString(),
                        RoadType,
                        roadwide.toString(),
                        Groundleveled,
                        Groundvalue.text.toString(),
                        moredetail.text.toString(),
                        Latitude.toString(),
                        Longitude.toString(),
                        assStatus.selectedItem.toString(),
                        observationpoint.text.toString(),
                        location.text.toString(),
                        province.selectedItem.toString(),
                        amphur.selectedItem.toString(),
                        district.selectedItem.toString(),
                        zipcode.selectedItem.toString(),
                        contacts1,
                        conStatus1.selectedItem.toString(),
                        contacts2,
                        conStatus2.selectedItem.toString(),
                        contacts3,
                        conStatus3.selectedItem.toString(),
                        day,
                        blind.isChecked.toInt(),
                        neareducation.isChecked.toInt(),
                        cenmarket.isChecked.toInt(),
                        market.isChecked.toInt(),
                        river.isChecked.toInt(),
                        mainroad.isChecked.toInt(),
                        insoi.isChecked.toInt(),
                        letcN.isChecked.toInt(),
                        airconditioner.isChecked.toInt(),
                        afan.isChecked.toInt(),
                        AirPurifier.isChecked.toInt(),
                        Waterheater.isChecked.toInt(),
                        WIFI.isChecked.toInt(),
                        TV.isChecked.toInt(),
                        refrigerator.isChecked.toInt(),
                        microwave.isChecked.toInt(),
                        gasstove.isChecked.toInt(),
                        wardrobe.isChecked.toInt(),
                        TCset.isChecked.toInt(),
                        sofa.isChecked.toInt(),
                        shelves.isChecked.toInt(),
                        CCTV.isChecked.toInt(),
                        Securityguard.isChecked.toInt(),
                        pool.isChecked.toInt(),
                        Fitness.isChecked.toInt(),
                        Publicarea.isChecked.toInt(),
                        ShuttleBus.isChecked.toInt(),
                        WVmachine.isChecked.toInt(),
                        "0",
                        Elevator.isChecked.toInt(),
                        Lobby.isChecked.toInt(),
                        ATM.isChecked.toInt(),
                        BeautySalon.isChecked.toInt(),
                        Balcony.isChecked.toInt(),
                        EventR.isChecked.toInt(),
                        MeetingR.isChecked.toInt(),
                        LivingR.isChecked.toInt(),
                        Hairsalon.isChecked.toInt(),
                        Laundry.isChecked.toInt(),
                        Store.isChecked.toInt(),
                        Supermarket.isChecked.toInt(),
                        Store.isChecked.toInt(),
                        commonFee.text.toString(),
                        Kitchen.isChecked.toInt(),
                        Age.text.toString(),
                        "",
                        ID_User
                    ).toString()
                }
            } else {
                val alertdialog: Dialog? = Dialog(this)
                alertdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                alertdialog.setContentView(R.layout.alertsignup)
                alertdialog.setCancelable(false)

                val messagealert = alertdialog.findViewById<TextView>(R.id.textView6)
                messagealert.text = "กรุณาใส่ข้อมูลให้ครบทุกช่อง"
                val okbutton = alertdialog.findViewById<Button>(R.id.button6)
                okbutton!!.setOnClickListener {
                    alertdialog.cancel()
                }
                alertdialog.show()
            }

        }


    }


    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        when {
            googleApiClient.isConnected -> googleApiClient.disconnect()
        }
    }

    override fun onLocationChanged(location: Location?) {

        Latitude = location!!.latitude
        Longitude = location.longitude
        Location = LatLng(Latitude, Longitude)
        Log.d("LocationChanged", "Latiude : $Latitude Longitude : $Longitude")


    }


    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        Log.d("LocationReady", "Latiude : $Latitude Longitude : $Longitude")
        with(mMap) {

            isMyLocationEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isRotateGesturesEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isTiltGesturesEnabled = true
            uiSettings.isScrollGesturesEnabled = false
        }
        mMap.setOnMapClickListener {
            Log.d("ClickMap", it.toString())
            // mMap.addMarker(it)
        }
        mMap.setOnCameraMoveStartedListener {
            Scroll_Insert.isScrollContainer = false
            Scroll_Insert.stopNestedScroll(R.id.fragment_map_container)
        }

    }


    override fun onConnected(connectionHint: Bundle?) {


        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
        }

        val locationAvailability =
            LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient)
        if (locationAvailability.isLocationAvailable) {
            // Call Location Services
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 5000L
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this
            )
        }


    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    private fun FusedLocationProviderClient.requestLocation(
        request: LocationRequest
    ): Single<LocationResult> {
        return Single.create<LocationResult> { emitter ->
            requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    removeLocationUpdates(object : LocationCallback() {})
                        .addOnCompleteListener {
                            if (emitter.isDisposed) {
                                return@addOnCompleteListener
                            }

                            if (result != null && result.locations.isNotEmpty()) {
                                onSuccess(result)
                            } else {
                                onError(RuntimeException("Invalid location result"))
                            }
                        }
                }

                private fun onError(error: Exception) {
                    if (!emitter.isDisposed) {
                        emitter.onError(error)
                    }
                }

                private fun onSuccess(item: LocationResult) {
                    if (!emitter.isDisposed) {
                        emitter.onSuccess(item)
                    }
                }

            }, Looper.getMainLooper())
        }
    }


    fun callServerUploadImageProfile(img: RequestBody) {
        val call = MyService().getService().updateImageProfile(img)
        call.enqueue(object : Callback<MessageDAO> {

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Increase_Property, "Uploaded", Toast.LENGTH_SHORT).show()
                    getUpload(response.body()!!.Status(), response.body()!!.getImagepath())

                }
            }

            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
            }
        })
    }


    fun getUpload(set: String, set1: List<MessageDAO.Upload_path>) {
        if (set != "failed") {
            CallInsertPhoto(ID_Announce, set1[0].Name_Photo)
        }
    }

    fun CallInsertPhoto(ID: String, Name: String) {
        val call = MyService().getService().Insert_Photo(ID, Name)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    //Toast.makeText(this@Increase_Property, "Uploaded", Toast.LENGTH_SHORT).show()
                    Log.d("Upload", "Uploaded")
                    val intent = Intent(this@Increase_Property, Home::class.java)
                    intent.putExtra("ID_User", ID_User)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    CallInsertPhoto(ID, Name)
                }
            }

        })
    }


    fun CallServerInsertProperty(
        PropertyType: String,
        AnnounceTH: String,
        CodeDeed: String,
        SellPrice: String,
        Costestimate: String,
        CostestimateB: String,
        MarketPrice: String,
        BathRoom: String,
        BedRoom: String,
        CarPark: String,
        HouseArea: String,
        Floor: String,
        LandR: String,
        LandG: String,
        LandWA: String,
        LandU: String,
        HomeCondition: String,
        BuildingAge: String,
        BuildFD: String,
        BuildFM: String,
        BuildFY: String,
        Directions: String,
        RoadType: String,
        RoadWide: String,
        GroundLevel: String,
        GroundValue: String,
        MoreDetails: String,
        Latitude: String,
        Longitude: String,
        AsseStatus: String,
        ObservationPoint: String,
        Location: String,
        LProvince: String,
        LAmphur: String,
        LDistrict: String,
        LZipCode: String,
        ContactUo: String,
        ContactSo: String,
        ContactUt: String,
        ContactSt: String,
        ContactU: String,
        ContactS: String,
        Created: String,
        Blind: String,
        Neareducation: String,
        Cenmarket: String,
        Market: String,
        River: String,
        Mainroad: String,
        Insoi: String,
        Letc: String,
        airconditioner: String,
        afan: String,
        AirPurifier: String,
        Waterheater: String,
        WIFI: String,
        TV: String,
        refrigerator: String,
        microwave: String,
        gasstove: String,
        wardrobe: String,
        TCset: String,
        sofa: String,
        shelves: String,
        CCTV: String,
        Securityguard: String,
        pool: String,
        Fitness: String,
        Publicarea: String,
        ShuttleBus: String,
        WVmachine: String,
        CWmachine: String,
        Elevator: String,
        Lobby: String,
        ATM: String,
        BeautySalon: String,
        Balcony: String,
        EventR: String,
        MeetingR: String,
        LivingR: String,
        Hairsalon: String,
        Laundry: String,
        Store: String,
        Supermarket: String,
        CStore: String,
        Mfee: String,
        Kitchen: String,
        LandAge: String,
        PPStatus: String,
        Owner: String
    ) {
        val call = MyService().getService().InsertProperty(
            PropertyType,
            AnnounceTH,
            CodeDeed,
            SellPrice,
            Costestimate,
            CostestimateB,
            MarketPrice,
            BathRoom,
            BedRoom,
            CarPark,
            HouseArea,
            Floor,
            LandR,
            LandG,
            LandWA,
            LandU,
            HomeCondition,
            BuildingAge,
            BuildFD,
            BuildFM,
            BuildFY,
            Directions,
            RoadType,
            RoadWide,
            GroundLevel,
            GroundValue,
            MoreDetails,
            Latitude,
            Longitude,
            AsseStatus,
            ObservationPoint,
            Location,
            LProvince,
            LAmphur,
            LDistrict,
            LZipCode,
            ContactUo,
            ContactSo,
            ContactUt,
            ContactSt,
            ContactU,
            ContactS,
            Created,
            Blind,
            Neareducation,
            Cenmarket,
            Market,
            River,
            Mainroad,
            Insoi,
            Letc,
            airconditioner,
            afan,
            AirPurifier,
            Waterheater,
            WIFI,
            TV,
            refrigerator,
            microwave,
            gasstove,
            wardrobe,
            TCset,
            sofa,
            shelves,
            CCTV,
            Securityguard,
            pool,
            Fitness,
            Publicarea,
            ShuttleBus,
            WVmachine,
            CWmachine,
            Elevator,
            Lobby,
            ATM,
            BeautySalon,
            Balcony,
            EventR,
            MeetingR,
            LivingR,
            Hairsalon,
            Laundry,
            Store,
            Supermarket,
            CStore,
            Mfee,
            Kitchen,
            LandAge,
            PPStatus,
            Owner
        )
        call.enqueue(object : Callback<MessageDAO> {
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Increase_Property, "Uploaded", Toast.LENGTH_SHORT).show()
                    Log.d("Upload", "Uploaded")
                    getAnnounce(response.body()!!.getInsert_Announce())
                }
            }

            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    CallServerInsertProperty(
                        PropertyType,
                        AnnounceTH,
                        CodeDeed,
                        SellPrice,
                        Costestimate,
                        CostestimateB,
                        MarketPrice,
                        BathRoom,
                        BedRoom,
                        CarPark,
                        HouseArea,
                        Floor,
                        LandR,
                        LandG,
                        LandWA,
                        LandU,
                        HomeCondition,
                        BuildingAge,
                        BuildFD,
                        BuildFM,
                        BuildFY,
                        Directions,
                        RoadType,
                        RoadWide,
                        GroundLevel,
                        GroundValue,
                        MoreDetails,
                        Latitude,
                        Longitude,
                        AsseStatus,
                        ObservationPoint,
                        Location,
                        LProvince,
                        LAmphur,
                        LDistrict,
                        LZipCode,
                        ContactUo,
                        ContactSo,
                        ContactUt,
                        ContactSt,
                        ContactU,
                        ContactS,
                        Created,
                        Blind,
                        Neareducation,
                        Cenmarket,
                        Market,
                        River,
                        Mainroad,
                        Insoi,
                        Letc,
                        airconditioner,
                        afan,
                        AirPurifier,
                        Waterheater,
                        WIFI,
                        TV,
                        refrigerator,
                        microwave,
                        gasstove,
                        wardrobe,
                        TCset,
                        sofa,
                        shelves,
                        CCTV,
                        Securityguard,
                        pool,
                        Fitness,
                        Publicarea,
                        ShuttleBus,
                        WVmachine,
                        CWmachine,
                        Elevator,
                        Lobby,
                        ATM,
                        BeautySalon,
                        Balcony,
                        EventR,
                        MeetingR,
                        LivingR,
                        Hairsalon,
                        Laundry,
                        Store,
                        Supermarket,
                        CStore,
                        Mfee,
                        Kitchen,
                        LandAge,
                        PPStatus,
                        Owner
                    )
                }
            }

        })
    }

    fun CallServerInsertLand(
        ColorType: String,
        AnnounceTH: String,
        CodeDeed: String,
        TypeCode: String,
        CodeProperty: String,
        SellPrice: String,
        Costestimate: String,
        CostestimateB: String,
        MarketPrice: String,
        PriceWA: String,
        LandR: String,
        LandG: String,
        LandWA: String,
        Land: String,
        Deed: String,
        RoadType: String,
        RoadWide: String,
        GroundLevel: String,
        GroundValue: String,
        MoreDetails: String,
        Latitude: String,
        Longitude: String,
        AsseStatus: String,
        ObservationPoint: String,
        Location: String,
        LProvince: String,
        LAmphur: String,
        LDistrict: String,
        LZipCode: String,
        ContactUo: String,
        ContactSo: String,
        ContactUt: String,
        ContactSt: String,
        ContactU: String,
        ContactS: String,
        Place: String,
        Blind: String,
        Neareducation: String,
        Cenmarket: String,
        Market: String,
        River: String,
        Mainroad: String,
        Insoi: String,
        Letc: String,
        WxD: String,
        LandAge: String,
        PPStatus: String,
        Owner: String,
        Created: String
    ) {
        val call = MyService().getService().InsertLand(
            ColorType,
            AnnounceTH,
            CodeDeed,
            TypeCode,
            CodeProperty,
            SellPrice,
            Costestimate,
            CostestimateB,
            MarketPrice,
            PriceWA,
            LandR,
            LandG,
            LandWA,
            Land,
            Deed,
            RoadType,
            RoadWide,
            GroundLevel,
            GroundValue,
            MoreDetails,
            Latitude,
            Longitude,
            AsseStatus,
            ObservationPoint,
            Location,
            LProvince,
            LAmphur,
            LDistrict,
            LZipCode,
            ContactUo,
            ContactSo,
            ContactUt,
            ContactSt,
            ContactU,
            ContactS,
            Place,
            Blind,
            Neareducation,
            Cenmarket,
            Market,
            River,
            Mainroad,
            Insoi,
            Letc,
            WxD,
            LandAge,
            PPStatus,
            Owner,
            Created
        )
        call.enqueue(object : Callback<MessageDAO> {
            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Increase_Property, "Uploaded", Toast.LENGTH_SHORT).show()
                    Log.d("Upload", "Uploaded")
                    getAnnounce(response.body()!!.getInsert_Announce())
                }
            }

            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    CallServerInsertLand(
                        ColorType,
                        AnnounceTH,
                        CodeDeed,
                        TypeCode,
                        CodeProperty,
                        SellPrice,
                        Costestimate,
                        CostestimateB,
                        MarketPrice,
                        PriceWA,
                        LandR,
                        LandG,
                        LandWA,
                        Land,
                        Deed,
                        RoadType,
                        RoadWide,
                        GroundLevel,
                        GroundValue,
                        MoreDetails,
                        Latitude,
                        Longitude,
                        AsseStatus,
                        ObservationPoint,
                        Location,
                        LProvince,
                        LAmphur,
                        LDistrict,
                        LZipCode,
                        ContactUo,
                        ContactSo,
                        ContactUt,
                        ContactSt,
                        ContactU,
                        ContactS,
                        Place,
                        Blind,
                        Neareducation,
                        Cenmarket,
                        Market,
                        River,
                        Mainroad,
                        Insoi,
                        Letc,
                        WxD,
                        LandAge,
                        PPStatus,
                        Owner,
                        Created
                    )
                }
            }

        })
    }

    fun getAnnounce(set: List<MessageDAO.Insert_Announce>) {
        ID_Announce = set[0].ID_Announce
        if(image_Selected.drawable != null){
            val img = RequestBody.create(MediaType.parse("image/*"), file as File)
            callServerUploadImageProfile(img)
        }else{
            val intent = Intent(this@Increase_Property, Home::class.java)
            intent.putExtra("ID_User", ID_User)
            startActivity(intent)
        }


    }

    fun callServerProvince() {
        val call = MyService().getService().Province()
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerProvince()
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getProvince(response.body()!!.getProvince())
                }
            }

        })
    }

    fun getProvince(set: List<MessageDAO.Province>) {
        Province = set
        val Province: Array<String?> = arrayOfNulls(set.size)
        for (i in set.indices) {
            Province[i] = set[i].PROVINCE_NAME
        }
        val adapterProvince =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Province)
        province.adapter = adapterProvince

        val Province_Name = province.selectedItem.toString()

        for (i in set.indices) {
            if ((Province_Name == set[i].PROVINCE_NAME)) {
                callServerAmphur(set[i].PROVINCE_ID)
                break
            }
        }


    }

    fun callServerAmphur(ID: String) {
        val call = MyService().getService().Amphurs(ID)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerAmphur(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    getAmphur(response.body()!!.getAmphur())
                }
            }

        })
    }

    fun getAmphur(set: List<MessageDAO.Amphur>) {
        Amphur = set
        val Amphur: Array<String?> = arrayOfNulls(set.size)
        for (i in set.indices) {
            Amphur[i] = set[i].AMPHUR_NAME
        }
        val adapterAmphur =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Amphur)
        amphur.adapter = adapterAmphur

        val Amphur_Name = amphur.selectedItem.toString()
        for (i in set.indices) {
            if ((Amphur_Name == set[i].AMPHUR_NAME) and (!Amphur_Name.contains("*"))) {
                callServerDistrict(set[i].AMPHUR_ID)
                break
            }
        }


    }

    fun callServerDistrict(ID: String) {
        val call = MyService().getService().District1(ID)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerDistrict(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    try {
                        getDistrict(response.body()!!.getDistrict())
                    } catch (e: Exception) {
                    }

                }
            }

        })
    }

    fun getDistrict(set: List<MessageDAO.District>) {
        District = set
        val District: Array<String?> = arrayOfNulls(set.size)
        for (i in set.indices) {
            District[i] = set[i].DISTRICT_NAME
        }
        val adapterdistrict =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, District)
        district.adapter = adapterdistrict

        val District_Name = district.selectedItem.toString()
        for (i in set.indices) {
            if ((District_Name == set[i].DISTRICT_NAME) and (!District_Name.contains("*"))) {
                callServerZipcode(set[i].DISTRICT_ID)
                break
            }
        }


    }

    fun callServerZipcode(ID: String) {
        val call = MyService().getService().Zipcode1(ID)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerZipcode(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    try {
                        getZipcode(response.body()!!.getZipcode())
                    } catch (e: Exception) {

                    }

                }
            }

        })
    }

    fun getZipcode(set: List<MessageDAO.Zipcode>) {
        Zipcode = set
        val Zipcode: Array<String?> = arrayOfNulls(set.size)
        for (i in set.indices) {
            Zipcode[i] = set[i].ZIPCODE
        }
        val adapterzipcode =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Zipcode)
        zipcode.adapter = adapterzipcode
        callServerContact(ID_User)
    }

    fun callServerContact(ID: String) {
        val call = MyService().getService().Contact(ID)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerContact(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    if (response.body()!!.Status() == "failed") {
                        val Contact: Array<String?> = arrayOfNulls(1)
                        Contact[0] = "-กรุณาเพิ่มผู้ติดต่อ-"
                        contact1.adapter = ArrayAdapter<String>(
                            this@Increase_Property,
                            android.R.layout.simple_dropdown_item_1line,
                            Contact
                        )
                        contact2.adapter = ArrayAdapter<String>(
                            this@Increase_Property,
                            android.R.layout.simple_dropdown_item_1line,
                            Contact
                        )
                        contact3.adapter = ArrayAdapter<String>(
                            this@Increase_Property,
                            android.R.layout.simple_dropdown_item_1line,
                            Contact
                        )
                    } else {
                        getConteact(response.body()!!.getContact())
                    }

                }
            }
        })
    }

    fun getConteact(set: List<MessageDAO.Contact>) {
        ContactS = set
        val Contact: Array<String?> = arrayOfNulls(set.size + 1)
        Contact[0] = "-กรุณาเลือกผู้ติดต่อ-"
        for (i in 1 until set.size + 1) {
            Contact[i] = set[i - 1].Name
        }
        contact1.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Contact)
        contact2.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Contact)
        contact3.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Contact)
    }

    fun callServerInsertContact(
        name: String,
        email: String,
        phone: String,
        line: String,
        ID: String
    ) {
        val call = MyService().getService().InsertContact(name, email, phone, line, ID)
        call.enqueue(object : Callback<MessageDAO> {
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                Log.d("Upload", "Fail=>" + t.localizedMessage)
                if (t.localizedMessage == "unexpected end of stream") {
                    callServerContact(ID_User)
                }

            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
                if (response.isSuccessful) {
                    Log.d("Upload", "finish")
                    callServerContact(ID_User)
                }
            }

        })
    }


    private fun Boolean.toInt() = if (this) "1" else "0"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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
            }
            R.id.recommend_menu->{
                val intent = Intent(this, Home::class.java)
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

}


fun String.toMoney() =
    if (this.contains(",")) {
        val formatter = DecimalFormat("#,###")
        val mm = this.split(",")
        var mString = ""
        for (i in mm) {
            mString += i
        }
        formatter.format(mString.toDouble())

    } else {
        val formatter = DecimalFormat("#,###")
        formatter.format(this.toDouble())
    }


fun String.moneyto() =
    if (this.contains(",")) {
        val mm = this.split(",")
        var mString = ""
        for (i in mm) {
            mString += i
        }
        mString
    } else {
        if (this.isNotEmpty()) {
            this
        } else {
            ""
        }
    }

fun RadioButton.onCheck() =
    if (isChecked){
        this.isChecked = false
    }else{
        this.isChecked = true
    }




