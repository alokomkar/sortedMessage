package com.sortedqueue.messageplus

import com.sortedqueue.messageplus.base.TYPE_TEXT

/**
 * Created by Alok on 02/07/18.
 */
class MessageTitle( var messageId : Long = 0L,
                         var messageTitle: String = "",
                         var messageContent : String = "",
                         var messageType : Int = TYPE_TEXT,
                         var scheduleTime : Long = 0L,
                         var sendCount : Int = 0 ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageTitle

        if (messageId != other.messageId) return false

        return true
    }

    override fun hashCode(): Int {
        return messageId.hashCode()
    }
}