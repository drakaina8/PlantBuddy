package com.example.plantbuddy

import android.content.Context
import androidx.room.*

class InternalStorageHandler {

    @androidx.room.Database(entities = [PlantProfile::class]
                                        ,version = 1, exportSchema = true)
    abstract class InternalDatabase: RoomDatabase(){

        abstract fun getProfileDao(): EntityDataAccessObjects.ProfileDao

        companion object{
            @Volatile
            private var DATABASE: InternalDatabase? = null

            fun getDataBaseInstance(context: Context): InternalDatabase? {

                var instance = DATABASE
                if (instance == null) {

                    synchronized(this) {

                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            InternalDatabase::class.java,
                            "internal_database"
                        ).build()

                        instance = DATABASE
                        return instance
                    }
                }

                return instance
            }
        }

    }

    class InternalDatabaseRepository{

    }

    companion object Entities {

        @Entity(tableName = "plant_profiles")
        data class PlantProfile (

            @PrimaryKey(autoGenerate = true) var id: Int,

            @ColumnInfo(name = "plant_name") var plantName : String,

            @ColumnInfo(name = "profile_image_path") var profileImagePath: String?

            )


    }


    class EntityDataAccessObjects{
        @Dao
        interface ProfileDao {

            @Query("SELECT * FROM plant_profiles")
            fun getAll(): List<PlantProfile>
        }


    }
}