package com.example.henryjacobs.shoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.henryjacobs.shoppinglist.R
import com.example.henryjacobs.shoppinglist.ScrollingActivity
import com.example.henryjacobs.shoppinglist.data.AppDatabase
import com.example.henryjacobs.shoppinglist.data.Item
import kotlinx.android.synthetic.main.item_row.view.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    // list of items needs to be mutable
    var shoppingItems = mutableListOf<Item>()

    var context: Context

    constructor(context: Context, items: List<Item>) : super() {
        this.context = context
        this.shoppingItems.addAll(items) // add the items from the list passed as an arg to constructor
    }

    constructor(context: Context) : super() {
            this.context = context
    }

    // what does one item in the recycler view look like
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.item_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    // called getItemCount() number of times
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = shoppingItems[position]
        when {
            curItem.category == 0 -> holder.icon.setImageResource(R.drawable.food)
            curItem.category == 1 -> holder.icon.setImageResource(R.drawable.electronics)
            curItem.category == 2 -> holder.icon.setImageResource(R.drawable.clothes)
            else -> holder.icon.setImageResource(R.drawable.other)
        }

        holder.name.text = curItem.name
        holder.price.text = curItem.price.toString()
        holder.purchased.isChecked = curItem.purchased
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(
                    curItem, holder.adapterPosition
            )
        }
    }

    private fun deleteItem(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(
                    shoppingItems[adapterPosition] // delete the element that the adapter is on
            )
            shoppingItems.removeAt(adapterPosition) // it is possible this line of code is executed before
            // the thread which would give an index outofbounds exception
            // so we moved it into the Thread
            (context as ScrollingActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.itemIcon
        val name = itemView.tvItemName
        val price = itemView.tvItemPrice
        val purchased = itemView.purchased
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnViewDetails
    }

    fun addItem(item: Item) {
        shoppingItems.add(0, item)
        notifyItemInserted(0)
    }

    // replaces what was in list with edited item
    fun updateItem(item: Item, idx: Int) {
        shoppingItems[idx] = item
        notifyItemChanged(idx)
    }
}
