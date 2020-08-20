package dev.blank.sketchme.data.remote

import dev.blank.sketchme.data.model.product.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("{version}/product.json")
    fun listProduct(@Path("version") version: String?): Call<ProductResponse?>
}