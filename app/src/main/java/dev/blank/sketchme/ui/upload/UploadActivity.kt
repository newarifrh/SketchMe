package dev.blank.sketchme.ui.upload

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dev.blank.sketchme.R
import dev.blank.sketchme.data.local.preferences.UserPreferences
import dev.blank.sketchme.data.model.Order
import dev.blank.sketchme.data.repository.OrderRepository
import dev.blank.sketchme.databinding.ActivityUploadBinding
import dev.blank.sketchme.util.Constant.RC_LOAD_IMG
import dev.blank.sketchme.viewmodel.order.OrderViewModel
import dev.blank.sketchme.viewmodel.order.OrderViewModelFactory
import java.io.FileNotFoundException
import java.util.*

class UploadActivity : AppCompatActivity() {
    private var binding: ActivityUploadBinding? = null
    private var idProduct = 0
    private var descriptionList: List<Int?>? = null
    private var userPreferences: UserPreferences? = null
    private var uri: Uri? = null
    private var viewModel: OrderViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        val mStorageRef = FirebaseStorage.getInstance().reference
        val orderRepository = OrderRepository(mDatabaseRef, mStorageRef)
        viewModel = ViewModelProvider(this, OrderViewModelFactory(orderRepository)).get(OrderViewModel::class.java)
        idProduct = intent.getIntExtra("idProduct", 0)
        descriptionList = intent.getIntegerArrayListExtra("descriptionList")
        userPreferences = UserPreferences(this)
        ContextCompat.getDrawable(this, R.drawable.button_bg)?.let { binding!!.btnOrder.background = it }
        this.bindProgressButton(binding!!.btnOrder)

        binding!!.btnOrder.attachTextChangeAnimator {
            this.fadeInMills = 300
            this.fadeOutMills = 300
        }
        binding!!.btnOrder.setOnClickListener {
            setAnimation(true)
            handlerOrder()
        }
        binding!!.avatarView.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RC_LOAD_IMG)
        }
        viewModel?.order?.observe(this) { status: Boolean? ->
            if (status != null) {
                if (status) {
                    Toast.makeText(this, "Successfully to Order", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to Order", Toast.LENGTH_SHORT).show()
                }
                setAnimation(false)
                finish()
            }
        }
        viewModel?.filenameImage?.observe(this, { filename: String? ->
            if (filename != null) {
                if (filename != "") {
                    println(filename)
                    val idOrder = UUID.randomUUID().toString()
                    val order = Order(idOrder, idProduct, descriptionList, 1, filename)
                    viewModel!!.setOrder(userPreferences?.id, idOrder, order)
                }
            } else {
                Toast.makeText(this, "Failed to upload Image", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAnimation(status: Boolean) {
        if (status) {
            binding?.btnOrder?.showProgress {
                this.progressColor = Color.WHITE
                this.gravity = DrawableButton.GRAVITY_CENTER
            }
            binding!!.btnOrder.isEnabled = false
            binding!!.avatarView.isAnimating = true
        } else {
            binding!!.avatarView.isAnimating = false
            binding!!.btnOrder.isEnabled = true
            binding!!.btnOrder.hideProgress(R.string.order)
        }
    }

    private fun handlerOrder() {
        if (uri != null) {
            val path = uri!!.lastPathSegment
            val filenameArray = path!!.split("\\.".toRegex()).toTypedArray()
            val extension = filenameArray[filenameArray.size - 1]
            viewModel!!.uploadImage(userPreferences?.id, uri, extension)
        } else {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
            setAnimation(false)
        }
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        if (reqCode == RC_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                try {
                    uri = data!!.data
                    if (uri != null) {
                        val imageStream = contentResolver.openInputStream(uri!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        binding!!.avatarView.distanceToBorder = 20
                        binding!!.avatarView.setImageBitmap(selectedImage)
                    } else {
                        Toast.makeText(this@UploadActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this@UploadActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@UploadActivity, "You haven't picked Image", Toast.LENGTH_LONG).show()
            }
        }
    }
}