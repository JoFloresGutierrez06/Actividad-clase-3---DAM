package com.example.actividad1

/*
data class Task(
    val id: Int,
    var title: String,
    var descripcion: String,
    var completed: Boolean,
    var dueDate: Long
) */

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val dueDate: Long
)
