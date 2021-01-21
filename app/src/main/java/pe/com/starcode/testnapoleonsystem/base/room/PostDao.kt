package pe.com.starcode.testnapoleonsystem.base.room

import androidx.room.*

@Dao
interface PostDao {

    @Insert()
    fun insert(item: Post)

    @Query("SELECT *  FROM post ")
    fun listAll(): List<Post>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(item: Post)

}