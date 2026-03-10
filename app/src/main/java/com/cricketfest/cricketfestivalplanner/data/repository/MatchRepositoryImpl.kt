package com.cricketfest.cricketfestivalplanner.data.repository

import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchResultDao
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchResultEntity
import com.cricketfest.cricketfestivalplanner.data.model.Match
import com.cricketfest.cricketfestivalplanner.data.model.MatchResult
import com.cricketfest.cricketfestivalplanner.data.model.StandingsEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class MatchRepositoryImpl(
    private val matchDao: MatchDao,
    private val matchResultDao: MatchResultDao
) : MatchRepository {

    override fun getMatchesByTournament(tournamentId: Long): Flow<List<Match>> =
        matchDao.getMatchesByTournament(tournamentId).map { list -> list.map { it.toDomain() } }

    override suspend fun getMatchById(id: Long): Match? =
        matchDao.getMatchById(id)?.toDomain()

    override suspend fun insertMatch(match: Match): Long =
        matchDao.insertMatch(match.toEntity())

    override suspend fun insertMatches(matches: List<Match>) =
        matchDao.insertMatches(matches.map { it.toEntity() })

    override suspend fun updateMatch(match: Match) =
        matchDao.updateMatch(match.toEntity())

    override suspend fun deleteMatchesByTournament(tournamentId: Long) =
        matchDao.deleteMatchesByTournament(tournamentId)

    override suspend fun getMatchResult(matchId: Long): MatchResult? =
        matchResultDao.getResultByMatchId(matchId)?.toDomain()

    override suspend fun insertOrUpdateResult(result: MatchResult) =
        matchResultDao.insertOrUpdateResult(result.toEntity())

    override fun getResultsByTournament(tournamentId: Long): Flow<List<MatchResult>> =
        matchResultDao.getResultsByTournament(tournamentId).map { list -> list.map { it.toDomain() } }

    override fun getStandings(tournamentId: Long): Flow<List<StandingsEntry>> {
        return combine(
            matchDao.getMatchesByTournament(tournamentId),
            matchResultDao.getResultsByTournament(tournamentId)
        ) { matches, results ->
            val resultMap = results.associateBy { it.matchId }
            val completedMatches = matches.filter { it.isCompleted }

            val teamStats = mutableMapOf<Long, MutableList<Any>>()

            completedMatches.forEach { match ->
                val result = resultMap[match.id] ?: return@forEach
                val homeId = match.homeTeamId
                val awayId = match.awayTeamId

                if (!teamStats.containsKey(homeId)) {
                    teamStats[homeId] = mutableListOf(match.homeTeamName, 0, 0, 0, 0, 0)
                }
                if (!teamStats.containsKey(awayId)) {
                    teamStats[awayId] = mutableListOf(match.awayTeamName, 0, 0, 0, 0, 0)
                }

                val homeStats = teamStats[homeId]!!
                val awayStats = teamStats[awayId]!!

                // [teamName, played, won, lost, points, runDiff]
                homeStats[1] = (homeStats[1] as Int) + 1
                awayStats[1] = (awayStats[1] as Int) + 1

                val homeScore = result.homeScore
                val awayScore = result.awayScore

                if (homeScore > awayScore) {
                    homeStats[2] = (homeStats[2] as Int) + 1
                    homeStats[4] = (homeStats[4] as Int) + 2
                    awayStats[3] = (awayStats[3] as Int) + 1
                } else if (awayScore > homeScore) {
                    awayStats[2] = (awayStats[2] as Int) + 1
                    awayStats[4] = (awayStats[4] as Int) + 2
                    homeStats[3] = (homeStats[3] as Int) + 1
                } else {
                    homeStats[4] = (homeStats[4] as Int) + 1
                    awayStats[4] = (awayStats[4] as Int) + 1
                }

                homeStats[5] = (homeStats[5] as Int) + (homeScore - awayScore)
                awayStats[5] = (awayStats[5] as Int) + (awayScore - homeScore)
            }

            teamStats.map { (teamId, stats) ->
                StandingsEntry(
                    teamId = teamId,
                    teamName = stats[0] as String,
                    played = stats[1] as Int,
                    won = stats[2] as Int,
                    lost = stats[3] as Int,
                    points = stats[4] as Int,
                    runDifference = stats[5] as Int
                )
            }.sortedWith(compareByDescending<StandingsEntry> { it.points }.thenByDescending { it.runDifference })
        }
    }

    override suspend fun getAllMatchesOnce(): List<Match> =
        matchDao.getAllMatchesOnce().map { it.toDomain() }

    override suspend fun getAllResultsOnce(): List<MatchResult> =
        matchResultDao.getAllResultsOnce().map { it.toDomain() }

    private fun MatchEntity.toDomain() = Match(
        id = id, tournamentId = tournamentId, homeTeamId = homeTeamId,
        awayTeamId = awayTeamId, homeTeamName = homeTeamName,
        awayTeamName = awayTeamName, scheduledTime = scheduledTime,
        isCompleted = isCompleted, round = round
    )

    private fun Match.toEntity() = MatchEntity(
        id = id, tournamentId = tournamentId, homeTeamId = homeTeamId,
        awayTeamId = awayTeamId, homeTeamName = homeTeamName,
        awayTeamName = awayTeamName, scheduledTime = scheduledTime,
        isCompleted = isCompleted, round = round
    )

    private fun MatchResultEntity.toDomain() = MatchResult(
        id = id, matchId = matchId, homeScore = homeScore,
        awayScore = awayScore, bestPlayer = bestPlayer
    )

    private fun MatchResult.toEntity() = MatchResultEntity(
        id = id, matchId = matchId, homeScore = homeScore,
        awayScore = awayScore, bestPlayer = bestPlayer
    )
}
