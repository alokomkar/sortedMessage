package com.sortedqueue.messageplus.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import java.util.*
import com.sortedqueue.messageplus.base.CURRENT_TIME


/**
 * Created by Alok on 05/07/18.
 */
class DatePickerFragment : DialogFragment() {

    private lateinit var dateSetListener : DatePickerDialog.OnDateSetListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is DatePickerDialog.OnDateSetListener )
            dateSetListener = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        c.timeInMillis = arguments.getLong(CURRENT_TIME)

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, dateSetListener, year, month, day)

    }


}