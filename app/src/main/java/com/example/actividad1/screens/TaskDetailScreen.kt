package com.example.actividad1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.actividad1.Task
import com.example.actividad1.getTaskStatus

@Composable
fun TaskDetailScreen(
    task: Task,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var title by remember {
        mutableStateOf(task.title)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            title = "Detalles de la tarea",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Título",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Estado",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 24.dp,
                        bottom = 8.dp
                    )
            )

            Text(
                text = getTaskStatus(task.isCompleted),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onEdit(title)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Guardar cambios")
            }

            TextButton(
                onClick = onDelete
            ) {
                Text("Eliminar")
            }
        }
    }
}