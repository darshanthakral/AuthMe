package com.example.authme.helpers.weather

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import io.grpc.android.BuildConfig
import org.json.JSONObject
import java.net.URL


class GetWeatherData(timeZoneCityName: String?, textView: TextView?) :
    AsyncTask<String, Void, String>() {

    private var baseURL = "https://api.openweathermap.org/data/2.5/forecast?q="

    @SuppressLint("StaticFieldLeak")
    private var showInTextView: TextView? = null
    private var cityName: String? = null
    private var apiKey = "&appid=${com.example.authme.BuildConfig.API_KEY}"


    init {
        this.cityName = timeZoneCityName
        showInTextView = textView
    }

    override fun doInBackground(vararg p0: String?): String {

        val response: String? = try {
            URL(baseURL + cityName + apiKey).readText(
                Charsets.UTF_8
            )
        } catch (e: Exception) {
            null
        }
        return response!!
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        try {
            /* Extracting JSON returns from the API */
            val jsonObj = result?.let { JSONObject(it) }

            val objArray = jsonObj?.getJSONArray("list")

            if (objArray != null) {
                for (i in 0..objArray.length()) {
                    val myResult = objArray.getJSONObject(i)
                    showInTextView?.append("report $i: \n$myResult\n\n")
                    // Log.i("WEATHER DATA", myResult.toString())
                }
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}