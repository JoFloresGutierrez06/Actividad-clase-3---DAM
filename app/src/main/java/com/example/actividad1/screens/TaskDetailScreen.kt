package com.example.actividad1.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.actividad1.Task
import com.example.actividad1.getTaskStatus
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (title: String, description: String, dueDate: Long, isCompleted: Boolean) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

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
                text = "NOTA " + task.id + ". " + task.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón de Fecha
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp), // Esquinas suaves
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD2D2D2), // Color del fondo del botón
                        contentColor = Color(0xFF808080)          // Color del texto/iconos dentro
                    )
                ) {
                    Text(text = dateFormatter.format(dueDate))
                }

                // Checkbox de Estado
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isCompleted = !isCompleted }
                ) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                }
            }

            // Botón de guardar
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onEdit(title, description, dueDate, isCompleted)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp), // Esquinas suaves
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF808080), // Color del fondo del botón
                        contentColor = Color.White          // Color del texto/iconos dentro
                    )
            ) {
                Text("Guardar cambios")
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedBorderColor = Color(0xFF808080),
                    unfocusedBorderColor = Color(0xFFD2D2D2),
                    focusedTextColor = Color(0xFF808080),
                    unfocusedTextColor = Color(0xFF808080)
                )
            )

        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dueDate = it }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}