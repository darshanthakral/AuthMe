package com.example.authme.helpers.weather

object SetSpinnerItems {

    fun getItems(): ArrayList<TimeZoneModel> {

        val itemList = ArrayList<TimeZoneModel>()

        val zone1 = TimeZoneModel(
            "Chennai, Kolkata, Mumbai, New Delhi",
            "GMT(+05:30)"
        )

        itemList.add(zone1)

        val zone2 = TimeZoneModel(
            "Moscow, St. Petersburg, Volgograd (RTZ 2)",
            "GMT(+03:00)"
        )

        itemList.add(zone2)

        val zone3 = TimeZoneModel(
            "Pacific Time (US & Canada)",
            "GMT(-08:00)"
        )

        itemList.add(zone3)

        val zone4 = TimeZoneModel(
            "Dublin, Edinburgh, Lisbon, London",
            "00:00"
        )

        itemList.add(zone4)


        val zone5 = TimeZoneModel(
            "Beijing, Chongqing, Hong Kong, Urumqi",
            "+08:00"
        )

        itemList.add(zone5)

        return itemList
    }
}