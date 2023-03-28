package com.example.authme.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.authme.R
import com.example.authme.databinding.ActivityLoginBinding
import com.example.authme.helpers.CallIntent
import com.example.authme.helpers.CheckIfNull
import com.example.authme.helpers.EditTextHandler
import com.example.authme.helpers.ToastMe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var binder: ActivityLoginBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.currentUser

        user?.let {
            CallIntent(this@LoginActivity, ProfileActivity())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binder = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binder?.root)

        mAuth = Firebase.auth

        userEmail = binder?.etEnterLoginEmail!!
        userPassword = binder?.etEnterLoginPassword!!

        binder?.btnLogIn?.setOnClickListener {
            checkIfFieldValid()
        }

        //Intent To SignUp Activity
        binder?.btnSignUp?.setOnClickListener {
            CallIntent(this@LoginActivity, SignUpActivity())
        }
    }

    private fun checkIfFieldValid() {
        val emptyFieldMessage = " field empty"

        if (CheckIfNull.isNull(userEmail)) EditTextHandler(
            userEmail,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(userPassword)) EditTextHandler(
            userPassword,
            true,
            emptyFieldMessage
        )
        else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text).matches()) EditTextHandler(
            userEmail,
            false,
            "Enter valid email"
        )
        else loginUser()
    }

    private fun loginUser() {

        val email = userEmail.text.toString()
        val password = userPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        ToastMe(this, "Username or password is incorrect!")
                    } catch (e: java.lang.Exception) {
                        Log.e(TAG, e.message.toString())
                    }
                } else {
                    ToastMe(this, "Authentication success. User loggedIn...")
                    CallIntent(this@LoginActivity, ProfileActivity())
                }
            }
    }

}