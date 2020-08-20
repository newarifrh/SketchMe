package dev.blank.sketchme.viewmodel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.blank.sketchme.data.repository.ProductRepository

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(private val mProductRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(mProductRepository) as T
    }
}