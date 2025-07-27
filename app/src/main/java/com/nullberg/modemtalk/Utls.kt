package com.nullberg.modemtalk

// Some useful utilities (Utls) for the project.
// Note, this is like a static Java class (object in Kotlin)

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

object Utls {

    fun custAlertDialog(context: Context, strMsg: String) {

        AlertDialog.Builder(context)
            .setMessage(strMsg)
            .setPositiveButton("OK", null)
            .show()
    }

    fun simpleAlertDialog(context: Context) {

        AlertDialog.Builder(context)
            .setTitle("Hello!")
            .setMessage("This is a simple AlertDialog.")
            .setPositiveButton("OK", null)
            .show()

    }

    fun populateTable(context: Context, tableLayout: TableLayout, labels: List<String>, values: List<String>) {

        tableLayout.removeAllViews()  // clear any existing rows

        for (i in labels.indices) {

            val row       = TableRow(context)
            val labelView = TextView(context)
            val valueView = TextView(context)

            labelView.apply {
                text = labels[i]
                textSize = 12f
                isSingleLine = false
                setSingleLine(false)
                setHorizontallyScrolling(false)
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            valueView.apply {
                text = values[i]
                textSize = 12f
                textAlignment = View.TEXT_ALIGNMENT_VIEW_END

                // THESE are essential apparently
                layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                isSingleLine = false
                setSingleLine(false)
                setLineSpacing(0f, 1.1f)
                setHorizontallyScrolling(false)
                ellipsize = null  // Allow full wrap

            }

            row.addView(labelView)
            row.addView(valueView)
            tableLayout.addView(row)
        }
    }
}