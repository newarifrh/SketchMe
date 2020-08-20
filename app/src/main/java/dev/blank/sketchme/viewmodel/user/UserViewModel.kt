package dev.blank.sketchme.viewmodel.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dev.blank.sketchme.data.model.User
import dev.blank.sketchme.data.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var userMutableLiveData: MutableLiveData<User?>?
    private var account: GoogleSignInAccount? = null
    fun setUser(account: GoogleSignInAccount?) {
        this.account = account
        saveUser()
    }

    val user: MutableLiveData<User?>?
        get() {
            userMutableLiveData = loadUser()
            return userMutableLiveData
        }

    fun deleteUser() {
        userRepository.deleteUser()
    }

    private fun loadUser(): MutableLiveData<User?>? {
        return userRepository.user
    }

    private fun saveUser() {
        userRepository.setUser(account)
    }

    init {
        userMutableLiveData = MutableLiveData()
    }
}