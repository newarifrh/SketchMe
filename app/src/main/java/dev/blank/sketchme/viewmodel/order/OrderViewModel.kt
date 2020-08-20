package dev.blank.sketchme.viewmodel.order

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.blank.sketchme.data.model.Order
import dev.blank.sketchme.data.repository.OrderRepository

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private var statusOrderLiveData: MutableLiveData<Boolean?>? = MutableLiveData()
    private var filenameImageLiveData: MutableLiveData<String?>? = MutableLiveData()
    val order: MutableLiveData<Boolean?>?
        get() {
            statusOrderLiveData = loadOrder()
            return statusOrderLiveData
        }

    private fun loadOrder(): MutableLiveData<Boolean?>? {
        return orderRepository.statusOrder
    }

    fun setOrder(id: String?, idOrder: String?, order: Order?) {
        orderRepository.setOrder(id, idOrder, order)
    }

    val filenameImage: MutableLiveData<String?>?
        get() {
            filenameImageLiveData = loadFilename()
            return filenameImageLiveData
        }

    private fun loadFilename(): MutableLiveData<String?>? {
        return orderRepository.filenameImage
    }

    fun uploadImage(id: String?, uri: Uri?, extension: String) {
        orderRepository.uploadImage(id, uri, extension)
    }
}