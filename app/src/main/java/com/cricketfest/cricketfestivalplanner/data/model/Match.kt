package com.cricketfest.cricketfestivalplanner.data.model

data class Match(
    val id: Long = 0,
    val tournamentId: Long,
    val homeTeamId: Long,
    val awayTeamId: Long,
    val homeTeamName: String = "",
    val awayTeamName: String = "",
    val scheduledTime: Long,
    val isCompleted: Boolean = false,
    val round: Int = 1
)
