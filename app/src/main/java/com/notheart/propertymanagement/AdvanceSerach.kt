package com.notheart.propertymanagement

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MyService
import kotlinx.android.synthetic.main.activity_advance_serach.*
import kotlinx.android.synthetic.main.increase_property.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AdvanceSerach : AppCompatActivity() {
    lateinit var ID_User: String
    lateinit var Announce_item: List<MessageDAO.Announce>
    lateinit var Status: String
    lateinit var Province: List<MessageDAO.Province>
    lateinit var Amphur: List<MessageDAO.Amphur>
    lateinit var District: List<MessageDAO.District>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance_serach)
        val bundle = intent.extras
        ID_User = bundle!!.getString("ID_User")!!

        province_View.visibility = View.GONE

        //callServerProvince()

            Search_Button.setOnClickListener {
                PPstatus.selectedItem.toString()
                var PPStatus = ""
                var Type = ""
                var Word = ""
                var MinPrice = "0"
                var MaxPrice = "0"
                if(PPstatus.selectedItem.toString()!="-เลือก-"){
                       PPStatus = PPstatus.selectedItem.toString()
                }
                if(typeSearch.selectedItem.toString()!="-เลือก-"){
                  Type = typeSearch.selectedItem.toString()
                }
                Word=" "

                if (Searchprice.selectedItem.toString()!="-เลือก-" && Searchprice.selectedItem.toString()!="ต่ำกว่า500,000" &&Searchprice.selectedItem.toString()!="มากกว่า10,000,000"){
                    val getPrice:List<String> = Searchprice.selectedItem.toString().split("–")
                    val getMinprice :List<String> = getPrice[0].split(",")
                    val getMaxprice :List<String> = getPrice[1].split(",")
                    MinPrice = ""
                    MaxPrice = ""
                    for (i in getMinprice){
                        MinPrice += i
                    }
                    for (i in getMaxprice){
                        MaxPrice += i
                    }
                }else if(Searchprice.selectedItem.toString()!="-เลือก-" && Searchprice.selectedItem.toString()=="ต่ำกว่า500,000" ){
                    MaxPrice = "500000"
                }else if(Searchprice.selectedItem.toString()!="-เลือก-" && Searchprice.selectedItem.toString()=="มากกว่า10,000,000" ){
                    MinPrice = "10000000"
                }


                Log.d("Money", "$MinPrice : $MaxPrice")
                val Search: Array<String> = arrayOf( PPStatus,Type,Word,MinPrice,MaxPrice)
                Log.d("ASearch",Search.contentToString())
                val intent = Intent(this, MyProperty::class.java)
                intent.putExtra("ID_User", ID_User)
                intent.putExtra("Search", Search)


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
        val adapterProvince = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Province)
        Province_AS.adapter = adapterProvince

        val Province_Name = Province_AS.selectedItem.toString()

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
        Amphur_AS.adapter = adapterAmphur

        val Amphur_Name =  Amphur_AS.selectedItem.toString()
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
        District_AS.adapter = adapterdistrict



    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu ,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
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


}
