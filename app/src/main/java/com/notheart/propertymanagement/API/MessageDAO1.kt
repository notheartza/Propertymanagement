package com.notheart.propertymanagement.API;

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class MessageDAO1 {

    data class Announceinfo1(
            @SerializedName("ID_Property") val ID_Property: String,
            @SerializedName("PropertyType") val PropertyType: String,
            @SerializedName("AnnounceTH") val AnnounceTH: String,
            @SerializedName("CodeDeed") val CodeDeed: String,
            @SerializedName("SellPrice") val SellPrice: String,
            @SerializedName("Costestimate") val Costestimate: String,
            @SerializedName("CostestimateB") val CostestimateB: String,
            @SerializedName("MarketPrice") val MarketPrice: String,
            @SerializedName("BathRoom") val BathRoom: String,
            @SerializedName("BedRoom") val BedRoom: String,
            @SerializedName("CarPark") val CarPark: String,
            @SerializedName("HouseArea") val HouseArea: String,
            @SerializedName("Floor") val Floor: String,
            @SerializedName("LandR") val LandR: String,
            @SerializedName("LandG") val LandG: String,
            @SerializedName("LandWA") val LandWA: String,
            @SerializedName("LandU") val LandU: String,
            @SerializedName("HomeCondition") val HomeCondition: String,
            @SerializedName("BuildingAge") val BuildingAge: String,
            @SerializedName("BuildFD") val BuildFD: String,
            @SerializedName("BuildFM") val BuildFM: String,
            @SerializedName("BuildFY") val BuildFY: String,
            @SerializedName("Directions") val Directions: String,
            @SerializedName("RoadType") val RoadType: String,
            @SerializedName("RoadWide") val RoadWide: String,
            @SerializedName("GroundLevel") val GroundLevel: String,
            @SerializedName("GroundValue") val GroundValue: String,
            @SerializedName("MoreDetails") val MoreDetails: String,
            @SerializedName("Latitude") val Latitude: Double,
            @SerializedName("Longitude") val Longitude: Double,
            @SerializedName("AsseStatus") val AsseStatus: String,
            @SerializedName("ObservationPoint") val ObservationPoint: String,
            @SerializedName("Location") val Location: String,
            @SerializedName("LProvince") val LProvince: String,
            @SerializedName("LAmphur") val LAmphur: String,
            @SerializedName("LDistrict") val LDistrict: String,
            @SerializedName("LZipCode") val LZipCode: String,
            @SerializedName("ContactUo") val ContactUo: String,
            @SerializedName("ContactSo") val ContactSo: String,
            @SerializedName("ContactUt") val ContactUt: String,
            @SerializedName("ContactSt") val ContactSt: String,
            @SerializedName("ContactU") val ContactU: String,
            @SerializedName("ContactS") val ContactS: String,
            @SerializedName("Blind") val Blind: String,
            @SerializedName("Neareducation") val Neareducation: String,
            @SerializedName("Cenmarket") val Cenmarket: String,
            @SerializedName("Market") val Market: String,
            @SerializedName("River") val River: String,
            @SerializedName("Mainroad") val Mainroad: String,
            @SerializedName("Insoi") val Insoi: String,
            @SerializedName("Letc") val Letc: String,
            @SerializedName("airconditioner") val airconditioner: String,
            @SerializedName("afan") val afan: String,
            @SerializedName("AirPurifier") val AirPurifier: String,
            @SerializedName("Waterheater") val Waterheater: String,
            @SerializedName("WIFI") val WIFI: String,
            @SerializedName("TV") val TV: String,
            @SerializedName("refrigerator") val refrigerator: String,
            @SerializedName("microwave") val microwave: String,
            @SerializedName("gasstove") val gasstove: String,
            @SerializedName("wardrobe") val wardrobe: String,
            @SerializedName("TCset") val TCset: String,
            @SerializedName("sofa") val sofa: String,
            @SerializedName("shelves") val shelves: String,
            @SerializedName("CCTV") val CCTV: String,
            @SerializedName("Securityguard") val Securityguard: String,
            @SerializedName("pool") val pool: String,
            @SerializedName("Fitness") val Fitness: String,
            @SerializedName("Publicarea") val Publicarea: String,
            @SerializedName("ShuttleBus") val ShuttleBus: String,
            @SerializedName("WVmachine") val WVmachine: String,
            @SerializedName("CWmachine") val CWmachine: String,
            @SerializedName("Elevator") val Elevator: String,
            @SerializedName("Lobby") val Lobby: String,
            @SerializedName("ATM") val ATM: String,
            @SerializedName("BeautySalon") val BeautySalon: String,
            @SerializedName("Balcony") val Balcony: String,
            @SerializedName("EventR") val EventR: String,
            @SerializedName("MeetingR") val MeetingR: String,
            @SerializedName("LivingR") val LivingR: String,
            @SerializedName("Hairsalon") val Hairsalon: String,
            @SerializedName("Laundry") val Laundry: String,
            @SerializedName("Store") val Store: String,
            @SerializedName("Supermarket") val Supermarket: String,
            @SerializedName("CStore") val CStore: String,
            @SerializedName("Mfee") val Mfee: String,
            @SerializedName("Kitchen") val Kitchen: String,
            @SerializedName("LandAge") val LandAge: String,
            @SerializedName("Created") val Created: String,
            @SerializedName("PPStatus") val PPStatus: String,
            @SerializedName("ImageEX") val ImageEX: String,
            @SerializedName("Owner") val Owner: String,
            val Photo: List<MessageDAO.Photo>
    )

    @SerializedName("Property") @Expose private lateinit var property: List<Announceinfo1>

    fun getproperty(): List<Announceinfo1>{
        return property
    }

    data class Announceinfo2(
            @SerializedName("ID_Lands") val ID_Lands: String,
            @SerializedName("ColorType") val ColorType: String,
            @SerializedName("AnnounceTH") val AnnounceTH: String,
            @SerializedName("CodeDeed") val CodeDeed: String,
            @SerializedName("TypeCode") val TypeCode: String,
            @SerializedName("CodeProperty") val CodeProperty: String,
            @SerializedName("SellPrice") val SellPrice: String,
            @SerializedName("Costestimate") val Costestimate: String,
            @SerializedName("CostestimateB") val CostestimateB: String,
            @SerializedName("MarketPrice") val MarketPrice: String,
            @SerializedName("PriceWA") val PriceWA: String,
            @SerializedName("LandR") val LandR: String,
            @SerializedName("LandG") val LandG: String,
            @SerializedName("LandWA") val LandWA: String,
            @SerializedName("Land") val Land: String,
            @SerializedName("Deed") val Deed: String,
            @SerializedName("RoadType") val RoadType: String,
            @SerializedName("RoadWide") val RoadWide: String,
            @SerializedName("GroundLevel") val GroundLevel: String,
            @SerializedName("GroundValue") val GroundValue: String,
            @SerializedName("MoreDetails") val MoreDetails: String,
            @SerializedName("Latitude") val Latitude: Double,
            @SerializedName("Longitude") val Longitude: Double,
            @SerializedName("AsseStatus") val AsseStatus: String,
            @SerializedName("ObservationPoint") val ObservationPoint: String,
            @SerializedName("Location") val Location: String,
            @SerializedName("LProvince") val LProvince: String,
            @SerializedName("LAmphur") val LAmphur: String,
            @SerializedName("LDistrict") val LDistrict: String,
            @SerializedName("LZipCode") val LZipCode: String,
            @SerializedName("ContactUo") val ContactUo: String,
            @SerializedName("ContactSo") val ContactSo: String,
            @SerializedName("ContactUt") val ContactUt: String,
            @SerializedName("ContactSt") val ContactSt: String,
            @SerializedName("ContactU") val ContactU: String,
            @SerializedName("ContactS") val ContactS: String,
            @SerializedName("Place") val Place: String,
            @SerializedName("Blind") val Blind: String,
            @SerializedName("Neareducation") val Neareducation: String,
            @SerializedName("Cenmarket") val Cenmarket: String,
            @SerializedName("Market") val Market: String,
            @SerializedName("River") val River: String,
            @SerializedName("Mainroad") val Mainroad: String,
            @SerializedName("Insoi") val Insoi: String,
            @SerializedName("Letc") val Letc: String,
            @SerializedName("WxD") val WxD: String,
            @SerializedName("LandAge") val LandAge: String,
            @SerializedName("PPStatus") val PPStatus: String,
            @SerializedName("ImageEX") val ImageEX: String,
            @SerializedName("Owner") val Owner: String,
            @SerializedName("Created") val Created: String,
            val Photo: List<MessageDAO.Photo>
    )

    @SerializedName("Land") @Expose private lateinit var Land: List<Announceinfo2>

    fun getLand(): List<Announceinfo2>{
        return Land
    }

        data class Folder(
                @SerializedName("ID_Folder") val ID_Folder: String,
                @SerializedName("NameF") val NameF: String,
                @SerializedName("ID_Group") val ID_Group: String,
                @SerializedName("Created") val Created:String,
                @SerializedName("Announce") val Announce: List<MessageDAO.Announce>
        )

        @SerializedName("Group") @Expose lateinit var Folders: List<Folder>

        fun getFolader(): List<Folder>{
                return Folders
        }

}
