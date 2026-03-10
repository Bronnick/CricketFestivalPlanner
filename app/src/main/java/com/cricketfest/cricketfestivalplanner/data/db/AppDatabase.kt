package com.cricketfest.cricketfestivalplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.MatchResultDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.TeamDao
import com.cricketfest.cricketfestivalplanner.data.db.dao.TournamentDao
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.MatchResultEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.TeamEntity
import com.cricketfest.cricketfestivalplanner.data.db.entity.TournamentEntity

@Database(
    entities = [
        TournamentEntity::class,
        TeamEntity::class,
        MatchEntity::class,
        MatchResultEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tournamentDao(): TournamentDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun matchResultDao(): MatchResultDao
}
