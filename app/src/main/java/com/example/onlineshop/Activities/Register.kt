package com.example.onlineshop.activity.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.onlineshop.R
import com.example.onlineshop.firestore.FirestoreClass
import com.example.onlineshop.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class Register : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()

        tv_login.setOnClickListener {
            //val intent = Intent(this@Register, Login::class.java)
            //startActivity(intent)
            onBackPressed()
        }
        btn_register.setOnClickListener{
            registerUser()
            //validateRegisterDetails()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_register_activity)
        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        toolbar_register_activity.setNavigationOnClickListener{onBackPressed()}

    }

    private fun validateRegisterDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' })  -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            et_password.text.toString().trim{it<=' '} != et_confirm_password.text.toString()
                .trim{ it <=' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            terms_and_condition.isActivated-> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else ->{
                //showErrorSnackBar(resources.getString(R.string.registry_successful),false)
                true
            }
        }
    }

    private fun registerUser(){
        if(validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))
            val email:String=et_email.text.toString().trim{it<=' '}
            val password:String=et_password.text.toString().trim{it<=' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult>{ task->

                        if (task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user= User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{it<=' '},
                                et_last_name.text.toString().trim{it<=' '},
                                et_email.text.toString().trim{it<=' '},
                            )

                            /*val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{it<=' '},
                                et_last_name.text.toString().trim{it<=' '},
                                et_email.text.toString().trim{it<=' '},
                            )*/



                            //showErrorSnackBar("you are registered successfully. your user id is ${firebaseUser.uid} ",false)

                            FirestoreClass().registerUser(this@Register,user)

                            //FirebaseAuth.getInstance().signOut()
                            // finish()

                        }
                        else{
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
                )
        }
    }
    fun userRegistrationSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@Register,resources.getString(R.string.registry_successful),
            Toast.LENGTH_SHORT
        ).show()
    }
}