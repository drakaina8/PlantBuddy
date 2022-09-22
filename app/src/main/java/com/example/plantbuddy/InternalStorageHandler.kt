package com.example.plantbuddy

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

class InternalStorageHandler {

    //defines tables to be included in the database, its version number and where or not to export the schema
    @androidx.room.Database(entities = [PlantProfile::class]
                                        ,version = 1, exportSchema = false)
    abstract class InternalDatabase: RoomDatabase(){

        //contains methods fo accessing the plant profile database
        abstract fun profileDao(): EntityDataAccessObjects.ProfileDao

        companion object{
            @Volatile
            private var DATABASE: InternalDatabase? = null
            fun getDataBaseInstance(context: Context): InternalDatabase {


                var instance = DATABASE
                if (instance == null) {
                    println("is null")



                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            InternalDatabase::class.java,
                            "internal_database"
                        ).build()
                        println("Built")

                        DATABASE = instance
                        return instance


                }


                return instance
            }


        }

    }

    class InternalDatabaseRepository{

    }
    //collection of Entities used in the program, each represents a table in the database
    companion object Entities {

        //collection of user created profiles for individual plants
        @Entity(tableName = "plant_profiles")
        data class PlantProfile (

            @PrimaryKey var id: Int,

            @ColumnInfo(name = "plant_name") var plantName : String,

            @ColumnInfo(name = "profile_image_path") var profileImagePath: String?,


            )


    }


    class EntityDataAccessObjects{
        //interface for accessing the plant profile table
        @Dao interface ProfileDao {
            //returns all entries as a List
            @Query("SELECT * FROM plant_profiles")
            fun getAll(): List<PlantProfile>

            //returns all entries as a LiveData List
            @Query("SELECT * FROM plant_profiles")
            fun getAllLiveData(): LiveData<List<PlantProfile>>

            @Insert
            fun insertProfile(profile:PlantProfile)

            //selects all plants with matching name
            @Query("SELECT * FROM plant_profiles WHERE plant_name = :name ")
            fun selectByName(name: String)

        }


    }
}


