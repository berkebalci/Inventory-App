package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item:: class], version = 1, exportSchema = false)
abstract class InventoryDataBase: RoomDatabase() {
    abstract fun itemDao() : ItemDAO

    companion object{
        @Volatile //Boyle yaparak veriler cache'lenmiyor direkt ram'e aktariliyor, bu sayede tum core'lar verileri updated halleriyle gorebiliyor.
        private var INSTANCE: InventoryDataBase? = null

        fun getDatabase(context: Context): InventoryDataBase{
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context = context, klass = InventoryDataBase:: class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
            //synchronized olmasi exection zamaninda sadece 1 adet veritabani objesi olmasini sagliyor cunku sadece 1 adet olmali.
        }

    }


}