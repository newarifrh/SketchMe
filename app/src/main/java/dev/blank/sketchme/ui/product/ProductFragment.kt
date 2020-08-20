package dev.blank.sketchme.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dev.blank.sketchme.R
import dev.blank.sketchme.data.model.product.Product
import dev.blank.sketchme.databinding.FragmentProductBinding
import dev.blank.sketchme.ui.upload.UploadActivity
import java.util.*

class ProductFragment : Fragment() {
    private var binding: FragmentProductBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductBinding.inflate(inflater)
        val args = arguments
        if (args != null) {
            val product: Product? = args.getParcelable("SERVICE")
            val position = args.getInt("POSITION")
            product?.let { setView(it, position) }
        } else {
            Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
    private fun setView(product: Product, position: Int) {
        binding!!.tvName.text = product.name
        var result = ""
        for (i in product.descriptionList?.indices!!) {
            var value: String? = null
            when (i) {
                Product.SIZE_STATUS -> {
                    value = product.descriptionList!![i]?.let { Product.SIZE[it] }
                }
                Product.BACKGROUND_STATUS -> {
                    value = product.descriptionList!![i]?.let { Product.BACKGROUND_FULL[it] }
                }
                Product.CONTOUR_STATUS -> {
                    value = product.descriptionList!![i]?.let { Product.CONTOUR_FULL[it] }
                }
            }
            if (value != null) {
                if (i != product.descriptionList!!.size - 1) {
                    result = "$result$value, "
                } else {
                    result += value
                    result = "($result)"
                    binding!!.tvDescription.text = result
                }
            }
        }
        if (context != null) {
            when (position) {
                0 -> {
                    binding!!.layoutBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.hbrbrc)
                }
                1 -> {
                    binding!!.layoutBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.fbrbrc)
                }
                2 -> {
                    binding!!.layoutBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.hbcbrc)
                }
                3 -> {
                    binding!!.layoutBackground.background = ContextCompat.getDrawable(requireContext(), R.drawable.hbcbcc)
                }
            }
        }
        binding!!.btnOrder.setOnClickListener {
            val intent = Intent(context, UploadActivity::class.java)
            intent.putExtra("idProduct", product.id)
            intent.putIntegerArrayListExtra("descriptionList", product.descriptionList as ArrayList<Int?>)
            startActivity(intent)
        }
    }
}