package com.sortedqueue.copypaste

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Alok on 02/07/18.
 */
class ContentRVAdapter( private val messageList: ArrayList<MessageTitle>,
                        private val mainView: MainView ) : RecyclerView.Adapter<ContentRVAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent?.context).inflate(R.layout.item_message, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.contentTv?.text = messageList[position].messageContent
    }

    inner class ViewHolder( itemView: View ) : RecyclerView.ViewHolder( itemView ) {

        val contentTv : TextView = itemView.findViewById(R.id.contentTv)

        init {

        }
    }

    fun addMessage(messageTitle: MessageTitle?) {
        messageList.add(messageTitle!!)
        notifyItemInserted(messageList.size - 1)
    }

    fun removeMessage(messageTitle: MessageTitle) {
        val position = messageList.indexOf( messageTitle )
        if( position != -1 ) {
            messageList.remove(messageTitle)
            notifyItemRemoved( position )
        }
    }
}