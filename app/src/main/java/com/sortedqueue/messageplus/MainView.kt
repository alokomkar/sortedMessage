package com.sortedqueue.messageplus

import com.sortedqueue.messageplus.base.BaseView

/**
 * Created by Alok on 02/07/18.
 */
interface MainView : BaseView {
    fun onSuccess(allMessages: ArrayList<MessageTitle>)
    fun addEditTemplate( messageTitle: MessageTitle? )
    fun removeTemplate( messageTitle: MessageTitle)
    fun sendMessage( messageTitle: MessageTitle)
    fun setAlarm( messageTitle: MessageTitle )
}