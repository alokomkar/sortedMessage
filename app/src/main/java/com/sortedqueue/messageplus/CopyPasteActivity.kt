package com.sortedqueue.messageplus

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.sortedqueue.messageplus.data.PreferencesView

import kotlinx.android.synthetic.main.activity_copy_paste.*
import kotlinx.android.synthetic.main.content_copy_paste.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.sortedqueue.messageplus.base.*
import com.sortedqueue.messageplus.utils.DatePickerFragment
import com.sortedqueue.messageplus.utils.TimePickerFragment
import java.util.*


class CopyPasteActivity : AppCompatActivity(), MainView, MessageListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        currentMessageTitle?.scheduleTime = calendar.timeInMillis
        CPJob.cancelJobById( currentMessageTitle!! )
        CPJob.scheduleJob( currentMessageTitle!!, Calendar.getInstance() )
        checkForPermission()
        onSuccess(currentMessageTitle!!)
    }

    private val PERMISSIONS_REQUEST: Int = 12312

    private fun checkForPermission() {
        val permissionList = arrayListOf<String>(Manifest.permission.SEND_SMS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionList.toTypedArray(), PERMISSIONS_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this@CopyPasteActivity, "Some permissions were denied", Toast.LENGTH_LONG).show()
                checkForPermission()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        calendar.set( year, month, day )
        currentMessageTitle?.scheduleTime = calendar.timeInMillis
        scheduleTime()
    }

    private fun scheduleTime() {
        val timePickerFragment = TimePickerFragment()
        val bundle = Bundle()
        bundle.putLong( CURRENT_TIME, currentMessageTitle!!.scheduleTime )
        timePickerFragment.arguments = bundle
        timePickerFragment.show( supportFragmentManager, "timePicker" )
    }

    private val calendar = Calendar.getInstance()


    override fun onSuccess(messageTitle: MessageTitle) {
        preferencesView.addMessage( messageTitle )
        if( contentAdapter != null )
            contentAdapter?.addMessage( messageTitle )
    }

    override fun onCancel() {

    }


    private lateinit var presenterView : PresenterView
    private lateinit var preferencesView : PreferencesView
    private var contentAdapter : ContentRVAdapter?= null

    override fun showProgress( message: Int ) {

    }

    override fun onSuccess( allMessages: ArrayList<MessageTitle> ) {
        ivSplash.hide()
        refreshLayout.isRefreshing = false
        contentAdapter = ContentRVAdapter( allMessages, this )
        contentRecyclerView.adapter = contentAdapter
    }

    override fun hideProgress() {

    }

    override fun showError(errorMessage: Int) {

    }

    override fun sendMessage(messageTitle: MessageTitle) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, messageTitle.messageContent)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun addEditTemplate( messageTitle: MessageTitle? ) {
        this.showInputDialog( messageTitle!!, this )
    }

    private var currentMessageTitle: MessageTitle ?= null

    override fun setAlarm(messageTitle: MessageTitle) {
        val datePickerFragment = DatePickerFragment()
        currentMessageTitle = messageTitle
        val bundle = Bundle()
        if( currentMessageTitle!!.scheduleTime == 0L ) currentMessageTitle!!.scheduleTime = Date().time
        bundle.putLong( CURRENT_TIME, currentMessageTitle!!.scheduleTime )
        datePickerFragment.arguments = bundle
        datePickerFragment.show( supportFragmentManager, "datePicker" )
    }

    override fun removeTemplate( messageTitle: MessageTitle) {
        if( contentAdapter != null ) {
            CPJob.cancelJobById( messageTitle )
            contentAdapter?.removeMessage( messageTitle )
            preferencesView.removeMessage( messageTitle )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy_paste)
        setSupportActionBar(toolbar)

        preferencesView = CPApplication.getPreferences()

        presenterView = CopyPastePresenter(this, preferencesView)
        presenterView.loadTemplates()

        contentRecyclerView.layoutManager = LinearLayoutManager( this )
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = true
            presenterView.loadTemplates()
        }

        fab.setOnClickListener {
            addEditTemplate( MessageTitle(System.currentTimeMillis(), "", "", TYPE_TEXT, 0) )
        }

        handleShare()
    }

    private fun handleShare() {
        if ( intent != null && Intent.ACTION_SEND == intent.action && intent.type != null ) {
            if ("text/plain" == intent.type) {
                addEditTemplate(MessageTitle(System.currentTimeMillis(), "", intent.getStringExtra(Intent.EXTRA_TEXT), TYPE_TEXT, 0))
            }
        }
    }

    inner class UpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_copy_paste, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
