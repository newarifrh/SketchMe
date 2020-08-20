package dev.blank.sketchme.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dev.blank.sketchme.R
import dev.blank.sketchme.data.APIClient
import dev.blank.sketchme.data.local.preferences.UserPreferences
import dev.blank.sketchme.data.model.User
import dev.blank.sketchme.data.model.product.Product
import dev.blank.sketchme.data.remote.ProductService
import dev.blank.sketchme.data.repository.ProductRepository
import dev.blank.sketchme.data.repository.UserRepository
import dev.blank.sketchme.databinding.FragmentHomeBinding
import dev.blank.sketchme.ui.login.LoginActivity
import dev.blank.sketchme.util.adapter.ProductPagerAdapter
import dev.blank.sketchme.viewmodel.product.ProductViewModel
import dev.blank.sketchme.viewmodel.product.ProductViewModelFactory
import dev.blank.sketchme.viewmodel.user.UserViewModel
import dev.blank.sketchme.viewmodel.user.UserViewModelFactory
import java.util.*

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var product: Product? = null
    private var status: Int = Product.SIZE_STATUS
    private var productViewModel: ProductViewModel? = null
    private var userViewModel: UserViewModel? = null
    private var adapter: ProductPagerAdapter? = null
    private var mProductList: List<Product?>? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        product = Product()
        mProductList = ArrayList()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val userPreferences = UserPreferences(context)
        val userRepository = UserRepository(userPreferences)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)
        userViewModel?.user?.observe(viewLifecycleOwner) { user: User? ->
            if (user!!.isStatusLogin) {
                binding!!.tvName.text = user.name
                Glide.with(requireContext()).load(user.photo).circleCrop().into(binding!!.ivPhoto)
            } else {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        val productService = APIClient().instance?.create(ProductService::class.java)
        val productRepository = ProductRepository(productService!!)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(productRepository)).get(ProductViewModel::class.java)
        productViewModel?.product?.observe(viewLifecycleOwner, { productList: List<Product?>? ->
            if (productList != null) {
                mProductList = productList
                adapter = ProductPagerAdapter(requireParentFragment(), productList)
                binding!!.viewPager.adapter = adapter
            }
        })

        binding!!.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                product = mProductList?.get(position)
                handleValueType(status)
            }
        })
        binding!!.btnLogout.setOnClickListener {
            mGoogleSignInClient!!.signOut().addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    userViewModel!!.deleteUser()
                } else {
                    Toast.makeText(context, "Logout failed, please try agian.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding!!.layoutSize.setOnClickListener {
            status = Product.SIZE_STATUS
            handleClick(status)
        }
        binding!!.layoutBackground.setOnClickListener {
            status = Product.BACKGROUND_STATUS
            handleClick(status)
        }
        binding!!.layoutContour.setOnClickListener {
            status = Product.CONTOUR_STATUS
            handleClick(status)
        }
        return binding!!.root
    }

    private fun handleClick(status: Int) {
        handleStatus(status)
        handleValueType(status)
    }

    private fun handleValueType(status: Int) {
        when (status) {
            Product.SIZE_STATUS -> {
                binding!!.tvValueType.text = Product.SIZE[product?.descriptionList?.get(Product.SIZE_STATUS)!!]
            }
            Product.BACKGROUND_STATUS -> {
                binding!!.tvValueType.text = Product.BACKGROUND[product?.descriptionList?.get(Product.BACKGROUND_STATUS)!!]
            }
            Product.CONTOUR_STATUS -> {
                binding!!.tvValueType.text = Product.CONTOUR[product?.descriptionList?.get(Product.CONTOUR_STATUS)!!]
            }
        }
    }

    private fun handleStatus(status: Int) {
        when (status) {
            Product.SIZE_STATUS -> {
                binding!!.statusSize.visibility = View.VISIBLE
                binding!!.statusBackground.visibility = View.INVISIBLE
                binding!!.statusContour.visibility = View.INVISIBLE
                binding!!.tvStatusSize.setTextColor(resources.getColor(R.color.customWhiteLight))
                binding!!.tvStatusBackground.setTextColor(resources.getColor(R.color.customWhiteDark))
                binding!!.tvStatusContour.setTextColor(resources.getColor(R.color.customWhiteDark))
            }
            Product.BACKGROUND_STATUS -> {
                binding!!.statusSize.visibility = View.INVISIBLE
                binding!!.statusBackground.visibility = View.VISIBLE
                binding!!.statusContour.visibility = View.INVISIBLE
                binding!!.tvStatusSize.setTextColor(resources.getColor(R.color.customWhiteDark))
                binding!!.tvStatusBackground.setTextColor(resources.getColor(R.color.customWhiteLight))
                binding!!.tvStatusContour.setTextColor(resources.getColor(R.color.customWhiteDark))
            }
            Product.CONTOUR_STATUS -> {
                binding!!.statusSize.visibility = View.INVISIBLE
                binding!!.statusBackground.visibility = View.INVISIBLE
                binding!!.statusContour.visibility = View.VISIBLE
                binding!!.tvStatusSize.setTextColor(resources.getColor(R.color.customWhiteDark))
                binding!!.tvStatusBackground.setTextColor(resources.getColor(R.color.customWhiteDark))
                binding!!.tvStatusContour.setTextColor(resources.getColor(R.color.customWhiteLight))
            }
        }
    }
}