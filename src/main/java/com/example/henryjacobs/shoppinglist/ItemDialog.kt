package com.example.henryjacobs.shoppinglist

import android.app.Dialog
import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.henryjacobs.shoppinglist.data.Item
import kotlinx.android.synthetic.main.dialog_item.view.*
import java.util.*

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)

    }

    private lateinit var itemHandler: ItemHandler

    // context is the activity that opens the dialog
    // this method is called when the activity runs the dialog
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // if context is implementing the ItemHandler interface
        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(getString(R.string.handler_exception))
        }
    }


    private lateinit var itemCategory: Spinner
    private lateinit var etItemName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etItemPrice: EditText
    private lateinit var cbPurchased: CheckBox
    private lateinit var btnAdd: Button

    // create the dialog itself
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // make sure AlertDialog is imported from support package
        val builder = android.support.v7.app.AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        // use custom layout for this dialog
        // create the xml, inflate it into a view, set the view of the Dialog builder to this view
        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_item, null)
        itemCategory = rootView.spinner
        etItemName = rootView.itemName
        etDescription = rootView.itemDescription
        etItemPrice = rootView.estimatedPrice
        cbPurchased = rootView.purchasedCheckBox
        btnAdd = rootView.addItemBtn

        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
            val curItem = arguments.getSerializable(
                    ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Item

            itemCategory.setSelection(curItem.category)
            etItemName.setText(curItem.name)
            etItemPrice.setText(curItem.price)
            etDescription.setText(curItem.description)
            cbPurchased.isSelected = curItem.purchased

            builder.setTitle(getString(R.string.edit_item))
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        val positiveButton = btnAdd
        positiveButton.setOnClickListener {
            // if edit text is not empty, check if edit mode or not
            when {
                etItemName.text.isEmpty() -> etItemName.error = getString(R.string.empty_name)
                etItemPrice.text.isEmpty() -> etItemPrice.error = getString(R.string.empty_name)
                else -> createItem()
            }
        }
    }

    private fun createItem() {
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
            handleItemEdit()
        } else {
            handleItemCreate()
        }
        dialog.dismiss()
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
                Item(
                        null,
                        itemCategory.selectedItemPosition,
                        etItemName.text.toString(),
                        etDescription.text.toString(),
                        etItemPrice.text.toString(),
                        cbPurchased.isChecked
                )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Item
        itemToEdit.category = itemCategory.selectedItemPosition
        itemToEdit.name = etItemName.text.toString()
        itemToEdit.description =  etDescription.text.toString()
        itemToEdit.price = etItemPrice.text.toString()
        itemToEdit.purchased = cbPurchased.isChecked


        itemHandler.itemUpdated(itemToEdit)
    }
}
