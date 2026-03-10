package com.cricketfest.cricketfestivalplanner.utils

import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.data.model.Team

object ScheduleGenerator {

    /**
     * Generates a round-robin schedule for the given teams.
     * Each team plays every other team once.
     */
    fun generateRoundRobin(tournamentId: Long, teams: List<Team>): List<Match> {
        val matches = mutableListOf<Match>()
        val baseTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000L // start tomorrow
        val matchIntervalMs = 2 * 60 * 60 * 1000L // 2 hours between matches
        var matchIndex = 0

        for (round in 1..teams.size - 1) {
            val roundTeams = teams.toMutableList()
            // Rotate for round-robin scheduling
            val rotated = mutableListOf<Team>()
            rotated.add(roundTeams[0])
            for (i in 1 until roundTeams.size) {
                rotated.add(roundTeams[((i - 1 + round) % (roundTeams.size - 1)) + 1])
            }
            for (i in 0 until rotated.size / 2) {
                val home = rotated[i]
                val away = rotated[rotated.size - 1 - i]
                matches.add(
                    Match(
                        tournamentId = tournamentId,
                        homeTeamId = home.id,
                        awayTeamId = away.id,
                        homeTeamName = home.name,
                        awayTeamName = away.name,
                        scheduledTime = baseTime + matchIndex * matchIntervalMs,
                        round = round
                    )
                )
                matchIndex++
            }
        }
        return matches
    }

    /**
     * Generates a simple knockout/playoff bracket.
     */
    fun generatePlayoffs(tournamentId: Long, teams: List<Team>): List<Match> {
        val matches = mutableListOf<Match>()
        val baseTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
        val matchIntervalMs = 3 * 60 * 60 * 1000L
        val shuffled = teams.shuffled()

        for (i in shuffled.indices step 2) {
            if (i + 1 < shuffled.size) {
                matches.add(
                    Match(
                        tournamentId = tournamentId,
                        homeTeamId = shuffled[i].id,
                        awayTeamId = shuffled[i + 1].id,
                        homeTeamName = shuffled[i].name,
                        awayTeamName = shuffled[i + 1].name,
                        scheduledTime = baseTime + (i / 2) * matchIntervalMs,
                        round = 1
                    )
                )
            }
        }
        return matches
    }

    fun generateSchedule(tournamentId: Long, teams: List<Team>, structure: String): List<Match> {
        return when (structure) {
            "Playoffs" -> generatePlayoffs(tournamentId, teams)
            else -> generateRoundRobin(tournamentId, teams)
        }
    }
}
