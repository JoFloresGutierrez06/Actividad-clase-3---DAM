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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.actividad1.Task
import java.text.SimpleDateFormat
import java.util.Locale

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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Ajusta automáticamente el número de columnas
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

                // Si quieres que desaparezcan al completarse, usa: val isVisible = !task.isCompleted
                val isVisible = true

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SwipeToDismissBox(
                        modifier = Modifier
                            .animateItem(
                                fadeInSpec = tween(300),
                                placementSpec = tween(300),
                                fadeOutSpec = tween(300)
                            ),
                        state = dismissState,
                        backgroundContent = {
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
}

@Composable
fun TaskItem(
    task: Task,
    onCompletedChange: (Boolean) -> Unit,
    onTaskClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (task.isCompleted) Color(0xFFE8E8E8) else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "taskContainerColor"
    )

    // Formateador de fecha corto
    val dateFormatter = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 1. Título
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isCompleted) Color.Gray else Color.Unspecified
            )

            // 2. Descripción (vista previa corta)
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // 3. Fila inferior: Fecha y Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormatter.format(task.dueDate),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCompletedChange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
