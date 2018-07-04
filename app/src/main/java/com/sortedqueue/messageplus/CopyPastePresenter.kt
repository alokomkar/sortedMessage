package com.sortedqueue.messageplus

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.sortedqueue.messageplus.data.PreferencesView
import com.sortedqueue.messageplus.R

/**
 * Created by Alok on 02/07/18.
 */
@SuppressLint("StaticFieldLeak")
class CopyPastePresenter(private val mainView: MainView, private val preferencesView: PreferencesView) : PresenterView {

    @Synchronized override fun loadTemplates() {

        object : AsyncTask<Void, Void, ArrayList<MessageTitle>>() {

            override fun doInBackground(vararg p0: Void?): ArrayList<MessageTitle> {
                return preferencesView.getAllMessages()
            }

            override fun onCancelled() {
                super.onCancelled()
                mainView.hideProgress()
                mainView.showError(R.string.cancelled_error)
            }

            override fun onPostExecute(result: ArrayList<MessageTitle>?) {
                super.onPostExecute(result)
                mainView.hideProgress()
                if( result != null )
                    mainView.onSuccess(result)
            }

            override fun onPreExecute() {
                super.onPreExecute()
                mainView.showProgress(R.string.loading)
            }
        }.executeOnExecutor( AsyncTask.SERIAL_EXECUTOR )

    }
}