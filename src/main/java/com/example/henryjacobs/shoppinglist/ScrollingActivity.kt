package com.example.henryjacobs.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.henryjacobs.shoppinglist.adapter.ItemAdapter
import com.example.henryjacobs.shoppinglist.data.AppDatabase
import com.example.henryjacobs.shoppinglist.data.Item
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {
    companion object {
        const val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    private lateinit var itemAdapter: ItemAdapter

    private var editIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        toolbar.title = getString(R.string.shopping_list)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addItem -> {
                showAddItemDialog()
            }
            R.id.deleteList ->{
                deleteList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        Thread {

            // get all todos from DB, returned as a list
            val items = AppDatabase.getInstance(this@ScrollingActivity).itemDao().findAllItems()

            // create the todoAdapter
            runOnUiThread {

                // need to have changed the constructor of the TodoAdapter to take the list of todos
                // as a parameter see next step (9)
                itemAdapter = ItemAdapter(this@ScrollingActivity, items)

                // all this needs to be inside this thread because they need to be initialized before
                // recycler view is is intialized (??)
                recyclerItem.adapter = itemAdapter
                //val callback = TodoTouchHelperCallback(todoAdapter)
                //val touchHelper = ItemTouchHelper(callback)
                //touchHelper.attachToRecyclerView(recyclerTodo)
            }
        }.start()
    }

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, getString(R.string.tag_create))
    }


    fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx
        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
                getString(R.string.edit_item_dialog))
    }

    fun deleteList(){
        Thread {
            var context = this@ScrollingActivity
            AppDatabase.getInstance(context).itemDao().deleteAll()
            itemAdapter.shoppingItems.clear() // it is possible this line of code is executed before
            // the thread which would give an index outofbounds exception
            // so we moved it into the Thread
            (context as ScrollingActivity).runOnUiThread {
                itemAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    // have to create a new thread to add the element to the database
    // use the DAO to do this
    // need this sort of code every time you want to access the DB
    override fun itemCreated(item: Item) {
        Thread {
            val id = AppDatabase.getInstance(this).itemDao().insertItem(item)
            item.itemId = id
            runOnUiThread {
                itemAdapter.addItem(item)
            }
        }.start()

    }

    override fun itemUpdated(item: Item) {
        val dbThread = Thread {
            AppDatabase.getInstance(this@ScrollingActivity).itemDao().updateItem(item)

            runOnUiThread { itemAdapter.updateItem(item, editIndex) }
        }
        dbThread.start()
    }
}

