package com.example.actividad1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.actividad1.ui.theme.Actividad1Theme
import com.example.actividad1.screens.TaskListScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad1.screens.TaskDetailScreen
import com.example.actividad1.screens.CreateTaskScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Actividad1Theme {

                val navController = rememberNavController()
                val viewModel: TaskViewModel = viewModel()
                val tasks by viewModel.tasks.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "lista"
                ) {

                    composable("lista") {
                        TaskListScreen(
                            tasks = tasks,
                            onCompletedChange = { task, completed ->
                                viewModel.setCompleted(task, completed)
                            },
                            onTaskClick = { task ->
                                navController.navigate("detalle/${task.id}")
                            },
                            onCreateTask = {
                                navController.navigate("crear")
                            }
                        )
                    }

                    composable("crear") {
                        CreateTaskScreen(
                            onSave = { title, description, dueDate ->
                                viewModel.addTask(title, description, dueDate)
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("detalle/{id}") {
                        val id = it.arguments?.getString("id")?.toIntOrNull()
                        val task = tasks.find { t -> t.id == id }

                        if (task != null) {
                            TaskDetailScreen(
                                task = task,
                                onBack = { navController.popBackStack() },
                                onDelete = {
                                    viewModel.deleteTask(task)
                                    navController.popBackStack()
                                },
                                onEdit = { newTitle, newDescription, newDueDate ->
                                    viewModel.updateTask(
                                        task.copy(
                                            title = newTitle,
                                            description = newDescription,
                                            dueDate = newDueDate
                                        )
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}