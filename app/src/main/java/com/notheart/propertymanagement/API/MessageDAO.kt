package com.notheart.propertymanagement.API

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageDAO {

    data class Upload_path(
        @SerializedName("Name") val Name_Photo: String,
        @SerializedName("imagepath") val imagePath: String

    )


    @SerializedName("Photo") @Expose private lateinit var imagepath: List<Upload_path>
    fun getImagepath(): List<Upload_path> {
        return imagepath
    }

    data class User(
        @SerializedName("ID_User") val ID_User: String,
        @SerializedName("Email") val Email: String,
        @SerializedName("Firstname") val Firstname: String,
        @SerializedName("Lastname") val Lastname: String,
        @SerializedName("Occupation") val Occupation: String,
        @SerializedName("Age") val Age: String,
        @SerializedName("Gender") val Gender: String,
        @SerializedName("Birthday") val Birthday: String,
        @SerializedName("LocationU") val LocationU: String,
        @SerializedName("Phone") val Phone: String,
        @SerializedName("ProfileImg") val ProfileImg: String,
        @SerializedName("Created") val Created: String
    )


        @SerializedName("Status")@Expose private lateinit var Status: String
        @SerializedName("User")@Expose private lateinit var Login: List<User>



    fun Status(): String {
        return Status
    }


    fun getLogin(): List<User> {
        return Login
    }

    data class Photo(
        @SerializedName("ID_Photo") val ID_Photo: String,
        @SerializedName("URL") val URL: String,
        @SerializedName("ID_property") val ID_property: String
    )

    data class Announce(
        @SerializedName("ID_Announce") val ID_Announce: String,
        @SerializedName("AnnounceTH") val AnnounceTH: String,
        @SerializedName("PPStatus") val PPStatus:String,
        @SerializedName("MarketPrice") val MarketPrice:String,
        @SerializedName("Created") val Created:String,
        @SerializedName("Owner") val Owner:String,
        val Photo: List<Photo>
    )
    @SerializedName("Announce")@Expose private lateinit var Announces: List<Announce>

    @SerializedName("Search")@Expose private lateinit var Searchs: List<Announce>

    fun getAnnounce(): List<Announce>{
        return Announces
    }

    fun getSearch(): List<Announce>{
        return Searchs
    }

    data class Group(
        @SerializedName("ID_Group") val ID_Group: String,
        @SerializedName("NameG") val Name: String,
        @SerializedName("Img") val Img: String,
        @SerializedName("created") val created: String,
        @SerializedName("Owner") val Owner: String,
        @SerializedName("Members") val Members: List<Member>,
        @SerializedName("Folders") val Folders: List<Folder>
    )

    data class Folder(
        @SerializedName("ID_Folder") val ID_Folder: String,
        @SerializedName("NameF") val NameF: String,
        @SerializedName("ID_Group") val ID_Group: String,
        @SerializedName("Created") val Created:String
    )

    data class Member(
        @SerializedName("ID_member") val ID_member: String,
        @SerializedName("ID_Group") val ID_Group: String,
        @SerializedName("ID_User") val ID_User: String
    )

    @SerializedName("Group") @Expose private lateinit var Groups: List<Group>


    fun getGroup(): List<Group>{
        return Groups
    }

    data class Province(
        @SerializedName("PROVINCE_ID") val PROVINCE_ID: String,
        @SerializedName("PROVINCE_NAME") val PROVINCE_NAME: String
    )

    @SerializedName("Province") @Expose private lateinit var Provinces: List<Province>

    fun getProvince(): List<Province>{
        return Provinces
    }

    data class Amphur(
        @SerializedName("AMPHUR_ID") val AMPHUR_ID: String,
        @SerializedName("AMPHUR_NAME") val AMPHUR_NAME: String
    )

    @SerializedName("Amphurs") @Expose private lateinit var Amphurs: List<Amphur>

    fun getAmphur(): List<Amphur>{
        return Amphurs
    }

    data class District(
        @SerializedName("DISTRICT_ID") val DISTRICT_ID: String,
        @SerializedName("DISTRICT_NAME") val DISTRICT_NAME: String
    )

    @SerializedName("Districts") @Expose private lateinit var Districts: List<District>

    fun getDistrict(): List<District>{
        return Districts
    }

    data class Zipcode(
        @SerializedName("ZIPCODE_ID") val ZIPCODE_ID: String,
        @SerializedName("ZIPCODE") val ZIPCODE: String
    )

    @SerializedName("Zipcodes") @Expose private lateinit var Zipcodes: List<Zipcode>

    fun getZipcode(): List<Zipcode>{
        return Zipcodes
    }

    data class Contact(
        @SerializedName("ID_Contact") val ID_Contact: String,
        @SerializedName("Name") val Name: String,
        @SerializedName("Email") val Email: String,
        @SerializedName("Phone") val Phone: String,
        @SerializedName("Line") val Line : String
    )

    @SerializedName("Contact") @Expose private lateinit var Contacts: List<Contact>

    fun getContact(): List<Contact>{
        return Contacts
    }

    data class Insert_Announce(
        @SerializedName("ID_Announce") val ID_Announce: String,
        @SerializedName("Sql") val Sql: String
    )

    @SerializedName("Insert_Announce") @Expose private lateinit var Insert_Announces: List<Insert_Announce>

    fun getInsert_Announce(): List<Insert_Announce>{
        return Insert_Announces
    }



}