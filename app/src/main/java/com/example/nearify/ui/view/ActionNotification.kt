package com.example.nearify.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nearify.MainActivity
import com.example.nearify.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.nearify.db



class ActionNotification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.action_notification_actions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val table = findViewById<TableLayout>(R.id.notification_table)
        val loadingActionNotification = findViewById<TextView>(R.id.loading_action_notification)

        GlobalScope.launch(Dispatchers.IO) {
            val notifications = db.actionDao().getAllActions

               runOnUiThread() {
                val context = this@ActionNotification
                if (notifications.isNotEmpty()) {
                    notifications.forEach { x ->
                        val action = x.action
                        val device = x.device
                        // create row
                        val row = TableRow(context).apply {
                            val view = this@apply
                            layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            addViews(
                                // Message col
                                TextView(context).apply {
                                    text = action.message
                                },
                                // Where col
                                TextView(context).apply {
                                    text = if (action.onLeave) "Leave" else "Enter"
                                },
                                // Device name col
                                TextView(context).apply {
                                    val deviceNameDisplayText = device.name
                                    text = deviceNameDisplayText
                                },
                                // Remove col
                                Button(context).apply {
                                    val removeButton = "Remove"
                                    text = removeButton
                                    setOnClickListener {
                                        table.removeView(view)
                                        GlobalScope.launch(Dispatchers.IO) {
                                            db.actionDao().deleteAction(action)
                                        }
                                    }
                                })
                        }

                        table.addView(row)
                    }
                    table.visibility = TableLayout.VISIBLE
                    loadingActionNotification.visibility = TextView.GONE
                } else {
                    loadingActionNotification.text = "No Notifications"
                }
            }
        }
    }
}

private fun TableRow.addViews(vararg view: View) {
    view.forEach {
        this.addView(it)
    }
}


