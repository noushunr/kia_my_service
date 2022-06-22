package com.serviceapp.kia.ui.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.serviceapp.kia.R


/*
 *Created by Adithya T Raj on 04-07-2021
*/

class SearchableDialog(
    var context: Context,
    var items: List<SearchableItem>,
    var listener: SearchableItemListener,
    var cancelButtonText: String = "Cancel"
) {
    private val TAG = "SearchableDialog"
    private lateinit var alertDialog: AlertDialog
    var position: Int = 0
    var selected: SearchableItem? = null


    lateinit var searchListAdapter: SearchableListAdapter
    lateinit var listView: ListView
    /***
     *
     * show the seachable dialog
     */
    fun show() {

        val c: Context
        c = ContextThemeWrapper(context, R.style.LightTheme)
        context.setTheme(R.style.LightTheme)

        val adb = AlertDialog.Builder(c)
        val view = LayoutInflater.from(context).inflate(R.layout.searchable_dialog, null)
        val rippleViewClose = view.findViewById(R.id.close) as TextView

        listView = view.findViewById(R.id.list) as ListView

        val searchBox = view.findViewById(R.id.searchBox) as EditText
        searchListAdapter = SearchableListAdapter(context, items)
        listView.adapter = searchListAdapter
        adb.setView(view)
        alertDialog = adb.create()
        alertDialog.setCancelable(true)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, v, _, _ ->
            val t = v.findViewById<TextView>(R.id.t1)
            for (j in items.indices) {
                if (t.text.toString().equals(items[j].title, ignoreCase = true)) {
                    position = j
                    selected = items[position]
                }
            }
            try {
                listener.onSearchableItemClicked(selected!!, position)
            } catch (e: Exception) {
                //Log.e(TAG, e.message ?: e.toString())
            }

            alertDialog.dismiss()
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val filteredValues = arrayListOf<SearchableItem>()
                for (i in items.indices) {
                    val item = items[i]
                    if (item.title.toLowerCase().trim { it <= ' ' }.contains(searchBox.text.toString().toLowerCase().trim { it <= ' ' })) {
                        filteredValues.add(item)
                    }
                }
                searchListAdapter = SearchableListAdapter(context, filteredValues)
                listView.adapter = searchListAdapter
            }
        })

        if(cancelButtonText.isNotBlank()){
            rippleViewClose.text = cancelButtonText

            rippleViewClose.setOnClickListener { alertDialog.dismiss() }
        }else{
            rippleViewClose.visibility = View.GONE
        }
        alertDialog.setOnDismissListener {
            listener.onDismissDialog()
        }
        alertDialog.setOnCancelListener {
            listener.onDismissDialog()
        }
        alertDialog.show()
    }
}