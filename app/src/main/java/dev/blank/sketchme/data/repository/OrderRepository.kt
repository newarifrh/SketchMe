package dev.blank.sketchme.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.blank.sketchme.data.model.Order
import java.util.*

class OrderRepository(private var databaseReference: DatabaseReference, private var storageReference: StorageReference) {
    private var statusOrderLiveData = MutableLiveData<Boolean?>()
    private var filenameImageLiveData = MutableLiveData<String?>()
    private var status: Boolean? = null
    private var filename: String? = ""
    fun setOrder(id: String?, idOrder: String?, order: Order?) {
        databaseReference.child("order").child(id!!).child(idOrder!!).setValue(order).addOnCompleteListener { task: Task<Void?> ->
            status = task.isSuccessful
            statusOrder
        }.addOnFailureListener {
            status = false
            statusOrder
        }
    }

    val statusOrder: MutableLiveData<Boolean?>
        get() {
            statusOrderLiveData.value = status
            return statusOrderLiveData
        }

    fun uploadImage(id: String?, uri: Uri?, extension: String) {
        val imageRef = storageReference.child("uploads/" + id + "/" + UUID.randomUUID().toString() + "." + extension)
        imageRef.putFile(uri!!)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    filename = taskSnapshot.metadata!!.name
                    filenameImage
                }
                .addOnFailureListener { exception: Exception ->
                    println(exception.message)
                    filename = null
                    filenameImage
                }
    }

    val filenameImage: MutableLiveData<String?>
        get() {
            filenameImageLiveData.value = filename
            return filenameImageLiveData
        }
}