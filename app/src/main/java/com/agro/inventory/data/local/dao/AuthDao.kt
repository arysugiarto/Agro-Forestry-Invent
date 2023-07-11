package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.AuthEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(authEntity: AuthEntity)

    @Query("SELECT * FROM auth_entity")
    fun getAuth(): Flow<List<AuthEntity>>

    @Query("DELETE FROM auth_entity")
    suspend fun deleteAuth()
}

