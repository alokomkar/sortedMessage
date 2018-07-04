package com.sortedqueue.messageplus.base

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.sortedqueue.messageplus.MessageTitle
import android.widget.EditText
import android.view.LayoutInflater
import android.widget.TextView
import com.sortedqueue.messageplus.R


/**
 * Created by Alok on 03/07/18.
 */
fun View.isVisible() = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.getString(res: Int) = resources.getString(res)

fun View.toggleVisibility() {
    if( this.isVisible() ) this.hide() else this.show()
}

interface MessageListener {
    fun onSuccess( messageTitle: MessageTitle)
    fun onCancel()
}

fun Context.showInputDialog(messageTitle: MessageTitle, messageListener: MessageListener) {

    val dialogBuilder = AlertDialog.Builder(this)

    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input, null)
    dialogBuilder.setView(dialogView)

    val editText = dialogView.findViewById<EditText>(R.id.etMessage)
    editText.setText( messageTitle.messageContent )

    val alertDialog = dialogBuilder.create()
    dialogView.findViewById<TextView>(R.id.tvSave).setOnClickListener {
        messageTitle.messageContent = editText.text.toString()
        messageListener.onSuccess( messageTitle )
        alertDialog.dismiss()
    }
    dialogView.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
        messageListener.onCancel()
        alertDialog.dismiss()
    }
    alertDialog.show()

}