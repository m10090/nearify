package com.example.nearify

import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.marginLeft
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class ActionNotification : AppCompatActivity() {
    val db = MainActivity.db
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
        val loading_action_notification = findViewById<TextView>(R.id.loading_action_notification)
        GlobalScope.launch(Dispatchers.IO) {

            val notifications = db.actionDao().getAllActions
            withContext(Dispatchers.Main) {
                val context = this@ActionNotification
                if (!notifications.isEmpty()) {
                    notifications.forEach { x ->
                        val action=x.action
                        val device=x.device
                        // create row
                        val row = TableRow(context).apply {
                            layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                        }

                        // Message col
                        row.addView(TextView(context).apply {
                            text = action.message
                        })

                        // Where col
                        row.addView(TextView(context).apply {
                            text = if (action.onLeave) "Leave" else "Enter"
                        })
                        row.addView(TextView(context).apply{
                            text = "device name:" + device.name

                        })
                        // Remove col
                        row.addView(Button(context).apply {
                            text = "Remove"
                            setOnClickListener {
                                table.removeView(row)
                                GlobalScope.launch (Dispatchers.IO){
                                    db.actionDao().deleteAction(action)
                                }
                            }
                        })
                        table.addView(row)
                    }
                    table.visibility = TableLayout.VISIBLE
                    loading_action_notification.visibility = TextView.GONE
                } else {
                    loading_action_notification.text = "No Notifications"
                }
            }
        }
    }
}
