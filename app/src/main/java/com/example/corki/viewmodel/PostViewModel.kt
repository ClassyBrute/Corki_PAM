package com.example.corki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corki.models.post.Post
import com.example.corki.models.post.Posts
import com.example.corki.service.CorkiAPIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class PostViewModel : ViewModel() {
    private lateinit var service : CorkiAPIService
    private lateinit var dispose : Disposable
    var postData = MutableLiveData<Posts>()
    var postError = MutableLiveData<Boolean>()

    fun postViewModel() {
        service = CorkiAPIService()
        fetchData()
    }

    private fun fetchData() {
        dispose = service.getPostsData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Posts>(){
                override fun onSuccess(value: Posts) {
                    postData.value = value
                    postError.value = false
                }

                override fun onError(e: Throwable) {
                    postError.value = true
                }
            })
    }

    fun onDispose() {
        dispose.dispose()
    }

    fun getPosts() : LiveData<Posts> {
        return postData
    }

    fun getPostsError() : LiveData<Boolean> {
        return postError
    }
}