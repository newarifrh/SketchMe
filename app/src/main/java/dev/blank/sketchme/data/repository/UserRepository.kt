package dev.blank.sketchme.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dev.blank.sketchme.data.local.preferences.UserPreferences
import dev.blank.sketchme.data.model.User

class UserRepository(private val userPreferences: UserPreferences) {
    private var userMutableLiveData = MutableLiveData<User?>()
    fun setUser(account: GoogleSignInAccount?) {
        userPreferences.id = account!!.id
        userPreferences.token = account.idToken
        userPreferences.name = account.displayName
        userPreferences.email = account.email
        if (account.photoUrl != null) userPreferences.photo = account.photoUrl.toString()
        userPreferences.statusLogin = true
        user
    }

    val user: MutableLiveData<User?>
        get() {
            userMutableLiveData.value = userPreferences.user
            return userMutableLiveData
        }

    fun deleteUser() {
        userPreferences.deleteUser()
        user
    }
}