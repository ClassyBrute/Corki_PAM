package com.example.corki.viewmodel

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
    var postData = MutableLiveData<Post>()
    var postsData = MutableLiveData<Posts>()
    var postError = MutableLiveData<Boolean>()

    fun postViewModel() {
        service = CorkiAPIService()
    }

    private fun fetchPostsWithQuery(map: Map<String, String>) {
        dispose = service.getPostsDataWithQuery(map)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Posts>(){
                override fun onSuccess(t: Posts) {
                    postsData.value = t
                    postError.value = false
                }

                override fun onError(e: Throwable) {
                    postError.value = true
                }
            })
    }

    private fun fetchPost(id: String) {
        dispose = service.getPostData(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Post>(){
                override fun onSuccess(t: Post) {
                    postData.value = t
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

    fun getPostsWithQuery(map: Map<String, String>) : LiveData<Posts> {
        fetchPostsWithQuery(map)
        return postsData
    }

    fun getPost(id: String) : LiveData<Post> {
        fetchPost(id)
        return postData
    }

    fun getPostsError() : LiveData<Boolean> {
        return postError
    }
}