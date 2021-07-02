package com.notheart.propertymanagement

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.notheart.propertymanagement.API.MessageDAO
import com.notheart.propertymanagement.API.MessageDAO1
import com.notheart.propertymanagement.API.MyService
import com.notheart.propertymanagement.Adapter.ImageAdpter
import kotlinx.android.synthetic.main.property_info.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.NullPointerException
import java.text.DecimalFormat


class Property_Info : OnMapReadyCallback, AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    lateinit var   ID_User: String
    val formatter = DecimalFormat("#,###")


    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0!!
        with(googleMap) {

            isMyLocationEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isRotateGesturesEnabled = true
            uiSettings.isZoomControlsEnabled =true
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isCompassEnabled =true
            uiSettings.isTiltGesturesEnabled = true
            uiSettings.isScrollGesturesEnabled = false
        }



        callServerFindAnnounce(ID_Announce)
    }

    lateinit var imageadpter: ImageAdpter
    lateinit var ID_Announce: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.property_info)
        val bundle = intent.extras
        ID_Announce = bundle!!.getString("ID_Announce")!!
        ID_User = bundle.getString("ID_User")!!


        val mapFragment = MapFragment.newInstance()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_map_info, mapFragment)
        fragmentTransaction.commit()
        mapFragment.getMapAsync(this)



        button18.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("ID_User", ID_User)
            startActivity(intent)
        }

    }



    fun callServerFindAnnounce(ID: String){
        val call = MyService().getService().FindAnnounce(ID)
        call.enqueue(object : Callback<MessageDAO1> {
            override fun onResponse(call: Call<MessageDAO1>, response: Response<MessageDAO1>) {
                if (response.isSuccessful) {
                    if (ID_Announce[0].toString()=="l"){
                        getAnnounce_Lands(response.body()!!.getLand())
                    }else{
                        getAnnounce_Property(response.body()!!.getproperty())
                    }
                }
            }

            override fun onFailure(call: Call<MessageDAO1>, t: Throwable) {
                Log.d("Upload", "Fail=>"+t.localizedMessage)
                if (t.localizedMessage=="unexpected end of stream"){
                    callServerFindAnnounce(ID)
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun getAnnounce_Property(set: List<MessageDAO1.Announceinfo1>){
        info_CostEstimate.visibility = View.GONE
        if (set[0].ID_Property.contains("h")){
            info_WVMachine.visibility = View.GONE
            info_CWMachine.visibility = View.GONE
            info_Elevator.visibility = View.GONE
            info_Lobby.visibility = View.GONE
            facilities_Common.visibility = View.GONE
            facilities_Building.visibility = View.GONE
        }else if (set[0].ID_Property.contains("p")){
            facilities_Common.visibility = View.GONE
            info_Pool.visibility = View.GONE
            info_Fitness.visibility = View.GONE
            info_ShuttleBus.visibility = View.GONE
            info_WVMachine.visibility = View.GONE
            info_CWMachine.visibility = View.GONE
            info_Elevator.visibility = View.GONE
            info_Lobby.visibility = View.GONE
        }

        imageadpter = ImageAdpter(this,set[0].Photo)
        imageAnnounce.adapter = imageadpter

        info_AnnounceHeader.text = set[0].AnnounceTH
        info_Address.text = set[0].Location
        info_Price.text = formatter.format(set[0].MarketPrice.toDouble())
        info_type.text = info_type.text.toString()+set[0].PropertyType
        info_Announce.text = info_Announce.text.toString()+set[0].AnnounceTH
        info_Deedcode.text = info_Deedcode.text.toString()+set[0].CodeDeed
        info_SellPrice.text = info_SellPrice.text.toString()+formatter.format(set[0].SellPrice.toDouble())
        info_CostEstimateB.text = info_CostEstimateB.text.toString()+ formatter.format(set[0].CostestimateB.toDouble())
        info_MarketPrice.text = info_MarketPrice.text.toString()+formatter.format(set[0].MarketPrice.toDouble())
        info_floor.text = info_floor.text.toString()+set[0].Floor
        info_bathroomN.text = info_bathroomN.text.toString()+set[0].BathRoom
        info_bedroom.text = info_bedroom.text.toString()+set[0].BedRoom
        info_ParkCount.text = info_ParkCount.text.toString()+set[0].CarPark
        info_distanceUse.text = info_distanceUse.text.toString()+set[0].Publicarea
        info_distanceG.text = info_distanceG.text.toString()+set[0].LandG
        info_distanceWA.text = info_distanceWA.text.toString()+set[0].LandWA
        info_distanceR.text = info_distanceR.text.toString()+set[0].LandR
        info_HomeCondition.text = info_HomeCondition.text.toString()+set[0].HomeCondition
        info_assStatusText.text = info_assStatusText.text.toString()+set[0].AsseStatus
        info_tenureAge.visibility = View.GONE
        info_Age.text = info_Age.text.toString()+set[0].LandAge
        info_dateText.text = info_dateText.text.toString()+set[0].BuildFD+"/"+set[0].BuildFM+"/"+set[0].BuildFY
        info_directionText.text = info_directionText.text.toString()+set[0].Directions
        info_AirConditioner.text = info_AirConditioner.text.toString()+set[0].airconditioner.GetValue()
        info_aFan.text = info_aFan.text.toString()+set[0].afan.GetValue()
        info_AirPurifier.text = info_AirPurifier.text.toString()+set[0].AirPurifier.GetValue()
        info_WaterHeater.text = info_WaterHeater.text.toString()+set[0].Waterheater.GetValue()
        info_WIFI.text = info_WIFI.text.toString()+set[0].WIFI.GetValue()
        info_TV.text = info_TV.text.toString()+set[0].TV.GetValue()
        info_refrigerator.text = info_refrigerator.text.toString()+set[0].refrigerator.GetValue()
        info_microwave.text = info_microwave.text.toString()+set[0].microwave.GetValue()
        info_GasStove.text = info_GasStove.text.toString()+set[0].gasstove.GetValue()
        info_wardrobe.text = info_wardrobe.text.toString()+set[0].wardrobe.GetValue()
        info_TCSet.text = info_TCSet.text.toString()+set[0].TCset.GetValue()
        info_sofa.text = info_sofa.text.toString()+set[0].sofa.GetValue()
        info_shelves.text = info_shelves.text.toString()+set[0].shelves.GetValue()
        info_CCTV.text = info_CCTV.text.toString()+set[0].CCTV.GetValue()
        info_SecurityGuard.text = info_SecurityGuard.text.toString()+set[0].Securityguard.GetValue()
        info_Pool.text = info_Pool.text.toString()+set[0].pool.GetValue()
        info_Fitness.text = info_Fitness.text.toString()+set[0].Fitness.GetValue()
        info_PublicArea.text = info_PublicArea.text.toString()+set[0].Publicarea.GetValue()
        info_ShuttleBus.text = info_ShuttleBus.text.toString()+set[0].ShuttleBus.GetValue()
        info_WVMachine.text = info_WVMachine.text.toString()+set[0].WVmachine.GetValue()
        info_CWMachine.text = info_CWMachine.text.toString()+set[0].CWmachine.GetValue()
        info_Elevator.text = info_Elevator.text.toString()+set[0].Elevator.GetValue()
        info_Lobby.text = info_Lobby.text.toString()+set[0].Lobby.GetValue()
        info_Kitchen.text = info_Kitchen.text.toString()+set[0].Kitchen.GetValue()
        info_LivingR.text = info_LivingR.text.toString()+set[0].LivingR.GetValue()
        info_EventR.text = info_EventR.text.toString()+set[0].EventR.GetValue()
        info_MeetingR.text = info_MeetingR.text.toString()+set[0].MeetingR.GetValue()
        info_Balcony.text = info_Balcony.text.toString()+set[0].Balcony.GetValue()
        info_ATM.text = info_ATM.text.toString()+set[0].ATM.GetValue()
        info_BeautySalon.text = info_BeautySalon.text.toString()+set[0].BeautySalon.GetValue()
        info_CStore.text = info_CStore.text.toString()+set[0].CStore.GetValue()
        info_HairSalon.text= info_HairSalon.text.toString()+set[0].Hairsalon.GetValue()
        info_Laundry.text = info_Laundry.text.toString()+set[0].Laundry.GetValue()
        info_Store.text = info_Store.text.toString()+set[0].Store.GetValue()
        info_Supermarket.text = info_Supermarket.text.toString()+set[0].Supermarket.GetValue()
        info_blind.text = info_blind.text.toString()+set[0].Blind.GetValue()
        info_neareducation.text = info_neareducation.text.toString()+set[0].Neareducation.GetValue()
        info_cenmarket.text = info_cenmarket.text.toString()+set[0].Cenmarket.GetValue()
        info_insoi.text = info_insoi.text.toString()+set[0].Insoi.GetValue()
        info_market.text = info_market.text.toString()+set[0].Market.GetValue()
        info_river.text = info_river.text.toString()+set[0].River.GetValue()
        info_mainroad.text = info_mainroad.text.toString()+set[0].Mainroad.GetValue()
        info_letcN.text = info_letcN.text.toString()+set[0].Letc.GetValue()
        info_TypeR.text = info_TypeR.text.toString()+set[0].RoadType
        info_wideRoad.text = info_wideRoad.text.toString()+set[0].RoadWide
        info_GroundLevel.text = info_GroundLevel.text.toString()+set[0].GroundLevel
        moredetail.text = moredetail.text.toString()+set[0].MoreDetails
        info_observationPoint.text = info_observationPoint.text.toString()+set[0].ObservationPoint
        info_location.text = info_location.text.toString()+set[0].Location
        findContact(set[0].ContactUo)



            Log.d("Map", set[0].Latitude.toString() + " : " + set[0].Longitude.toString())
                val location = LatLng(set[0].Latitude,set[0].Longitude)

                googleMap.addMarker(MarkerOptions().position(location).title("Here"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))







    }

    @SuppressLint("SetTextI18n")
    fun getAnnounce_Lands(set: List<MessageDAO1.Announceinfo2>){

        imageadpter = ImageAdpter(this,set[0].Photo)
        imageAnnounce.adapter = imageadpter

        houseDetail.visibility = View.GONE
        facilities.visibility = View.GONE
        info_HomeCondition.visibility = View.GONE
        info_dateText.visibility = View.GONE
        info_directionText.visibility = View.GONE
        info_AnnounceHeader.text = set[0].AnnounceTH
        info_Address.text = set[0].Location
        info_CostEstimate.text = info_CostEstimate.text.toString()+formatter.format(set[0].Costestimate.toDouble())
        info_Price.text = formatter.format(set[0].MarketPrice.toDouble())
        info_type.text = info_type.text.toString()+"ที่ดิน"
        info_Announce.text = info_Announce.text.toString()+set[0].AnnounceTH
        info_Deedcode.text = info_Deedcode.text.toString()+set[0].CodeDeed
        info_SellPrice.text = info_SellPrice.text.toString()+formatter.format(set[0].SellPrice.toDouble())
        info_CostEstimateB.text = info_CostEstimateB.text.toString()+formatter.format(set[0].CostestimateB.toDouble())
        info_MarketPrice.text = info_MarketPrice.text.toString()+formatter.format(set[0].MarketPrice.toDouble())
        info_distanceG.text = info_distanceG.text.toString()+set[0].LandG
        info_distanceWA.text = info_distanceWA.text.toString()+set[0].LandWA
        info_distanceR.text = info_distanceR.text.toString()+set[0].LandR
        info_assStatusText.text = info_assStatusText.text.toString()+set[0].AsseStatus
        info_tenureAge.visibility = View.GONE
        info_Age.text = info_Age.text.toString()+set[0].LandAge
        info_blind.text = info_blind.text.toString()+set[0].Blind.GetValue()
        info_neareducation.text = info_neareducation.text.toString()+set[0].Neareducation.GetValue()
        info_cenmarket.text = info_cenmarket.text.toString()+set[0].Cenmarket.GetValue()
        info_insoi.text = info_insoi.text.toString()+set[0].Insoi.GetValue()
        info_market.text = info_market.text.toString()+set[0].Market.GetValue()
        info_river.text = info_river.text.toString()+set[0].River.GetValue()
        info_mainroad.text = info_mainroad.text.toString()+set[0].Mainroad.GetValue()
        info_letcN.text = info_letcN.text.toString()+set[0].Letc.GetValue()
        info_TypeR.text = info_TypeR.text.toString()+set[0].RoadType
        info_wideRoad.text = info_wideRoad.text.toString()+set[0].RoadWide
        info_GroundLevel.text = info_GroundLevel.text.toString()+set[0].GroundLevel
        moredetail.text = moredetail.text.toString()+set[0].MoreDetails
        info_observationPoint.text = info_observationPoint.text.toString()+set[0].ObservationPoint
        info_location.text = info_location.text.toString()+set[0].Location
        findContact(set[0].ContactUo)


        try {
            val location = LatLng(set[0].Latitude,set[0].Longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("Here"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

        }catch (e: NullPointerException){ }
        catch (e: UninitializedPropertyAccessException){}
    }


    fun findContact(ID: String){
        val call = MyService().getService().findContact(ID)
        call.enqueue(object : Callback<MessageDAO>{
            override fun onFailure(call: Call<MessageDAO>, t: Throwable) {
                if (t.localizedMessage=="unexpected end of stream"){
                    findContact(ID)
                }
            }

            override fun onResponse(call: Call<MessageDAO>, response: Response<MessageDAO>) {
               if (response.isSuccessful){
                   getCntact(response.body()!!.getContact())
               }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun getCntact(set: List<MessageDAO.Contact>){
        info_Contact.text = info_Contact.text.toString()+set[0].Name
    }


    fun String.GetValue() = if (this=="0") "ไม่มี" else "มี"
}
