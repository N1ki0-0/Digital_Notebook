package com.example.digitalnotebook.test.PrefData.utlis

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.digitalnotebook.test.PrefData.model.UserDetails
import kotlinx.coroutines.flow.map


private const val USER_DATASTORE = "user_data"

val Context.dataStore by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManger(val context: Context) {
    companion object{
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
        val MOBILE_NUMDER = stringPreferencesKey("MOBILE_NUMDER")
        val NAME = stringPreferencesKey("NAME")
    }

    suspend fun saveToDataStore(userDetails: UserDetails){
        context.dataStore.edit {
            it[EMAIL] = userDetails.emailAddress
            it[PASSWORD] = userDetails.password
            it[MOBILE_NUMDER] = userDetails.mobileNumber
            it[NAME] = userDetails.name
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        UserDetails(
            emailAddress = it[EMAIL]?:"",
            mobileNumber = it[MOBILE_NUMDER]?:"",
            password = it[PASSWORD]?:"",
            name = it[NAME]?:""
        )
    }

    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }

}