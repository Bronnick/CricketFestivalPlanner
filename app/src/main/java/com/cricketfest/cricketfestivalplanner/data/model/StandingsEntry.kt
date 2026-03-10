package com.cricketfest.cricketfestivalplanner.data.model

data class StandingsEntry(
    val teamId: Long,
    val teamName: String,
    val played: Int,
    val won: Int,
    val lost: Int,
    val points: Int,
    val runDifference: Int
)
