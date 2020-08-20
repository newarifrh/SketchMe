package dev.blank.sketchme.util.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.blank.sketchme.data.model.product.Product
import dev.blank.sketchme.ui.product.ProductFragment

class ProductPagerAdapter(fragment: Fragment, private val mProductList: List<Product?>?) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = ProductFragment()
        val args = Bundle()
        args.putInt("POSITION", position)
        args.putParcelable("SERVICE", mProductList!![position])
        fragment.arguments = args
        return fragment
    }

    override fun getItemCount(): Int {
        return mProductList?.size ?: 0
    }
}