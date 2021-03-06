package com.example.onlineshop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.example.onlineshop.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context:Context) {
    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView) // the view of thr image which will be loaded

        }catch (e: IOException){
            e.printStackTrace()
        }
    }
    fun loadProductPicture(image: Any, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView) // the view of thr image which will be loaded

        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}