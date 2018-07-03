package com.sortedqueue.copypaste

/**
 * Created by Alok on 02/07/18.
 */
data class MessageTitle( var messageId : Long,
                         var messageContent : String,
                         var messageType : Int,
                         var sendCount : Int = 0 ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageTitle

        if (messageContent != other.messageContent) return false
        if (messageType != other.messageType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageContent.hashCode()
        result = 31 * result + messageType
        return result
    }
}