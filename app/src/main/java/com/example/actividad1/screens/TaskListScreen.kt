package com.example.actividad1.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.actividad1.Task

@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onCompletedChange: (Task, Boolean) -> Unit,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onCreateTask: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = "Mis tareas")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTask,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 24.dp,
                vertical = 12.dp
            )
        ) {
            items(tasks, key = { it.id }) { task ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            onDeleteTask(task)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    // Animación de tamaño/posición al insertar, eliminar o reordenar
                    // elementos de la lista (p. ej. al completar una tarea y que
                    // cambie de orden, o al añadir/eliminar una tarea).
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(300),
                        placementSpec = tween(300),
                        fadeOutSpec = tween(300)
                    ),
                    state = dismissState,
                    backgroundContent = {
                        // Color animado del fondo mientras se desliza para borrar
                        val color by animateColorAsState(
                            targetValue = when (dismissState.dismissDirection) {
                                SwipeToDismissBoxValue.EndToStart -> Color.Red
                                else -> Color.Transparent
                            },
                            animationSpec = tween(200),
                            label = "swipeBackgroundColor"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 6.dp)
                                .background(color, CardDefaults.shape),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    },
                    enableDismissFromStartToEnd = false
                ) {
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
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCompletedChange: (Boolean) -> Unit,
    onTaskClick: () -> Unit
) {
    // Color de fondo animado al marcar/desmarcar una tarea como completada
    val containerColor by animateColorAsState(
        targetValue = if (task.isCompleted) Color(0xFFE8E8E8) else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "taskContainerColor"
    )

    // Opacidad animada del texto al completar la tarea
    val textAlpha by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.6f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "taskTextAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onTaskClick()
            }
            // Anima suavemente cualquier cambio de tamaño del contenido de la tarjeta
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCompletedChange
            )

            Text(
                text = task.title,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                textDecoration = if (task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                color = if (task.isCompleted) {
                    Color.Gray.copy(alpha = textAlpha)
                } else {
                    Color.Unspecified
                }
            )

            // Icono que aparece/desaparece con una animación al completar la tarea
            AnimatedVisibility(
                visible = task.isCompleted,
                enter = fadeIn(tween(250)) + scaleIn(tween(250)),
                exit = fadeOut(tween(200)) + scaleOut(tween(200))
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completada",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                )
            }
        }
    }
}