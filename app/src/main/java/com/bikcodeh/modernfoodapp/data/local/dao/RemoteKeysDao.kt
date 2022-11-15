package com.bikcodeh.modernfoodapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bikcodeh.modernfoodapp.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE recipeId = :recipeId")
    suspend fun remoteKeysRecipeId(recipeId: Int): RemoteKeysEntity

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}