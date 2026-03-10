package com.cricketfest.cricketfestivalplanner.data.repository

import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.data.model.MatchResult
import com.cricketfest.cricketfestivalplanner.data.model.StandingsEntry
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun getMatchesByTournament(tournamentId: Long): Flow<List<Match>>
    suspend fun getMatchById(id: Long): Match?
    suspend fun insertMatch(match: Match): Long
    suspend fun insertMatches(matches: List<Match>)
    suspend fun updateMatch(match: Match)
    suspend fun deleteMatchesByTournament(tournamentId: Long)
    suspend fun getMatchResult(matchId: Long): MatchResult?
    suspend fun insertOrUpdateResult(result: MatchResult)
    fun getResultsByTournament(tournamentId: Long): Flow<List<MatchResult>>
    fun getStandings(tournamentId: Long): Flow<List<StandingsEntry>>
    suspend fun getAllMatchesOnce(): List<Match>
    suspend fun getAllResultsOnce(): List<MatchResult>
}
