package yayang.setiyawan.toko.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import yayang.setiyawan.toko.model.Alamat
import yayang.setiyawan.toko.model.Produk

@Database(entities = [Produk::class,Alamat::class] /* List model Ex:NoteModel */, version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun daoKeranjang(): DaoKeranjang
    abstract fun daoAlamat(): DaoAlamat

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MyDatabase::class.java, "MyDatabase12" // Database Name
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}