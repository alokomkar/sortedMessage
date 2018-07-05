package com.sortedqueue.messageplus.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.sortedqueue.messageplus.base.CURRENT_TIME
import java.util.*

/**
 * Created by Alok on 05/07/18.
 */
class TimePickerFragment : DialogFragment() {

    private lateinit var timeSetListener : TimePickerDialog.OnTimeSetListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is TimePickerDialog.OnTimeSetListener )
            timeSetListener = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        c.timeInMillis = arguments.getLong(CURRENT_TIME)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)


        return TimePickerDialog(activity, timeSetListener, hour, minute, false)

    }
}