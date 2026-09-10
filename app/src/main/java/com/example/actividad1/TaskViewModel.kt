package com.example.actividad1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad1.AppDatabase
import com.example.actividad1.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val tasks: StateFlow<List<Task>>

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        tasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addTask(title: String, description: String, dueDate: Long) {
        viewModelScope.launch {
            repository.insert(
                Task(title = title, description = description, dueDate = dueDate)
            )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun setCompleted(task: Task, completed: Boolean) {
        updateTask(task.copy(isCompleted = completed))
    }
}