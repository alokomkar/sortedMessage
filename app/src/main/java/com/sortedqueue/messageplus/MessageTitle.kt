package com.sortedqueue.messageplus

import android.os.Parcel
import android.os.Parcelable
import com.sortedqueue.messageplus.base.TYPE_TEXT

/**
 * Created by Alok on 02/07/18.
 */
class MessageTitle(var messageId: Long = 0L,
                   var messageTitle: String = "",
                   var messageContent: String = "",
                   var messageType: Int = TYPE_TEXT,
                   var scheduleTime: Long = 0L,
                   var contacts : String = "",
                   var sendCount: Int = 0) : Parcelable {
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

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readLong(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(messageId)
        writeString(messageTitle)
        writeString(messageContent)
        writeInt(messageType)
        writeLong(scheduleTime)
        writeString(contacts)
        writeInt(sendCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MessageTitle> = object : Parcelable.Creator<MessageTitle> {
            override fun createFromParcel(source: Parcel): MessageTitle = MessageTitle(source)
            override fun newArray(size: Int): Array<MessageTitle?> = arrayOfNulls(size)
        }
    }
}