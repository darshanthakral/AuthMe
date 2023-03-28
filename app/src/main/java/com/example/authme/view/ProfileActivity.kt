package com.example.authme.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authme.R
import com.example.authme.databinding.ActivityProfileBinding
import com.example.authme.helpers.CallIntent
import com.example.authme.helpers.ToastMe
import com.example.authme.helpers.weather.GetWeatherData
import com.example.authme.helpers.weather.SetSpinnerItems
import com.example.authme.helpers.weather.TimeZoneModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class ProfileActivity : AppCompatActivity() {

    private var binder: ActivityProfileBinding? = null
    private var userID: String? = null
    private var timezoneSpinner: Spinner? = null
    private var model: TimeZoneModel? = null
    private var tvWeatherData: TextView? = null

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        actionBar?.elevation = 0F
        binder = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binder?.root)

        //setting textview from binder
        tvWeatherData = binder?.tvWeatherData

        //Getting Auth user Data
        getUserData()

        fetchSpinnerItems()

        //Logout User
        binder?.btnProfileLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            CallIntent(this@ProfileActivity, LoginActivity())
        }

    }

    private fun getUserData() {
        val mAuth: FirebaseAuth = Firebase.auth
        val mStore: FirebaseFirestore = Firebase.firestore
        userID = mAuth.currentUser?.uid
        binder?.tvRegisteredEmail?.text = mAuth.currentUser?.email

        val docRef: DocumentReference = mStore.collection("users").document(userID!!)
        docRef.get().addOnSuccessListener { document ->


            Picasso.with(this)
                .load(document?.get("imageUri").toString())
                .centerCrop()
                .resize(
                    binder?.ivProfilePicture!!.measuredWidth,
                    binder?.ivProfilePicture!!.measuredHeight
                )
                .into(binder?.ivProfilePicture)

            binder?.tvRegisteredName?.text = document?.get("name").toString()
            binder?.tvRegisteredUsername?.text = document?.get("username").toString()
            binder?.tvProfilePersonBio?.text = document?.get("userBio").toString()

        }

    }

    private fun fetchSpinnerItems() {

        //Getting view of spinner using viewBinder
        timezoneSpinner = binder?.timezoneSpinner

        val adapter: ArrayAdapter<TimeZoneModel> = ArrayAdapter<TimeZoneModel>(
            this,
            android.R.layout.simple_spinner_item,
            SetSpinnerItems.getItems()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timezoneSpinner?.adapter = adapter

        model = timezoneSpinner?.selectedItem as TimeZoneModel
        displayTimeZoneData(model!!)


        timezoneSpinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                model = p0?.selectedItem as TimeZoneModel
                displayTimeZoneData(model!!)

                when (p2) {
                    0 -> {
                        clearTextView()
                        GetWeatherData("mumbai", tvWeatherData).execute()
                    }

                    1 -> {
                        clearTextView()
                        GetWeatherData("moscow", tvWeatherData).execute()
                    }

                    2 -> {
                        clearTextView()
                        GetWeatherData("canada", tvWeatherData).execute()
                    }

                    3 -> {
                        clearTextView()
                        GetWeatherData("london", tvWeatherData).execute()
                    }

                    4 -> {
                        clearTextView()
                        GetWeatherData("hong%20kong", tvWeatherData).execute()
                    }

                    else -> {
                        clearTextView()
                        GetWeatherData("mumbai", tvWeatherData)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun displayTimeZoneData(model: TimeZoneModel) {
        val offset = model.getTimeZoneOffset()
        val info = "TimeZoneOffset: $offset"

        ToastMe(this@ProfileActivity, info)
    }

    private fun clearTextView(){
        tvWeatherData?.text = ""
    }
}




