package com.sortedqueue.messageplus.data

import com.sortedqueue.messageplus.MessageTitle

/**
 * Created by Alok on 02/07/18.
 */
interface PreferencesView {
    fun addMessage( messageTitle: MessageTitle)
    fun removeMessage( messageTitle: MessageTitle)
    fun getAllMessages() : ArrayList<MessageTitle>
    fun removeAllMessages()
}