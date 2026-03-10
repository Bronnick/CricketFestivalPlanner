package com.cricketfest.cricketfestivalplanner.utils

import android.content.Context
import android.net.Uri
import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchResultDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.TeamDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.TournamentDao
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchResultEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.TeamEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.TournamentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class DataBackupManager(
    private val context: Context,
    private val tournamentDao: TournamentDao,
    private val teamDao: TeamDao,
    private val matchDao: MatchDao,
    private val matchResultDao: MatchResultDao
) {
    suspend fun clearAll(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            matchResultDao.deleteAll()
            matchDao.deleteAll()
            teamDao.deleteAll()
            tournamentDao.deleteAll()
        }
    }

    suspend fun exportToUri(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val tournaments = tournamentDao.getAllTournamentsOnce()
            val teams = teamDao.getAllTeamsOnce()
            val matches = matchDao.getAllMatchesOnce()
            val results = matchResultDao.getAllResultsOnce()

            val root = JSONObject().apply {
                put("version", 1)
                put("exportedAt", System.currentTimeMillis())
                put("tournaments", JSONArray().apply {
                    tournaments.forEach { t ->
                        put(JSONObject().apply {
                            put("id", t.id)
                            put("name", t.name)
                            put("matchType", t.matchType)
                            put("teamCount", t.teamCount)
                            put("pointSystem", t.pointSystem)
                            put("structure", t.structure)
                            put("duration", t.duration)
                            put("location", t.location)
                            put("posterImagePath", t.posterImagePath)
                            put("isCompleted", t.isCompleted)
                            put("createdAt", t.createdAt)
                        })
                    }
                })
                put("teams", JSONArray().apply {
                    teams.forEach { t ->
                        put(JSONObject().apply {
                            put("id", t.id)
                            put("name", t.name)
                            put("tournamentId", t.tournamentId)
                        })
                    }
                })
                put("matches", JSONArray().apply {
                    matches.forEach { m ->
                        put(JSONObject().apply {
                            put("id", m.id)
                            put("tournamentId", m.tournamentId)
                            put("homeTeamId", m.homeTeamId)
                            put("awayTeamId", m.awayTeamId)
                            put("homeTeamName", m.homeTeamName)
                            put("awayTeamName", m.awayTeamName)
                            put("scheduledTime", m.scheduledTime)
                            put("isCompleted", m.isCompleted)
                            put("round", m.round)
                        })
                    }
                })
                put("matchResults", JSONArray().apply {
                    results.forEach { r ->
                        put(JSONObject().apply {
                            put("id", r.id)
                            put("matchId", r.matchId)
                            put("homeScore", r.homeScore)
                            put("awayScore", r.awayScore)
                            put("bestPlayer", r.bestPlayer)
                        })
                    }
                })
            }

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(root.toString(2).toByteArray(Charsets.UTF_8))
            } ?: error("Cannot open output stream")
        }
    }

    suspend fun importFromUri(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val json = context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.readBytes().toString(Charsets.UTF_8)
            } ?: error("Cannot open input stream")

            val root = JSONObject(json)

            val tournamentsArr = root.getJSONArray("tournaments")
            for (i in 0 until tournamentsArr.length()) {
                val t = tournamentsArr.getJSONObject(i)
                tournamentDao.insertTournament(
                    TournamentEntity(
                        id = t.getLong("id"),
                        name = t.getString("name"),
                        matchType = t.getString("matchType"),
                        teamCount = t.getInt("teamCount"),
                        pointSystem = t.getString("pointSystem"),
                        structure = t.getString("structure"),
                        duration = t.getString("duration"),
                        location = t.optString("location", ""),
                        posterImagePath = t.optString("posterImagePath", ""),
                        isCompleted = t.getBoolean("isCompleted"),
                        createdAt = t.getLong("createdAt")
                    )
                )
            }

            val teamsArr = root.getJSONArray("teams")
            teamDao.insertTeams((0 until teamsArr.length()).map { i ->
                val t = teamsArr.getJSONObject(i)
                TeamEntity(
                    id = t.getLong("id"),
                    name = t.getString("name"),
                    tournamentId = t.getLong("tournamentId")
                )
            })

            val matchesArr = root.getJSONArray("matches")
            matchDao.insertMatches((0 until matchesArr.length()).map { i ->
                val m = matchesArr.getJSONObject(i)
                MatchEntity(
                    id = m.getLong("id"),
                    tournamentId = m.getLong("tournamentId"),
                    homeTeamId = m.getLong("homeTeamId"),
                    awayTeamId = m.getLong("awayTeamId"),
                    homeTeamName = m.optString("homeTeamName", ""),
                    awayTeamName = m.optString("awayTeamName", ""),
                    scheduledTime = m.getLong("scheduledTime"),
                    isCompleted = m.getBoolean("isCompleted"),
                    round = m.getInt("round")
                )
            })

            val resultsArr = root.getJSONArray("matchResults")
            matchResultDao.insertResults((0 until resultsArr.length()).map { i ->
                val r = resultsArr.getJSONObject(i)
                MatchResultEntity(
                    id = r.getLong("id"),
                    matchId = r.getLong("matchId"),
                    homeScore = r.getInt("homeScore"),
                    awayScore = r.getInt("awayScore"),
                    bestPlayer = r.optString("bestPlayer", "")
                )
            })
        }
    }
}
