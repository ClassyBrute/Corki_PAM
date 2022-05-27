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
    var postData = MutableLiveData<Post>()
    var postId = MutableLiveData<String>()
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
                    Log.e("post_get_with_query", e.toString())
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
                    Log.e("post_get", e.toString())
                }
            })
    }

    private fun sendPost(map: Map<String, String>, token: String) {
        dispose = service.postPostData(map, token)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<String>(){
                override fun onSuccess(t: String) {
                    postId.value = t
                    postError.value = false
                }

                override fun onError(e: Throwable) {
                    postError.value = true
                    Log.e("post_post", e.toString())
                }
            })
    }

    //GETTER
    fun postGetter() : LiveData<Post> {
        return postData
    }

    fun postIdGetter() : LiveData<String> {
        return postId
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

    fun postPost(map: Map<String, String>, token: String): LiveData<String> {
        sendPost(map, "Bearer $token")
        return postId
    }

    fun getPostsError() : LiveData<Boolean> {
        return postError
    }
}