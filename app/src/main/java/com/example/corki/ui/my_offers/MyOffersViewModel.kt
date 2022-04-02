package com.example.corki.ui.my_offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyOffersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is myoffers Fragment"
    }
    val text: LiveData<String> = _text
}