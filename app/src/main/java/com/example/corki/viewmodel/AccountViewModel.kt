package com.example.corki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corki.models.account.Account
import com.example.corki.service.CorkiAPIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class AccountViewModel : ViewModel() {
    private lateinit var service : CorkiAPIService
    private lateinit var dispose : Disposable
    var loggedAccount = MutableLiveData<Account>()
    var account = MutableLiveData<Account>()
    var accountToken = MutableLiveData<String>()

    var tokenError = MutableLiveData<Boolean>()
    var accountError = MutableLiveData<Boolean>()
    var accountPreviewedError = MutableLiveData<Boolean>()

    fun accountViewModel() {
        service = CorkiAPIService()
    }

    private fun fetchAccountToken(map: Map<String, String>, mode: String) {
        when(mode) {
            "REGISTER" -> {
                dispose = service.postRegisterData(map)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object: DisposableSingleObserver<String>(){
                        override fun onSuccess(t: String) {
                            accountToken.value = t
                            tokenError.value = false
                        }
                        override fun onError(e: Throwable) {
                            tokenError.value = true
                            Log.e("token_register", e.toString())
                        }
                    })
            }

            "LOGIN" -> {
                dispose = service.postLoginData(map)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object: DisposableSingleObserver<String>(){
                        override fun onSuccess(t: String) {
                            accountToken.value = t
                            tokenError.value = false
                        }
                        override fun onError(e: Throwable) {
                            tokenError.value = true
                            Log.e("token_login", e.toString())
                        }
                    })
            }

        }
    }

    private fun fetchLoggedAccount(token: String) {
        dispose = service.getLoggedUserData(token)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Account>(){
                override fun onSuccess(t: Account) {
                    loggedAccount.value = t
                    accountError.value = false
                }

                override fun onError(e: Throwable) {
                    accountError.value = true
                    Log.e("logged_account", e.toString())
                }
            })
    }

    private fun fetchAccount(id: String) {
        dispose = service.getAccountData(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Account>(){
                override fun onSuccess(t: Account) {
                    account.value = t
                    accountPreviewedError.value = false
                }

                override fun onError(e: Throwable) {
                    accountPreviewedError.value = true
                    Log.e("previewed_account", e.toString())
                }
            })

    }

    fun onDispose() {
        dispose.dispose()
    }

    fun postLogin(map: Map<String, String>) : LiveData<String> {
        fetchAccountToken(map, "LOGIN")
        return accountToken
    }

    fun postRegister(map: Map<String, String>) : LiveData<String> {
        fetchAccountToken(map, "REGISTER")
        return accountToken
    }

    fun getProfile(token: String) : LiveData<Account> {
        fetchLoggedAccount("Bearer $token")
        return loggedAccount
    }

    fun getAccount(id: String) : LiveData<Account> {
        fetchAccount(id)
        return account
    }


//    fun getTokenError() : LiveData<Boolean> {
//        return tokenError
//    }
}