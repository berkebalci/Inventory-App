import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.example.inventory.data.InventoryDataBase
import com.example.inventory.data.Item
import org.junit.runner.RunWith
import com.example.inventory.data.ItemDAO
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDAO
    private lateinit var inventoryDatabase: InventoryDataBase

    @Before
    fun createDb(){
        val context: Context = ApplicationProvider.getApplicationContext()
        inventoryDatabase = Room.inMemoryDatabaseBuilder(
            context,
            InventoryDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        itemDao = inventoryDatabase.itemDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }



    private var item1 = Item(1, "Apples", 10.0, 20)
    private var item2 = Item(2, "Bananas", 15.0, 97)

    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    private suspend fun addTwoItemsToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
    }
    //Add a test for the DAO function to update an entity.


    @Test
    @Throws(Exception::class)
    fun daoUpdateItem_updatedItemInDb() = runBlocking{
    addTwoItemsToDb()
        val item1= itemDao.update(Item(1, "Apples", 15.0, 25))
        val item2 = itemDao.update(Item(2, "Bananas", 5.0, 50))
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], Item(1, "Apples", 15.0, 25))
        assertEquals(allItems[1], Item(2, "Bananas", 5.0, 50))

    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItem_deletedItemInDb() = runBlocking{
        addTwoItemsToDb()
        itemDao.delete(item1)
        itemDao.delete(item2)
        val allItems = itemDao.getAllItems().first()
        assertTrue(allItems.isEmpty())

    }



}