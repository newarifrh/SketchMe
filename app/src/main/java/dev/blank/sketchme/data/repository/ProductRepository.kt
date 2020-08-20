package dev.blank.sketchme.data.repository

import androidx.lifecycle.MutableLiveData
import dev.blank.sketchme.data.APIClient
import dev.blank.sketchme.data.model.product.Product
import dev.blank.sketchme.data.model.product.ProductResponse
import dev.blank.sketchme.data.remote.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val mProductService: ProductService) {
    var listProductMutableLiveData = MutableLiveData<List<Product?>?>()
    val news: MutableLiveData<List<Product?>?>
        get() {
            mProductService.listProduct(APIClient.VERSION_1).enqueue(object : Callback<ProductResponse?> {
                override fun onResponse(call: Call<ProductResponse?>, response: Response<ProductResponse?>) {
                    listProductMutableLiveData.value = response.body()?.productList
                }

                override fun onFailure(call: Call<ProductResponse?>, t: Throwable) {
                    val productList: List<Product?> = ArrayList()
                    listProductMutableLiveData.value = productList
                }
            })
            return listProductMutableLiveData
        }
}