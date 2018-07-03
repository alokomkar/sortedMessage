package com.sortedqueue.copypaste.base

/**
 * Created by Alok on 02/07/18.
 */
interface BaseView {

    fun showProgress( message : Int )
    fun hideProgress()
    fun showError( errorMessage : Int )

}