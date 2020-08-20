package dev.blank.sketchme.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dev.blank.sketchme.data.model.User

class UserPreferences(context: Context?) {
    private var preferences: SharedPreferences? = null
    var id: String?
        get() = preferences!!.getString(ID, null)
        set(id) {
            preferences!!.edit().putString(ID, id).apply()
        }
    var token: String?
        get() = preferences!!.getString(TOKEN, null)
        set(token) {
            preferences!!.edit().putString(TOKEN, token).apply()
        }
    var name: String?
        get() = preferences!!.getString(NAME, null)
        set(name) {
            preferences!!.edit().putString(NAME, name).apply()
        }
    var email: String?
        get() = preferences!!.getString(EMAIL, null)
        set(email) {
            preferences!!.edit().putString(EMAIL, email).apply()
        }
    var photo: String?
        get() = preferences!!.getString(PHOTO, null)
        set(photo) {
            preferences!!.edit().putString(PHOTO, photo).apply()
        }
    var statusLogin: Boolean
        get() = preferences!!.getBoolean(STATUS_LOGIN, false)
        set(status) {
            preferences!!.edit().putBoolean(STATUS_LOGIN, status).apply()
        }

    fun deleteUser() {
        preferences!!.edit().clear().apply()
    }

    val user: User
        get() {
            val user = User()
            user.id = id
            user.token = token
            user.name = name
            user.email = email
            user.photo = photo
            user.isStatusLogin = statusLogin
            return user
        }

    companion object {
        var ID = "ID_OF_USER"
        var TOKEN = "TOKEN_OF_USER"
        var NAME = "NAME_OF_USER"
        var EMAIL = "EMAIL_OF_USER"
        var PHOTO = "PHOTO_OF_USER"
        var STATUS_LOGIN = "STATUS_LOGIN_OF_USER"
    }

    init {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}