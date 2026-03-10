package com.cricketfest.cricketfestivalplanner.data.model

data class Tournament(
    val id: Long = 0,
    val name: String,
    val matchType: String,
    val teamCount: Int,
    val pointSystem: String,
    val structure: String,
    val duration: String,
    val location: String = "",
    val posterImagePath: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
