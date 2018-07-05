package com.sortedqueue.messageplus.base

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by Alok on 05/07/18.
 */
class CPJobCreator : JobCreator {
    override fun create(tag: String): Job? {
        return if( tag.startsWith(REMINDER_TAG) ) {
            CPJob()
        }
        else null
    }
}