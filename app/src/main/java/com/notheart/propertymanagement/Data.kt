package com.notheart.propertymanagement

import android.app.ProgressDialog
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Entity
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import androidx.work.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.MalformedURLException
import java.nio.charset.StandardCharsets
import java.io.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class Data {
    data class UserData(var ID_User: String ,var firstname: String ,var lastname: String ,var email: String ,var password: String ,var Occupation: String ,var Age: String ,var Gender: String,var birthday: String ,var location: String ,var phone: String ,var profile: String ,var created: String )
    data class LandData(var ID_Lands: String,var ColorType: String,var AnnounceTH: String,var CodeDeed: String,var TypeCode: String,var CodeProperty: String,var SellPrice: String,var Costestimate: String,var CostestimateB: String,var MarketPrice: String,var PriceWA: String,var LandR: String,var LandG: String,var LandWA: String,var Land: String,var Deed: String,var RoadType: String,var RoadWide: String,var GroundLevel: String,var GroundValue: String,var MoreDetails: String,var Latitude: String,var Longitude: String,var AsseStatus: String,var ObservationPoint: String,var Location: String,var LProvince: String,var LAmphur: String,var LDistrict: String,var LZipCode: String,var ContactUo: String,var ContactSo: String,var ContactUt: String,var ContactSt: String,var ContactU: String,var ContactS: String,var Created: String,var Place: String,var Blind: String,var Neareducation: String,var Cenmarket: String,var Market: String,var River: String,var Mainroad: String,var Insoi: String,var Letc: String,var WxD: String,var LandAge: String,var PPStatus: String,var Owner: String)
    data class propertysData(var ID_Property: String,var PropertyType: String,var AnnounceTH: String,var CodeDeed: String,var SellPrice: String,var Costestimate: String,var CostestimateB: String,var MarketPrice: String,var BathRoom: String,var BedRoom: String,var CarPark: String,var HouseArea: String,var Floor: String,var LandR: String,var LandG: String,var LandWA: String,var LandU: String,var HomeCondition: String,var BuildingAge: String,var BuildFD: String,var BuildFM: String,var BuildFY: String,var Directions: String,var RoadType: String,var RoadWide: String,var GroundLevel: String,var GroundValue: String,var MoreDetails: String,var Latitude: String,var Longitude: String,var AsseStatus: String,var ObservationPoint: String,var Location: String,var LProvince: String,var LAmphur: String,var LDistrict: String,var LZipCode: String,var ContactUo: String,var ContactSo: String,var ContactUt: String,var ContactSt: String,var ContactU: String,var ContactS: String,var Created: String,var Blind: String,var Neareducation: String,var Cenmarket: String,var Market: String,var River: String,var Mainroad: String,var Insoi: String,var Letc: String,var airconditioner: String,var afan: String,var AirPurifier: String,var Waterheater: String,var WIFI: String,var TV: String,var refrigerator: String,var microwave: String,var gasstove: String,var wardrobe: String,var TCset: String,var sofa: String,var shelves: String,var CCTV: String,var Securityguard: String,var pool: String,var Fitness: String,var Publicarea: String,var ShuttleBus: String,var WVmachine: String,var CWmachine: String,var Elevator: String,var Lobby: String,var ATM: String,var BeautySalon: String,var Balcony: String,var EventR: String,var MeetingR: String,var LivingR: String,var Hairsalon: String,var Laundry: String,var Store: String,var Supermarket: String,var CStore: String,var Mfee: String,var Kitchen: String,var LandAge: String,var PPStatus: String,var Owner: String)
    data class GroupData(var ID_Group: String, var Name: String, var Img: String, var Owner: String)
    data class GmemberData(var ID_member: String, var ID_Group: String, var ID_User: String)
    data class Photoland(var ID_Photo: String,var URL: String,var ID_land: String)
    data class PhotoProperty(var ID_Photo: String,var URL: String,var ID_property: String)
    data class Gannounce(var ID_Groupannouce: String,var ID_Typeannouce: String,var ID_Group: String)
    data class Tannounce(var ID_Type: String,var ID_Typeannouce: String)
    data class amphur(var AMPHUR_ID: String,var AMPHUR_CODE: String,var AMPHUR_NAME: String,var GEO_ID: String,var PROVINCE_ID: String)
    data class district(var DISTRICT_ID: String,var DISTRICT_CODE: String,var DISTRICT_NAME: String,var AMPHUR_ID: String,var PROVINCE_ID: String,var GEO_ID: String)
    data class province(var PROVINCE_ID: String,var PROVINCE_CODE: String,var PROVINCE_NAME: String,var GEO_ID: String)
    data class zipcode(var ZIPCODE_ID: String,var DISTRICT_CODE: String,var PROVINCE_ID: String,var AMPHUR_ID: String,var DISTRICT_ID: String,var ZIPCODE: String)


    companion object{
        var getUser: String? = null
        var getLand: String? = null
        var getPropertys: String? = null
        var getGroup: String? = null
        var getGmember: String? = null
        var getPhotoland: String? = null
        var getPhotoProperty: String? = null
        var getGannounce: String? = null
        var getTannounce: String? = null
        var getAmphur: String? = null
        var getDistrict: String? = null
        var getProvince: String? = null
        var getZipcode: String? = null

    }

    class getData(context: Context, parameter: WorkerParameters) :   Worker(context, parameter){

        //lateinit var Database: Array<String>
        /*lateinit var getUser: String
        lateinit var getLand: String
        lateinit var getPropertys: String
        lateinit var getGroup: String
        lateinit var getGmember: String*/

        override fun doWork(): Result {
            try {
                return when (getData("http://165.22.247.44/Data.php")) {
                    WorkInfo.State.SUCCEEDED -> {
                        /*val output = workDataOf(
                            "Data" to Database
                            "User" to getUser
                            "Land" to getLand,
                            "Propertys" to getPropertys,
                            "Group" to getGroup,
                            "Gmember" to getGmember
                        )*/
                        Result.success()
                    }
                    WorkInfo.State.CANCELLED -> Result.retry()
                    else -> Result.failure()
                }


            } catch (e: Exception) { e.printStackTrace() }
            return Result.failure()
        }



        fun getData(params: String): WorkInfo.State? {

            try {
                Log.d("JSON","Loading")
                val url = URL(params)
                Log.d("JSON", url.toString())

                val urlConnection = url.openConnection()
                val httpURLConnection = urlConnection as HttpURLConnection
                httpURLConnection.allowUserInteraction = false
                httpURLConnection.instanceFollowRedirects = true
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()


                lateinit var jsonObjectData: JSONObject
                val stringBuilder = StringBuilder()


                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = httpURLConnection.inputStream.bufferedReader(StandardCharsets.UTF_8).buffered(50000) as Reader
                    //Database = reader.readLines()
                    Log.d("ReaderData",reader.readText())
                    Log.d("ReaderCap",reader.readText().length.toString() +"com")
                    //if (reader.ready()){
                    for (line in reader.readText()) {
                        stringBuilder.append(line)
                        Log.d("Reader", line.toString())
                    }
                    /*}else{
                        Log.d("Error", "Not have Data")
                        WorkInfo.State.FAILED
                    }*/



                    reader.close()
                    httpURLConnection.disconnect()
                }else{
                    httpURLConnection.disconnect()
                    WorkInfo.State.FAILED
                }

                //val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.ISO_8859_1) as Reader?, 20)


                //inputStream.close()


                //val test = reader
                //Log.d("capacityreder", test.readLine().length.toString())


                jsonObjectData = JSONObject(stringBuilder.toString())
                //Database = (stringBuilder.toString().toShort())
                //Log.d("Hash", Database.toString())
                Log.d("capacity", stringBuilder.capacity().toString())
                Log.d("Data", jsonObjectData.toString())
                Log.d("JSON","Finish")

                getUser = jsonObjectData.getJSONArray("Users").toString()
                getLand = jsonObjectData.getJSONArray("lands").toString()
                getPropertys = jsonObjectData.getJSONArray("propertys").toString()
                getGroup = jsonObjectData.getJSONArray("Groups").toString()
                getGmember = jsonObjectData.getJSONArray("Groupmembers").toString()
                getPhotoland = jsonObjectData.getJSONArray("Photolands").toString()
                getPhotoProperty = jsonObjectData.getJSONArray("Photoproperties").toString()
                getGannounce = jsonObjectData.getJSONArray("Groupannounces").toString()
                getTannounce = jsonObjectData.getJSONArray("Typeannouces").toString()
                getAmphur = jsonObjectData.getJSONArray("amphurs").toString()
                getDistrict = jsonObjectData.getJSONArray("districts").toString()
                getProvince = jsonObjectData.getJSONArray("provinces").toString()
                getZipcode = jsonObjectData.getJSONArray("zipcodes").toString()

                Log.d("User", getUser!!)


                return if(getUser!!.isNotEmpty()&& getLand!!.isNotEmpty()&& getPropertys!!.isNotEmpty()&& getGroup!!.isNotEmpty()&& getGmember!!.isNotEmpty()&& getPhotoland!!.isNotEmpty()&& getPhotoProperty!!.isNotEmpty()&& getGannounce!!.isNotEmpty()&& getTannounce!!.isNotEmpty()  && getAmphur!!.isNotEmpty()&& getDistrict!!.isNotEmpty()&& getProvince!!.isNotEmpty()&& getZipcode!!.isNotEmpty()) WorkInfo.State.SUCCEEDED
                else WorkInfo.State.CANCELLED

            }catch (e: MalformedURLException){
                e.printStackTrace()
                return WorkInfo.State.CANCELLED }
            catch (e: IOException){e.printStackTrace()
                return WorkInfo.State.FAILED}
            catch (e: JSONException){e.printStackTrace()
                return WorkInfo.State.FAILED}
        }
    }

    fun GetUser(json: String): Array<UserData?> {
        val database = JSONArray(json)
        val User: Array<UserData?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            User[i] = UserData(
                    jsonData.getString("ID_User"),
                    jsonData.getString("Firstname"),
                    jsonData.getString("Lastname"),
                    jsonData.getString("Email"),
                    jsonData.getString("Password"),
                    jsonData.getString("Occupation"),
                    jsonData.getString("Age"),
                    jsonData.getString("Gender"),
                    jsonData.getString("Birthday"),
                    jsonData.getString("Location"),
                    jsonData.getString("Phone"),
                    jsonData.getString("profile"),
                    jsonData.getString("created")
            )
            Log.d("JSON", User[i].toString())
        }
        return User
    }

    fun GetLands(json: String): Array<LandData?> {
        val database = JSONArray(json)
        val Data: Array<LandData?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = LandData(jsonData.getString("ID_Lands"),
                    jsonData.getString("ColorType"),
                    jsonData.getString("AnnounceTH"),
                    jsonData.getString("CodeDeed"),
                    jsonData.getString("TypeCode"),
                    jsonData.getString("CodeProperty"),
                    jsonData.getString("SellPrice"),
                    jsonData.getString("Costestimate"),
                    jsonData.getString("CostestimateB"),
                    jsonData.getString("MarketPrice"),
                    jsonData.getString("PriceWA"),
                    jsonData.getString("LandR"),
                    jsonData.getString("LandG"),
                    jsonData.getString("LandWA"),
                    jsonData.getString("Land"),
                    jsonData.getString("Deed"),
                    jsonData.getString("RoadType"),
                    jsonData.getString("RoadWide"),
                    jsonData.getString("GroundLevel"),
                    jsonData.getString("GroundValue"),
                    jsonData.getString("MoreDetails"),
                    jsonData.getString("Latitude"),
                    jsonData.getString("Longitude"),
                    jsonData.getString("AsseStatus"),
                    jsonData.getString("ObservationPoint"),
                    jsonData.getString("Location"),
                    jsonData.getString("LProvince"),
                    jsonData.getString("LAmphur"),
                    jsonData.getString("LDistrict"),
                    jsonData.getString("LZipCode"),
                    jsonData.getString("ContactUo"),
                    jsonData.getString("ContactSo"),
                    jsonData.getString("ContactUt"),
                    jsonData.getString("ContactSt"),
                    jsonData.getString("ContactU"),
                    jsonData.getString("ContactS"),
                    jsonData.getString("Created"),
                    jsonData.getString("Place"),
                    jsonData.getString("Blind"),
                    jsonData.getString("Neareducation"),
                    jsonData.getString("Cenmarket"),
                    jsonData.getString("Market"),
                    jsonData.getString("River"),
                    jsonData.getString("Mainroad"),
                    jsonData.getString("Insoi"),
                    jsonData.getString("Letc"),
                    jsonData.getString("WxD"),
                    jsonData.getString("LandAge"),
                    jsonData.getString("PPStatus"),
                    jsonData.getString("Owner") )
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun Getpropertys(json: String): Array<propertysData?> {
        val database = JSONArray(json)
        val Data: Array<propertysData?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = propertysData(jsonData.getString("ID_Property"),
                    jsonData.getString("PropertyType"),
                    jsonData.getString("AnnounceTH"),
                    jsonData.getString("CodeDeed"),
                    jsonData.getString("SellPrice"),
                    jsonData.getString("Costestimate"),
                    jsonData.getString("CostestimateB"),
                    jsonData.getString("MarketPrice"),
                    jsonData.getString("BathRoom"),
                    jsonData.getString("BedRoom"),
                    jsonData.getString("CarPark"),
                    jsonData.getString("HouseArea"),
                    jsonData.getString("Floor"),
                    jsonData.getString("LandR"),
                    jsonData.getString("LandG"),
                    jsonData.getString("LandWA"),
                    jsonData.getString("LandU"),
                    jsonData.getString("HomeCondition"),
                    jsonData.getString("BuildingAge"),
                    jsonData.getString("BuildFD"),
                    jsonData.getString("BuildFM"),
                    jsonData.getString("BuildFY"),
                    jsonData.getString("Directions"),
                    jsonData.getString("RoadType"),
                    jsonData.getString("RoadWide"),
                    jsonData.getString("GroundLevel"),
                    jsonData.getString("GroundValue"),
                    jsonData.getString("MoreDetails"),
                    jsonData.getString("Latitude"),
                    jsonData.getString("Longitude"),
                    jsonData.getString("AsseStatus"),
                    jsonData.getString("ObservationPoint"),
                    jsonData.getString("Location"),
                    jsonData.getString("LProvince"),
                    jsonData.getString("LAmphur"),
                    jsonData.getString("LDistrict"),
                    jsonData.getString("LZipCode"),
                    jsonData.getString("ContactUo"),
                    jsonData.getString("ContactSo"),
                    jsonData.getString("ContactUt"),
                    jsonData.getString("ContactSt"),
                    jsonData.getString("ContactU"),
                    jsonData.getString("ContactS"),
                    jsonData.getString("Created"),
                    jsonData.getString("Blind"),
                    jsonData.getString("Neareducation"),
                    jsonData.getString("Cenmarket"),
                    jsonData.getString("Market"),
                    jsonData.getString("River"),
                    jsonData.getString("Mainroad"),
                    jsonData.getString("Insoi"),
                    jsonData.getString("Letc"),
                    jsonData.getString("airconditioner"),
                    jsonData.getString("afan"),
                    jsonData.getString("AirPurifier"),
                    jsonData.getString("Waterheater"),
                    jsonData.getString("WIFI"),
                    jsonData.getString("TV"),
                    jsonData.getString("refrigerator"),
                    jsonData.getString("microwave"),
                    jsonData.getString("gasstove"),
                    jsonData.getString("wardrobe"),
                    jsonData.getString("TCset"),
                    jsonData.getString("sofa"),
                    jsonData.getString("shelves"),
                    jsonData.getString("CCTV"),
                    jsonData.getString("Securityguard"),
                    jsonData.getString("pool"),
                    jsonData.getString("Fitness"),
                    jsonData.getString("Publicarea"),
                    jsonData.getString("ShuttleBus"),
                    jsonData.getString("WVmachine"),
                    jsonData.getString("CWmachine"),
                    jsonData.getString("Elevator"),
                    jsonData.getString("Lobby"),
                    jsonData.getString("ATM"),
                    jsonData.getString("BeautySalon"),
                    jsonData.getString("Balcony"),
                    jsonData.getString("EventR"),
                    jsonData.getString("MeetingR"),
                    jsonData.getString("LivingR"),
                    jsonData.getString("Hairsalon"),
                    jsonData.getString("Laundry"),
                    jsonData.getString("Store"),
                    jsonData.getString("Supermarket"),
                    jsonData.getString("CStore"),
                    jsonData.getString("Mfee"),
                    jsonData.getString("Kitchen"),
                    jsonData.getString("LandAge"),
                    jsonData.getString("PPStatus"),
                    jsonData.getString("Owner"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetGroup(json: String): Array<GroupData?>{
        val database = JSONArray(json)
        val Data: Array<GroupData?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = GroupData(jsonData.getString("ID_Group") ,jsonData.getString("Name") ,jsonData.getString("Img") ,jsonData.getString("Owner"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetMenber(json: String): Array<GmemberData?>{
        val database = JSONArray(json)
        val Data: Array<GmemberData?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            Data[i] = GmemberData(jsonData.getString("ID_member") ,jsonData.getString("ID_Group") ,jsonData.getString("ID_User"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetPhotoland(json: String): Array<Photoland?>{
        val database = JSONArray(json)
        val Data: Array<Photoland?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            Data[i] = Photoland(jsonData.getString("ID_Photo"),
                    jsonData.getString("URL"),
                    jsonData.getString("ID_land"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetPhotoproperty(json: String): Array<PhotoProperty?>{
        val database = JSONArray(json)
        val Data: Array<PhotoProperty?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            Data[i] = PhotoProperty(jsonData.getString("ID_Photo"),
                    jsonData.getString("URL"),
                    jsonData.getString("ID_property"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetGannounce(json: String): Array<Gannounce?>{
        val database = JSONArray(json)
        val Data: Array<Gannounce?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            Data[i] = Gannounce(jsonData.getString("ID_Groupannouce"),
                    jsonData.getString("ID_Typeannouce"),
                    jsonData.getString("ID_Group"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetTannounce(json: String): Array<Tannounce?>{
        val database = JSONArray(json)
        val Data: Array<Tannounce?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()) {
            val jsonData = database.getJSONObject(i)
            Data[i] = Tannounce(jsonData.getString("ID_Type"),
                    jsonData.getString("ID_Typeannouce"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetAmphur(json: String): Array<amphur?>{
        val database = JSONArray(json)
        //Log.d("JSON", json)
        val Data: Array<amphur?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = amphur(jsonData.getString("Amphur_ID"),
                    jsonData.getString("Amphur_code"),
                    jsonData.getString("Amphur_name"),
                    jsonData.getString("GEO_ID"),
                    jsonData.getString("PROVINCE_ID"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetDistrict(json: String): Array<district?>{
        val database = JSONArray(json)
        //Log.d("JSON", json)
        val Data: Array<district?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = district(jsonData.getString("District_ID"),
                    jsonData.getString("DISTRICT_CODE"),
                    jsonData.getString("DISTRICT_NAME"),
                    jsonData.getString("AMPHUR_ID"),
                    jsonData.getString("PROVINCE_ID"),
                    jsonData.getString("GEO_ID"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetProvince(json: String): Array<province?>{
        val database = JSONArray(json)
        //Log.d("JSON", json)
        val Data: Array<province?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = province(jsonData.getString("PROVINCE_ID"),
                    jsonData.getString("PROVINCE_CODE"),
                    jsonData.getString("PROVINCE_NAME"),
                    jsonData.getString("GEO_ID"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }

    fun GetZipcode(json: String): Array<zipcode?>{
        val database = JSONArray(json)
        //Log.d("JSON", json)
        val Data: Array<zipcode?> = arrayOfNulls(database.length())
        for (i in 0 until database.length()){
            val jsonData = database.getJSONObject(i)
            Data[i] = zipcode(jsonData.getString("ZIPCODE_ID"),
                    jsonData.getString("DISTRICT_CODE"),
                    jsonData.getString("PROVINCE_ID"),
                    jsonData.getString("AMPHUR_ID"),
                    jsonData.getString("DISTRICT_ID"),
                    jsonData.getString("ZIPCODE"))
            Log.d("JSON", Data[i].toString())
        }
        return Data
    }



    class Uploaddata : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String): String? {
            try {
                Log.d("JSON","Loading")
                val url = URL(params[0])
                Log.d("JSON", url.toString())

                val urlConnection = url.openConnection()
                val httpURLConnection = urlConnection as HttpURLConnection
                httpURLConnection.allowUserInteraction = false
                httpURLConnection.instanceFollowRedirects = true
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()


                var inputStream: InputStream? = null

                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.inputStream

                val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.ISO_8859_1), 20)
                val stringBuilder = StringBuilder()


                for (line in reader.readLine())
                    stringBuilder.append(line)


                inputStream!!.close()
                Log.d("JSON", stringBuilder.toString())


            }catch (e: MalformedURLException){e.printStackTrace()}

            return null
        }


    }



    fun NewGroup(Name: String,Img: String,ID_User: String){
        //http://165.22.247.44/insert_group.php?name=$Name&img=$Img&owner=$ID_User
    }


    fun insertGroup(ID_Group: String , ID_User: String){
        Uploaddata().execute("http://landvist.xyz/Upload/edit_member.php?choice=0&group=$ID_Group&user=$ID_User")
    }

    fun insertland( color: String, announceTH: String, codedeed: String, sellprice: String, costestimate: String, marketprice: String, pricePM: String, landR: String, landG: String, landWA: String, land: String, deed: String, roadtype: String, roadwide: String, groundlevel: String, groundvalue: String, moredetails: String, latitude: String, longitude: String, assestatus: String, observationpoint: String, location: String, Lprovince: String, Lamphur: String, Ldistrict: String, Lzipcode: String, contactU: String, contactE: String, contactP: String, contactL: String, created: String, place: String, blind: String, neareducation: String, cenmarket: String, market: String, river: String, mainroaad: String, insoi: String, letc: String, owner: String){
        Uploaddata().execute("http://landvist.xyz/Upload/Insert_land.php?color=$color&announceTH=$announceTH&codedeed=$codedeed&sellprice=$sellprice&costestimate=$costestimate&marketprice=$marketprice&pricePM=$pricePM&landR=$landR&landG=$landG&landWA=$landWA&increase_property=$land&deed=$deed&roadtype=$roadtype&roadwide=$roadwide&groundlevel=$groundlevel&groundvalue=$groundvalue&moredetails=$moredetails&latitude=$latitude&longitude=$longitude&assestatus=$assestatus&observationpoint=$observationpoint&location=$location&Lprovince=$Lprovince&Lamphur=$Lamphur&Ldistrict=$Ldistrict&Lzipcode=$Lzipcode&contactU=$contactU&contactE=$contactE&contactP=$contactP&contactL=$contactL&created=$created&place=$place&blind=$blind&neareducation=$neareducation&cenmarket=$cenmarket&market=$market&river=$river&mainroaad=$mainroaad&insoi=$insoi&letc=$letc&owner=$owner")
    }



    fun send(email: String,Name: String,Surname: String, Password: String, birthday: String, location: String, phone: String, profile: String){
        Uploaddata().execute("http://landvist.xyz/Upload/signup.php?email=$email&firstname=$Name&lastname=$Surname&password=$Password&birthday=$birthday&location=$location&phone=$phone&profile=$profile")
    }



}