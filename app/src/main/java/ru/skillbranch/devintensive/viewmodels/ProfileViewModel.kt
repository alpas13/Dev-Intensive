package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repository.PreferencesRepository

/**
 * Created by Oleksiy Pasmarnov on 11.10.21
 */
class ProfileViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()

    init {
        Log.d("M_ProfileViewModel","init view model")
        profileData.value = repository.getProfile()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel","view model cleared")
    }

    fun getProfileDate(): LiveData<Profile> = profileData

    fun saveProfileDate(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }
}