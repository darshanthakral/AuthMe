package com.example.authme.helpers.weather

class TimeZoneModel(timeZoneName: String, timeZoneOffset: String) {

    private var timeZoneName: String? = null
    private var timeZoneOffset: String? = null

    init {
        this.timeZoneName = timeZoneName
        this.timeZoneOffset = timeZoneOffset
    }


    fun setTimeZoneName(name: String) {
        this.timeZoneName = name
    }

    fun getTimeZoneName(): String {
        return timeZoneName.toString()
    }


    fun setTimeZoneOffset(offsetValue: String) {
        this.timeZoneOffset = offsetValue
    }

    fun getTimeZoneOffset(): String {
        return timeZoneOffset.toString()
    }

    override fun toString(): String {
        return timeZoneName.toString()
    }
}