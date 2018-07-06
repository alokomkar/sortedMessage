package com.sortedqueue.messageplus.base

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.AsyncTask
import android.os.Build

import android.support.v4.app.NotificationCompat
import android.telephony.SmsManager
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.google.gson.Gson
import com.sortedqueue.messageplus.CopyPasteActivity
import com.sortedqueue.messageplus.MessageTitle
import com.sortedqueue.messageplus.R
import java.util.*

/**
 * Created by Alok on 05/07/18.
 */
@SuppressLint("StaticFieldLeak")
class CPJob : Job() {

    override fun onRunJob(params: Params): Result {
        val gson = Gson()
        if( params.extras.containsKey(MESSAGE_LIST)) {
            val item = gson.fromJson(params.extras[MESSAGE_LIST] as String, MessageTitle::class.java)
            showNotification( "Alert : " + item.messageTitle, item.messageContent , item )
        }
        return Result.SUCCESS
    }


    private fun showNotification(title: String, desc: String?, item: MessageTitle) {

        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, CopyPasteActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder
        val channelId = "Reminders"
        if( Build.VERSION.SDK_INT == Build.VERSION_CODES.O ) {

            val name = title
            val description = desc
            val mChannel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)

            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)

            notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.mipmap.ic_launcher) //NotificationModel icon
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setChannelId(channelId)
                    .setWhen(0)
                    .setSound(defaultSoundUri)

        }
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationBuilder = NotificationCompat.Builder(context, channelId)
                        .setAutoCancel(true)   //Automatically delete the notification
                        .setSmallIcon(R.mipmap.ic_launcher) //NotificationModel icon
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(desc)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setWhen(0)
                        .setSound(defaultSoundUri)
            } else {

                notificationBuilder = NotificationCompat.Builder(context,channelId)
                        .setAutoCancel(true)   //Automatically delete the notification
                        .setSmallIcon(R.mipmap.ic_launcher) //NotificationModel icon
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(desc)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setWhen(0)
                        .setSound(defaultSoundUri)

            }


        val smsManager = SmsManager.getDefault()
        for( contact in item.contacts.split(",") ) {
            try {
                if( contact.contains(":") ) {
                    smsManager.sendTextMessage(contact.split(":")[1].replace(" ", ""), null, desc, null, null);
                }
                else {
                    if( contact.trim().isNotEmpty() )
                        smsManager.sendTextMessage(contact.replace(" ", ""), null, desc, null, null);
                }
            } catch (e : Exception) {
                e.printStackTrace()
                continue
            }
        }



        notificationManager.notify(channelId.hashCode(), notificationBuilder.build())
    }


    companion object {

        fun cancelJobById(id: MessageTitle) {
            object : AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg p0: Void?) {
                    JobManager.instance().cancelAllForTag(REMINDER_TAG + id.messageId )
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

        fun cancelAllJobs() {
            object : AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg p0: Void?) {
                    JobManager.instance().cancelAll()
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        }


        fun scheduleJob( reminder: MessageTitle, calendar: Calendar ) {

            val reminderString = Gson().toJson(reminder)
            val extras = PersistableBundleCompat()
            extras.putString(MESSAGE_LIST, reminderString)

            object: AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg p0: Void?) {
                    if( reminder.scheduleTime > calendar.timeInMillis ) {
                        JobRequest.Builder(REMINDER_TAG + reminder.messageId )
                                .setExact((reminder.scheduleTime - calendar.timeInMillis))
                                .setRequiresDeviceIdle(false)
                                .setExtras(extras)
                                .build()
                                .schedule()
                    }
                }

                override fun onPostExecute(result: Unit?) {
                    super.onPostExecute(result)

                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        }
    }
}