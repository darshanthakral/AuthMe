package com.example.authme.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.authme.R
import com.example.authme.databinding.ActivitySignUpBinding
import com.example.authme.helpers.CallIntent
import com.example.authme.helpers.CheckIfNull
import com.example.authme.helpers.EditTextHandler
import com.example.authme.helpers.ToastMe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private var binder: ActivitySignUpBinding? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseFirestore

    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImgURI: Uri
    private var isImageSet = false
    private val user: HashMap<String, String> = HashMap()

    //View variables
    private lateinit var etEnterSignEmail: EditText
    private lateinit var etEnterSignPassword: EditText
    private lateinit var etEnterSignConfirmPassword: EditText

    private lateinit var etEnterSignName: EditText
    private lateinit var etEnterSignUsername: EditText
    private lateinit var etEnterSignBio: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binder = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binder?.root)

        mAuth = Firebase.auth
        mDatabase = Firebase.firestore
        storage = Firebase.storage
        selectedImgURI = Uri.EMPTY

        etEnterSignEmail = binder?.etEnterSignEmail!!
        etEnterSignPassword = binder?.etEnterSignPassword!!
        etEnterSignConfirmPassword = binder?.etEnterSignConfirmPassword!!
        etEnterSignName = binder?.etEnterSignName!!
        etEnterSignUsername = binder?.etEnterSignUsername!!
        etEnterSignBio = binder?.etEnterSignBio!!


        binder?.ivAddProfilePicture?.setOnClickListener { addUserImage() }
        binder?.btnCreateUser?.setOnClickListener { checkFieldValidation() }
    }

    private fun checkFieldValidation() {
        val emptyFieldMessage = " field empty"

        if (CheckIfNull.isNull(etEnterSignName)) EditTextHandler(
            etEnterSignName,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(etEnterSignEmail)) EditTextHandler(
            etEnterSignEmail,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(etEnterSignPassword)) EditTextHandler(
            etEnterSignPassword,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(etEnterSignConfirmPassword)) EditTextHandler(
            etEnterSignConfirmPassword,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(etEnterSignUsername)) EditTextHandler(
            etEnterSignUsername,
            true,
            emptyFieldMessage
        )
        else if (CheckIfNull.isNull(etEnterSignBio)) EditTextHandler(
            etEnterSignBio,
            true,
            emptyFieldMessage
        )
        else if (!Patterns.EMAIL_ADDRESS.matcher(etEnterSignEmail.text)
                .matches()
        ) EditTextHandler(
            etEnterSignEmail,
            false,
            "Enter valid email"
        )
        else if (!isImageSet) ToastMe(
            this,
            "Set Profile image to proceed!"
        )
        else createUser()
    }

    private fun createUser() {
        //check Password
        val finalEmail = etEnterSignEmail.text.toString()
        val finalPassword = etEnterSignPassword.text.toString()

        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*\\d)" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )

        if (!passwordREGEX.matcher(finalPassword).matches()) AlertDialog.Builder(this).apply {
            setMessage(
                "Invalid password format! \n" +
                        "Must be at least 8 characters \n" +
                        "Must have al least one Number \n" +
                        "Must have at least one small letter \n" +
                        "Must have at least one capital letter \n" +
                        "Must not have any blank space"
            )
        }.create().show()
        else if (etEnterSignConfirmPassword.text.toString() != finalPassword) ToastMe(
            this@SignUpActivity,
            "Confirm password does no match!"
        )
        else {
            mAuth.createUserWithEmailAndPassword(finalEmail, finalPassword)
                .addOnCompleteListener { task ->

                    if (!task.isSuccessful) {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthUserCollisionException) {
                            ToastMe(this, "This Email already exist. Try different email.")
                            etEnterSignName.text = null
                            etEnterSignEmail.text = null
                            etEnterSignPassword.text = null
                            etEnterSignConfirmPassword.text = null
                            etEnterSignUsername.text = null
                            etEnterSignBio.text = null
                        } catch (e: java.lang.Exception) {
                            ToastMe(this, "Something went wrong!")
                            Log.e("USER_CREATION_EXCEPTION: ", e.message.toString())
                        }
                    } else {
                        storeUserData()
                    }
                }
        }
    }

    private fun storeUserData() {

        val userID: String? = mAuth.currentUser?.uid
        val docRef: DocumentReference = mDatabase.collection("users").document(userID!!)
        val reference = storage.reference.child("profiles").child(Date().time.toString())

        reference.putFile(selectedImgURI).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener {
                    user["imageUri"] = it.toString()
                    user["name"] = etEnterSignName.text.toString()
                    user["username"] = etEnterSignUsername.text.toString()
                    user["userBio"] = etEnterSignBio.text.toString()
                    docRef.set(user).addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            ToastMe(this, "Something went wrong in storing user data!")
                        } else {
                            CallIntent(this@SignUpActivity, ProfileActivity())
                            ToastMe(this, "User created successfully")
                        }
                    }
                }
            }
        }
    }

    private fun addUserImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 101)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedImgURI = data.data!!

                Picasso.with(this)
                    .load(selectedImgURI)
                    .centerCrop()
                    .resize(
                        binder?.ivProfilePicture!!.measuredWidth,
                        binder?.ivProfilePicture!!.measuredHeight
                    )
                    .into(binder?.ivProfilePicture)
                isImageSet = true
            }
        }
    }
}