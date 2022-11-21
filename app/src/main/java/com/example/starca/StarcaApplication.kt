package com.example.starca

import android.app.Application
import com.example.starca.models.Listing
import com.parse.Parse
import com.parse.ParseObject

class StarcaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Register the Listing Parse model so that we can use that class and link to the Listing table in Parse DB
        ParseObject.registerSubclass(Listing::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.BACK4APP_APP_ID)
                .clientKey(BuildConfig.BACK4APP_CLIENT_KEY)
                .server(BuildConfig.BACK4APP_SERVER_URL)
                .build());
    }
}