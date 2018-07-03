package com.sortedqueue.copypaste.data

import com.sortedqueue.copypaste.MessageTitle

/**
 * Created by Alok on 02/07/18.
 */
interface PreferencesView {
    fun addMessage( messageTitle: MessageTitle)
    fun removeMessage( messageTitle: MessageTitle)
    fun getAllMessages() : ArrayList<MessageTitle>
    fun removeAllMessages()
}