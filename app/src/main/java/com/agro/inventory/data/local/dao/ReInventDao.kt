package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ReInventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventData(inventDataEntity: List<ReinventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReInvent(reInventActivity: ReinventEntity)

    @Query("SELECT * FROM reinvent_entity")
    fun getReInventAll(): Flow<List<ReinventEntity>>

    @Query("SELECT * FROM reinvent_entity WHERE kodePlot =:kodePlot")
    fun getInventData(kodePlot: String): Flow<List<ReinventEntity>>

    @Query("SELECT * FROM reinvent_entity WHERE idComodity =:idComodity AND kodePlot =:kodePlot")
     fun getReInvent(idComodity: String, kodePlot: String): Flow<List<ReinventEntity>>

    @Query("UPDATE reinvent_entity SET jmlTanam =:jmlTanam,jmlHidup =:jmlHidup, jmlSakit =:jmlSakit, jmlMati =:jmlMati,penyulaman =:penyulaman, keliling =:keliling, tinggi =:tinggi, photo =:photo, lat =:lat, lng =:lng, idComodity =:idComodity WHERE kodePlot =:kodePlot")
    suspend fun updateReInvent(
        jmlTanam: String?,
        jmlHidup: String?,
        jmlSakit: String?,
        jmlMati: String?,
        penyulaman: String?,
        keliling: String?,
        tinggi: String?,
        photo: String?,
        lat: String?,
        lng: String?,
        idComodity: Int?,
        kodePlot: String?
    )

    @Query("DELETE FROM reinvent_entity")
    suspend fun deleteReInvent()



}