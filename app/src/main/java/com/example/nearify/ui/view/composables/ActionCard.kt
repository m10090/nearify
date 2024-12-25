package com.example.nearify.ui.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearify.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun ActionCard(
    message: String,
    isOnSight: Boolean,
    isOnLeave: Boolean,
    id: Int
) {
    var done = remember { mutableStateOf(false) }
    if (done.value) {
        return
    }
    val purpleGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A),  // Darker Purple
            Color(0xFF9C27B0) // Light Purple
        )
    )
    val onSightLeaveColor = Color(0xFF8E24BB)
    val endNearifyColor = Color(0xFF8E24AA)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(purpleGradient)
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            MessageSection(message)
            TypeSection(isOnSight, isOnLeave, onSightLeaveColor)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            // delete action
                            db.actionDao().deleteAction(id)
                        }
                        done.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = endNearifyColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(48.dp)
                    )
                ) {
                    Text(text = "End Nearify")
                }
            }
        }
    }
}

@Composable
private fun MessageSection(message: String) {
    Text(
        text = "Message:",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Box(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun TypeSection(isOnSight: Boolean, isOnLeave: Boolean, buttonColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Type:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp)
        )
        if (isOnSight) {
            ActionButton("OnSight", buttonColor)
        }
        if (isOnLeave) {
            ActionButton("OnLeave", buttonColor)
        }
    }
}

@Composable
private fun ActionButton(text: String, color: Color) {
    Button(
        onClick = { /* Handle action */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}

@Composable
private fun CenteredDeleteButton(id: Int, buttonColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    // delete action
                    db.actionDao().deleteAction(id)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            ),
            modifier = Modifier.border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(48.dp)
            )
        ) {
            Text(text = "End Nearify")
        }
    }
}