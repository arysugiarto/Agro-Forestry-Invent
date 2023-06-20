package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.InventEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface InventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvent(inventActivity: InventEntity)

    @Query("SELECT * FROM invent_entity")
    fun getInventAll(): Flow<List<InventEntity>>

    @Query("SELECT * FROM invent_entity WHERE idComodity =:idComodity AND kodePlot =:kodePlot")
     fun getInvent(idComodity: String, kodePlot: String): Flow<List<InventEntity>>

    @Query("SELECT kodePlot FROM invent_entity")
    fun getKodePlot(): Int

    @Query("UPDATE invent_entity SET jmlTanam =:jmlTanam, keliling =:keliling, tinggi =:tinggi, idComodity =:idComodity, photo =:photo, lat =:lat, lng =:lng WHERE id =:id")
    suspend fun updateInvent(
        jmlTanam: String?,
        keliling: String?,
        tinggi: String?,
        idComodity: Int?,
        photo: String?,
        lat: String?,
        lng: String?,
        id: String?
    )

    @Query("DELETE FROM invent_entity")
    suspend fun deleteInvent()



}