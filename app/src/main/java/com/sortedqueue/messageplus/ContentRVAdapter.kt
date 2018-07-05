package com.sortedqueue.messageplus

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sortedqueue.messageplus.base.DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Alok on 02/07/18.
 */
class ContentRVAdapter(private val messageList: ArrayList<MessageTitle>,
                       private val mainView: MainView) : RecyclerView.Adapter<ContentRVAdapter.ViewHolder>() {

    private val dateTimeFormat : SimpleDateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH)
    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent?.context).inflate(R.layout.item_message, parent, false) )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val messageTitle = messageList[position]
        holder?.contentTv?.text = messageTitle.messageContent
        if( messageTitle.scheduleTime != 0L ) {
            holder?.titleTv?.text = "Scheduled at : " + dateTimeFormat.format(Date(messageList[position].scheduleTime))
            if( messageTitle.scheduleTime < Calendar.getInstance().timeInMillis ) {
                holder?.ivAlarm?.setImageResource(R.drawable.ic_done_all_black_24dp)
            }
            else
                holder?.ivAlarm?.setImageResource(R.drawable.ic_alarm_on_black_24dp)

        }
        else {
            holder?.titleTv?.text = "Added on : " + dateTimeFormat.format(Date(messageList[position].messageId))
            holder?.ivAlarm?.setImageResource(R.drawable.ic_alarm_off_black_24dp)
        }

    }

    inner class ViewHolder( itemView: View ) : RecyclerView.ViewHolder( itemView ), View.OnClickListener {
        override fun onClick(view: View?) {
            val position = adapterPosition
            if( position != RecyclerView.NO_POSITION ) {
                when( view?.id ) {
                    R.id.ivDelete -> mainView.removeTemplate( messageList[position] )
                    R.id.ivSend -> mainView.sendMessage( messageList[position] )
                    R.id.ivAlarm -> mainView.setAlarm( messageList[position] )
                    R.id.contentTv, R.id.titleTv -> mainView.addEditTemplate( messageList[position] )
                }
            }
        }

        val contentTv : TextView = itemView.findViewById(R.id.contentTv)
        val titleTv : TextView = itemView.findViewById(R.id.titleTv)
        val ivDelete : ImageView = itemView.findViewById(R.id.ivDelete)
        val ivSend : ImageView = itemView.findViewById(R.id.ivSend)
        val ivAlarm : ImageView = itemView.findViewById(R.id.ivAlarm)

        init {
            ivAlarm.setOnClickListener( this )
            titleTv.setOnClickListener( this )
            contentTv.setOnClickListener( this )
            ivDelete.setOnClickListener( this )
            ivSend.setOnClickListener( this )
        }
    }

    fun addMessage(messageTitle: MessageTitle?) {

        val index = messageList.indexOf( messageTitle )
        if( index != -1 ) {
            messageList[index] = messageTitle!!
            notifyItemChanged(index)
        }
        else {
            messageList.add( messageTitle!! )
            notifyItemInserted(messageList.size - 1)
        }

    }

    fun removeMessage(messageTitle: MessageTitle) {
        val position = messageList.indexOf( messageTitle )
        if( position != -1 ) {
            messageList.remove(messageTitle)
            notifyItemRemoved( position )
        }
    }
}