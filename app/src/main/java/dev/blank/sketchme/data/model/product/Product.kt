package dev.blank.sketchme.data.model.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

open class Product : Parcelable {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("descriptionList")
    var descriptionList: List<Int?>? = null

    @SerializedName("image")
    var image: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        name = `in`.readString()
        if (`in`.readByte().toInt() == 0x01) {
            descriptionList = ArrayList()
            `in`.readList(descriptionList as ArrayList<Int?>, Int::class.java.classLoader)
        } else {
            descriptionList = null
        }
        image = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        if (descriptionList == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(descriptionList)
        }
        dest.writeString(image)
    }

    companion object {
        const val SIZE_STATUS = 0
        const val BACKGROUND_STATUS = 1
        const val CONTOUR_STATUS = 2
        val SIZE = arrayOf("Half Body", "Full Body")
        val BACKGROUND = arrayOf("Reguler", "Custom")
        val CONTOUR = arrayOf("Reguler", "Custom")
        val BACKGROUND_FULL = arrayOf("Reguler Background", "Custom Background")
        val CONTOUR_FULL = arrayOf("Reguler Contour", "Custom Contour")
        var HALF_BODY = 0
        var FULL_BODY = 1
        var REGULER_BACKGROUND = 0
        var CUSTOM_BACKGROUND = 1
        var REGULER_CONTOUR = 0
        var CUSTOM_CONTOUR = 1
        val CREATOR: Parcelable.Creator<Product?> = object : Parcelable.Creator<Product?> {
            override fun createFromParcel(`in`: Parcel): Product? {
                return Product(`in`)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }
}