package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    @Query("SELECT * FROM election_table WHERE old = 0")
    fun getCurrentElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE saved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun getElectionById(electionId: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :electionId")
    fun deleteElection(electionId: Int)

    @Query("DELETE FROM election_table")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Update
    fun update(election: Election)

}