package com.sortedqueue.messageplus.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.sortedqueue.messageplus.MessageTitle
import com.sortedqueue.messageplus.base.MESSAGE_LIST

/**
 * Created by Alok on 02/07/18.
 */
class CPPreferenceManager( context : Context ) : PreferencesView {

    private val sharedPreference : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun removeMessage(messageTitle: MessageTitle) {
        val messageList = getMessageList()
        messageList.messageList.remove(messageTitle)
        sharedPreference.edit().putString(MESSAGE_LIST, Gson().toJson(messageList) ).apply()
    }

    override fun getAllMessages(): ArrayList<MessageTitle> {
        return getMessageList().messageList
    }

    override fun removeAllMessages() {
        sharedPreference.edit().putString(MESSAGE_LIST, "" ).apply()
    }

    override fun addMessage( messageTitle: MessageTitle) {
        val messageList = getMessageList()
        if( !messageList.messageList.contains(messageTitle) ) {
            messageList.messageList.add(messageTitle)
            sharedPreference.edit().putString(MESSAGE_LIST, Gson().toJson(messageList) ).apply()
        }
    }

    private fun getMessageList(): MessageList {
        val messages = sharedPreference.getString(MESSAGE_LIST, "" )
        return if( messages == "" ) MessageList() else Gson().fromJson(messages, MessageList::class.java)
    }

    inner class MessageList( var groupName : String = "default", var messageList : ArrayList<MessageTitle> = ArrayList())

}