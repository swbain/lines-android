package com.stephenbain.lines.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val welcomeText = MutableLiveData<String>()

    init {
        welcomeText.value = "welcome to llllllll.co/"
    }

    fun welcomeText(): LiveData<String> {
        return welcomeText
    }

}