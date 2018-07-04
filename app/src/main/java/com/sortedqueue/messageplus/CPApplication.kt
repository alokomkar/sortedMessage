package com.sortedqueue.messageplus

import android.app.Application
import com.sortedqueue.messageplus.data.CPPreferenceManager
import com.sortedqueue.messageplus.data.PreferencesView

/**
 * Created by Alok on 02/07/18.
 */
class CPApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private var cpPreferences : PreferencesView?= null

        fun getPreferences() : PreferencesView {

            if( cpPreferences == null )
                cpPreferences = CPPreferenceManager(instance)

            return cpPreferences!!
        }

        lateinit var instance: CPApplication
            private set

    }

}