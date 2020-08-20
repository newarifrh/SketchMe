package dev.blank.sketchme.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.blank.sketchme.data.repository.OrderRepository

@Suppress("UNCHECKED_CAST")
class OrderViewModelFactory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(orderRepository) as T
    }
}