package pe.com.starcode.testnapoleonsystem.base.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class DataBaseApp : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: DataBaseApp? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            DataBaseApp::class.java, "db_napoleon_post.db"
        ).fallbackToDestructiveMigration().build()
    }
}