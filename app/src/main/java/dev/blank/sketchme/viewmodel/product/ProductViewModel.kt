package dev.blank.sketchme.viewmodel.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.blank.sketchme.data.model.product.Product
import dev.blank.sketchme.data.repository.ProductRepository

class ProductViewModel(productRepository: ProductRepository) : ViewModel() {
    private var mProductLiveData: MutableLiveData<List<Product?>?>?
    private val mProductRepository: ProductRepository
    val product: MutableLiveData<List<Product?>?>?
        get() {
            mProductLiveData = loadProduct()
            return mProductLiveData
        }

    private fun loadProduct(): MutableLiveData<List<Product?>?>? {
        return mProductRepository.news
    }

    init {
        mProductLiveData = MutableLiveData()
        mProductRepository = productRepository
    }
}