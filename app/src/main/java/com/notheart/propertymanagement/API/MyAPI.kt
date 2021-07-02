package com.notheart.propertymanagement.API



import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MyAPI {

        @Multipart
        @POST("/Upload/upload_image.php")
        fun updateImageProfile(
                @Part("pro_image\"; filename=\"image.jpg\" ") picture: RequestBody): Call<MessageDAO>


        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Data/Login")
        fun checkLogin(@Field("Email") email: String, @Field("Password") password: String): Call<MessageDAO>

        @FormUrlEncoded
        @POST("/apipmb/Data/AllAnnounce")
        fun Announce(@Field("ID")ID: String): Call<MessageDAO>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Data/Announce")
        fun FindAnnounce(@Field("ID_Announce") ID_Announce:String): Call<MessageDAO1>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Data/Search")
        fun Search(@Field("ID") ID_User: String,@Field("Word") Word: String): Call<MessageDAO>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Data/AdvanceSearch")
        fun AdvenceSearch(@Field("ID") ID: String, @Field("PPStatus") PPStatus: String, @Field("Type") Type:String, @Field("Word") Word: String, @Field("MinPrice") MinPrice: String, @Field("MaxPrice") MaxPrice: String): Call<MessageDAO>


        @GET("/apipmb/Data/Group/{id}")
        fun AllGroup(@Path("id") ID: String): Call<MessageDAO>

        @GET("/apipmb/Group/AllGroup/{id}")
        fun Group(@Path("id") ID: String): Call<MessageDAO>

        @GET("/apipmb/Group/findFolder/{id}")
        fun Folder(@Path("id") ID: String): Call<MessageDAO1>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Group/AddGroup")
        fun AddGroup(@Field("Name")Name: String, @Field("Img")Img: String,@Field("Created")Created: String,@Field("ID")ID_User: String): Call<MessageDAO>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Group/AddMemberInGroup")
        fun AddMenverInGroup(@Field("ID_Group")ID_Group: String,@Field("ID_User")ID_User: String): Call<MessageDAO>

        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Group/AddFolderInGroup")
        fun AddFolderInGroup(@Field("ID_Group")ID_Group: String,@Field("NameF")NameF :String,@Field("ID_User")ID_User: String): Call<MessageDAO>


        @FormUrlEncoded
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("/apipmb/Group/AddPropertyInFolder")
        fun AddPropertyInFolder(@Field("ID_Property")ID_Property: String,@Field("ID_Folder")ID_Folder: String,@Field("Created")Created: String): Call<MessageDAO>

        @GET("/apipmb/Data/User/{id}")
        fun FindUser(@Path("id") ID: String): Call<MessageDAO>

        @GET("apipmb/Data/Province")
        fun Province(): Call<MessageDAO>

        @GET("apipmb/Data/Amphurs/{id}")
        fun Amphurs(@Path("id")ID: String): Call<MessageDAO>

        @GET("apipmb/Data/Districts/{id}")
        fun District1(@Path("id")ID: String): Call<MessageDAO>

        @GET("apipmb/Data/Zipcodes/{id}")
        fun Zipcode1(@Path("id")ID: String): Call<MessageDAO>

        @GET("apipmb/Data/Contact/{id}")
        fun Contact(@Path("id")ID: String): Call<MessageDAO>

        @GET("apipmb/Data/FindContact/{id}")
        fun findContact(@Path("id")ID: String): Call<MessageDAO>


        @GET("apipmb/Data/AnnounceP/{id}/{id1}")
        fun Insert_Photo(@Path("id") ID_Announce: String,@Path("id1") Name: String): Call<MessageDAO>




        @GET("/Upload/signup.php")
        fun Singup(@Query("email")email: String,@Query("firstname")firstname: String,@Query("lastname")lastname: String,@Query("password")password: String,@Query("birthday")birthday: String,@Query("location")location: String,@Query("phone")phone: String,@Query("profile")profile: String,@Query("created")created: String): Call<MessageDAO>

        @GET("/Upload/Insert_protertys.php")
        fun InsertProperty(@Query("PropertyType")PropertyType: String,@Query("AnnounceTH")AnnounceTH: String,@Query("CodeDeed")CodeDeed: String,@Query("SellPrice")SellPrice: String,@Query("Costestimate")Costestimate: String,@Query("CostestimateB")CostestimateB: String,@Query("MarketPrice")MarketPrice: String,@Query("BathRoom")BathRoom: String,@Query("BedRoom")BedRoom: String,@Query("CarPark")CarPark: String,@Query("HouseArea")HouseArea: String,@Query("Floor")Floor: String,@Query("LandR")LandR: String,@Query("LandG")LandG: String,@Query("LandWA")LandWA: String,@Query("LandU")LandU: String,@Query("HomeCondition")HomeCondition: String,@Query("BuildingAge")BuildingAge: String,@Query("BuildFD")BuildFD: String,@Query("BuildFM")BuildFM: String,@Query("BuildFY")BuildFY: String,@Query("Directions")Directions: String,@Query("RoadType")RoadType: String,@Query("RoadWide")RoadWide: String,@Query("GroundLevel")GroundLevel: String,@Query("GroundValue")GroundValue: String,@Query("MoreDetails")MoreDetails: String,@Query("Latitude")Latitude: String,@Query("Longitude")Longitude: String,@Query("AsseStatus")AsseStatus: String,@Query("ObservationPoint")ObservationPoint: String,@Query("Location")Location: String,@Query("LProvince")LProvince: String,@Query("LAmphur")LAmphur: String,@Query("LDistrict")LDistrict: String,@Query("LZipCode")LZipCode: String,@Query("ContactUo")ContactUo: String,@Query("ContactSo")ContactSo: String,@Query("ContactUt")ContactUt: String,@Query("ContactSt")ContactSt: String,@Query("ContactU")ContactU: String,@Query("ContactS")ContactS: String,@Query("Created")Created: String,@Query("Blind")Blind: String,@Query("Neareducation")Neareducation: String,@Query("Cenmarket")Cenmarket: String,@Query("Market")Market: String,@Query("River")River: String,@Query("Mainroad")Mainroad: String,@Query("Insoi")Insoi: String,@Query("Letc")Letc: String,@Query("airconditioner")airconditioner: String,@Query("afan")afan: String,@Query("AirPurifier")AirPurifier: String,@Query("Waterheater")Waterheater: String,@Query("WIFI")WIFI: String,@Query("TV")TV: String,@Query("refrigerator")refrigerator: String,@Query("microwave")microwave: String,@Query("gasstove")gasstove: String,@Query("wardrobe")wardrobe: String,@Query("TCset")TCset: String,@Query("sofa")sofa: String,@Query("shelves")shelves: String,@Query("CCTV")CCTV: String,@Query("Securityguard")Securityguard: String,@Query("pool")pool: String,@Query("Fitness")Fitness: String,@Query("Publicarea")Publicarea: String,@Query("ShuttleBus")ShuttleBus: String,@Query("WVmachine")WVmachine: String,@Query("CWmachine")CWmachine: String,@Query("Elevator")Elevator: String,@Query("Lobby")Lobby: String,@Query("ATM")ATM: String,@Query("BeautySalon")BeautySalon: String,@Query("Balcony")Balcony: String,@Query("EventR")EventR: String,@Query("MeetingR")MeetingR: String,@Query("LivingR")LivingR: String,@Query("Hairsalon")Hairsalon: String,@Query("Laundry")Laundry: String,@Query("Store")Store: String,@Query("Supermarket")Supermarket: String,@Query("CStore")CStore: String,@Query("Mfee")Mfee: String,@Query("Kitchen")Kitchen: String,@Query("LandAge")LandAge: String,@Query("PPStatus")PPStatus: String,@Query("Owner")Owner: String): Call<MessageDAO>

        @GET("/Upload/Insert_land.php")
        fun InsertLand(@Query("ColorType") ColorType: String,@Query("AnnounceTH") AnnounceTH: String,@Query("CodeDeed") CodeDeed: String,@Query("TypeCode") TypeCode: String,@Query("CodeProperty") CodeProperty: String,@Query("SellPrice") SellPrice: String,@Query("Costestimate") Costestimate: String,@Query("CostestimateB") CostestimateB: String,@Query("MarketPrice") MarketPrice: String,@Query("PriceWA") PriceWA: String,@Query("LandR") LandR: String,@Query("LandG") LandG: String,@Query("LandWA") LandWA: String,@Query("Land") Land: String,@Query("Deed") Deed: String,@Query("RoadType") RoadType: String,@Query("RoadWide") RoadWide: String,@Query("GroundLevel") GroundLevel: String,@Query("GroundValue") GroundValue: String,@Query("MoreDetails") MoreDetails: String,@Query("Latitude") Latitude: String,@Query("Longitude") Longitude: String,@Query("AsseStatus") AsseStatus: String,@Query("ObservationPoint") ObservationPoint: String,@Query("Location") Location: String,@Query("LProvince") LProvince: String,@Query("LAmphur") LAmphur: String,@Query("LDistrict") LDistrict: String,@Query("LZipCode") LZipCode: String,@Query("ContactUo") ContactUo: String,@Query("ContactSo") ContactSo: String,@Query("ContactUt") ContactUt: String,@Query("ContactSt") ContactSt: String,@Query("ContactU") ContactU: String,@Query("ContactS") ContactS: String,@Query("Place") Place: String,@Query("Blind") Blind: String,@Query("Neareducation") Neareducation: String,@Query("Cenmarket") Cenmarket: String,@Query("Market") Market: String,@Query("River") River: String,@Query("Mainroad") Mainroad: String,@Query("Insoi") Insoi: String,@Query("Letc") Letc: String,@Query("WxD") WxD: String,@Query("LandAge") LandAge: String,@Query("PPStatus") PPStatus: String,@Query("Owner") Owner: String,@Query("Created") Created: String): Call<MessageDAO>


        @GET("/Upload/Insert_Contact.php")
        fun InsertContact(@Query("name")name: String,@Query("email")email: String,@Query("phone")phone: String,@Query("line")line: String,@Query("owner")owner: String): Call<MessageDAO>






}


