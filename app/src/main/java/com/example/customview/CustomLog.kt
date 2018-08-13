package com.example.customview
import android.util.Log

object CustomLog{
    val TAG = "Log"
    fun d(msg:String){
        if(BuildConfig.DEBUG){
            Log.d(TAG, msg)
        }
    }
    fun cd(tag: String, msg:String){
        if(BuildConfig.DEBUG){
            Log.d(tag, msg)
        }
    }
}