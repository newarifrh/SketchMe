package dev.blank.sketchme.data.model.product

import com.google.gson.annotations.SerializedName

class ProductResponse {
    @SerializedName("data")
    var productList: List<Product>? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null
}