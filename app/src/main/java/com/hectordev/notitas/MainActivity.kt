package com.hectordev.notitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.preference.ListPreferenceDialogFragmentCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val key = "NOTES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var itemList = arrayListOf<String>()
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, itemList)

        var prefs = PreferenceManager.getDefaultSharedPreferences(this  )

        // loadValues()
        val values = prefs.getString(key, "")
        if (!values.isNullOrBlank()) {
            // convert string to array
            val activities = values.split(",").toMutableList() // .toTypedArray()
            println(activities)
            for (element in activities) {
                itemList.add(element.trimStart())
                listView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }


        // Adicionamos el item cuando el boton adicionar es presionado
        add.setOnClickListener {
            itemList.add(editText.text.toString())
            listView.adapter = adapter
            adapter.notifyDataSetChanged()
            // limpiamos elitem añadido
            editText.text.clear()

            val editor = prefs.edit()
            editor.remove(key)
            editor.putString(key, itemList.joinToString())
            editor.apply()
            val values = prefs.getString(key, "")

        }

        // Item seleccionado
        listView.setOnItemClickListener { adapterView, view, i, l ->
            android.widget.Toast.makeText(this, "Seleccionó el item: " + itemList.get(i), android.widget.Toast.LENGTH_SHORT).show()
        }

        // Eliminar items seleccionados
        delete.setOnClickListener{
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item)) {
                    adapter.remove(itemList.get(item))
                }
                item --
            }
            position.clear()
            adapter.notifyDataSetChanged()

            val editor = prefs.edit()
            editor.remove(key)
            editor.putString(key, itemList.joinToString())
            editor.apply()
            val values = prefs.getString(key, "")

        }

        // Limpiar todos los items añadidos
        clear.setOnClickListener {
            itemList.clear()
            adapter.notifyDataSetChanged()

            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }

    }
}