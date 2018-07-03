package com.sortedqueue.copypaste

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.sortedqueue.copypaste.base.TYPE_TEXT
import com.sortedqueue.copypaste.base.hide
import com.sortedqueue.copypaste.data.PreferencesView

import kotlinx.android.synthetic.main.activity_copy_paste.*
import kotlinx.android.synthetic.main.content_copy_paste.*

class CopyPasteActivity : AppCompatActivity(), MainView {

    private lateinit var presenterView : PresenterView
    private lateinit var preferencesView : PreferencesView
    private var contentAdapter : ContentRVAdapter ?= null

    override fun showProgress( message: Int ) {

    }

    override fun onSuccess( allMessages: ArrayList<MessageTitle> ) {
        ivSplash.hide()
        contentAdapter = ContentRVAdapter( allMessages, this )
        contentRecyclerView.adapter = contentAdapter
    }

    override fun hideProgress() {

    }

    override fun showError(errorMessage: Int) {

    }

    override fun addEditTemplate( messageTitle: MessageTitle? ) {
        if( contentAdapter != null && messageTitle != null )
            contentAdapter?.addMessage( messageTitle )
    }

    override fun removeTemplate( messageTitle: MessageTitle ) {
        if( contentAdapter != null )
            contentAdapter?.removeMessage( messageTitle )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy_paste)
        setSupportActionBar(toolbar)

        preferencesView = CPApplication.getPreferences()

        presenterView = CopyPastePresenter( this, preferencesView )
        presenterView.loadTemplates()

        contentRecyclerView.layoutManager = LinearLayoutManager( this )

        fab.setOnClickListener { view ->
            addEditTemplate( MessageTitle(System.currentTimeMillis(), "New Message " + System.currentTimeMillis(), TYPE_TEXT, 0) )
            Snackbar.make(view, "Message added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
