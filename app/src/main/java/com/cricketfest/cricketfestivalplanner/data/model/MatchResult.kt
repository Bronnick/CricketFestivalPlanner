package com.cricketfest.cricketfestivalplanner.data.model

data class MatchResult(
    val id: Long = 0,
    val matchId: Long,
    val homeScore: Int,
    val awayScore: Int,
    val bestPlayer: String = ""
)
