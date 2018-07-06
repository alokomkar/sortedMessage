package com.sortedqueue.messageplus.utils

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sortedqueue.messageplus.MessageTitle
import com.sortedqueue.messageplus.R
import com.sortedqueue.messageplus.base.MESSAGE_LIST
import com.sortedqueue.messageplus.base.MessageListener
import kotlinx.android.synthetic.main.fragment_message.*
import android.provider.ContactsContract
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.database.Cursor
import android.util.Log


/**
 * Created by Alok on 06/07/18.
 */
class MessageFragment : DialogFragment() {

    private lateinit var messageListener: MessageListener
    private lateinit var messageTitle : MessageTitle

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_message, container, false)
    }

    private val RESULT_PICK_CONTACT: Int = 43232

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageTitle = arguments.getParcelable<MessageTitle>(MESSAGE_LIST)

        etMessage.setText( messageTitle.messageContent )

        (tvSave).setOnClickListener {
            messageTitle.messageContent = etMessage.text.toString()
            messageListener.onSuccess( messageTitle )
        }
        (tvCancel).setOnClickListener {
            messageListener.onCancel()
        }
        (tvClear).setOnClickListener {
            etMessage.setText("")
        }
        ivChoose.setOnClickListener {
            val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult ( contactPickerIntent, RESULT_PICK_CONTACT)
        }

    }

    override fun onStart() {
        super.onStart()
        setFullScreen()
    }

    private fun setFullScreen() {
        val d = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window.setLayout(width, height)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_PICK_CONTACT -> {
                    val cursor: Cursor?
                    try {
                        var phoneNo: String? = null
                        var name: String? = null
                        val uri = data!!.data
                        cursor = context.contentResolver.query(uri, null, null, null, null)
                        cursor!!.moveToFirst()
                        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        phoneNo = cursor.getString(phoneIndex)
                        name = cursor.getString(nameIndex)
                        etContacts.append("$name : $phoneNo, ")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } else {
            Log.e("Failed", "Not able to pick contact")
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if( context is MessageListener )
            messageListener = context
    }
}