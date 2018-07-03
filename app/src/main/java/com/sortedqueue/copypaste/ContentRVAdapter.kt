package com.sortedqueue.copypaste

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    inner class ViewHolder( itemView: View ) : RecyclerView.ViewHolder( itemView ), View.OnClickListener {
        override fun onClick(view: View?) {
            val position = adapterPosition
            if( position != RecyclerView.NO_POSITION ) {
                when( view?.id ) {
                    R.id.ivDelete -> mainView.removeTemplate( messageList[position] )
                    R.id.ivSend -> mainView.sendMessage( messageList[position] )
                    R.id.ivEdit -> mainView.addEditTemplate( messageList[position] )
                }
            }
        }

        val contentTv : TextView = itemView.findViewById(R.id.contentTv)
        val ivEdit : ImageView = itemView.findViewById(R.id.ivEdit)
        val ivDelete : ImageView = itemView.findViewById(R.id.ivDelete)
        val ivSend : ImageView = itemView.findViewById(R.id.ivSend)

        init {
            ivEdit.setOnClickListener( this )
            ivDelete.setOnClickListener( this )
            ivSend.setOnClickListener( this )
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