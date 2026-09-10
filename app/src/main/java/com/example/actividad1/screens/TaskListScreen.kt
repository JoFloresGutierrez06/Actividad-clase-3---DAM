package com.example.actividad1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.actividad1.Task
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Modifier
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onCompletedChange: (Task, Boolean) -> Unit, // "MainActivity, tú tienes los datos. Yo solamente te aviso cuando el usuario haga algo."
    onTaskClick: (Task) -> Unit,
    onCreateTask: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TopBar(
                title = "Mis tareas"
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {

                items(tasks) { task ->

                    TaskItem(
                        task = task,
                        onCompletedChange = { completed ->
                            onCompletedChange(task, completed)
                        },
                        onTaskClick = {
                            onTaskClick(task)
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onCreateTask,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text("+")
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCompletedChange: (Boolean) -> Unit,
    onTaskClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onTaskClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                Color(0xFFE8E8E8)
            } else {
                Color.White
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Switch(
                checked = task.isCompleted,
                onCheckedChange = onCompletedChange
            )

            Text(
                text = task.title,
                modifier = Modifier.padding(start = 12.dp),
                textDecoration = if (task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                color = if (task.isCompleted) {
                    Color.Gray.copy(alpha = 0.6f)
                } else {
                    Color.Unspecified
                }
            )
        }
    }
}