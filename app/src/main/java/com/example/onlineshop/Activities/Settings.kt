package com.example.onlineshop.activity.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.View
import com.example.onlineshop.R
import com.example.onlineshop.activity.firestore.FirestoreClass
import com.example.onlineshop.activity.models.User
import com.example.onlineshop.activity.utils.Constants
import com.example.onlineshop.activity.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionbar()

        //link the 2 button
        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }
    private fun setupActionbar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionBar=supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_back_24dp)
        }
        toolbar_settings_activity.setNavigationOnClickListener{onBackPressed()}
    }
    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }
    fun userDetailsSuccess(user: User){

        mUserDetails = user

        hideProgressDialog()
        GlideLoader(this@Settings).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstName}${user.lastname}"
        tv_gender.text=user.gender
        tv_email.text=user.email
        tv_mobile_number.text="${user.mobile}"
    }
    override fun onResume(){
        super.onResume()
        getUserDetails()
    }
    override fun onClick(v:View?){
        if (v != null){
            when(v.id){

                R.id.tv_edit ->{
                    //FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@Settings,UserProfile::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@Settings,Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}