package com.sortedqueue.copypaste.base

import android.view.View

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