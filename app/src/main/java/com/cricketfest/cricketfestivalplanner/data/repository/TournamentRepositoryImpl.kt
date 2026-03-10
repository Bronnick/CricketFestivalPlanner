package com.cricketfest.cricketfestivalplanner.data.repository

import com.cricketfest.cricketfestivalplanner.data.db.dao.TeamDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.TournamentDao
import com.cricketfest.cricketfestivalplanner.data.db.entity.TeamEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.TournamentEntity
import com.cricketfest.cricketfestivalplanner.data.model.Team
import com.cricketfest.cricketfestivalplanner.data.model.Tournament
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TournamentRepositoryImpl(
    private val tournamentDao: TournamentDao,
    private val teamDao: TeamDao
) : TournamentRepository {

    override fun getAllTournaments(): Flow<List<Tournament>> =
        tournamentDao.getAllTournaments().map { list -> list.map { it.toDomain() } }

    override fun getActiveTournaments(): Flow<List<Tournament>> =
        tournamentDao.getActiveTournaments().map { list -> list.map { it.toDomain() } }

    override fun getCompletedTournaments(): Flow<List<Tournament>> =
        tournamentDao.getCompletedTournaments().map { list -> list.map { it.toDomain() } }

    override suspend fun getTournamentById(id: Long): Tournament? =
        tournamentDao.getTournamentById(id)?.toDomain()

    override suspend fun insertTournament(tournament: Tournament): Long =
        tournamentDao.insertTournament(tournament.toEntity())

    override suspend fun updateTournament(tournament: Tournament) =
        tournamentDao.updateTournament(tournament.toEntity())

    override suspend fun deleteTournament(id: Long) =
        tournamentDao.deleteTournament(id)

    override fun getTeamsByTournament(tournamentId: Long): Flow<List<Team>> =
        teamDao.getTeamsByTournament(tournamentId).map { list -> list.map { it.toDomain() } }

    override suspend fun getTeamsByTournamentOnce(tournamentId: Long): List<Team> =
        teamDao.getTeamsByTournamentOnce(tournamentId).map { it.toDomain() }

    override suspend fun insertTeams(teams: List<Team>) =
        teamDao.insertTeams(teams.map { it.toEntity() })

    override suspend fun deleteTeamsByTournament(tournamentId: Long) =
        teamDao.deleteTeamsByTournament(tournamentId)

    private fun TournamentEntity.toDomain() = Tournament(
        id = id, name = name, matchType = matchType, teamCount = teamCount,
        pointSystem = pointSystem, structure = structure, duration = duration,
        location = location, posterImagePath = posterImagePath,
        isCompleted = isCompleted, createdAt = createdAt
    )

    private fun Tournament.toEntity() = TournamentEntity(
        id = id, name = name, matchType = matchType, teamCount = teamCount,
        pointSystem = pointSystem, structure = structure, duration = duration,
        location = location, posterImagePath = posterImagePath,
        isCompleted = isCompleted, createdAt = createdAt
    )

    private fun TeamEntity.toDomain() = Team(id = id, name = name, tournamentId = tournamentId)
    private fun Team.toEntity() = TeamEntity(id = id, name = name, tournamentId = tournamentId)
}
