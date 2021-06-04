package com.project.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_add_quote.*

class AddQuoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quote)

        val folders= intent.getStringArrayListExtra("folders")

        val adapter = ArrayAdapter(this@AddQuoteActivity, android.R.layout.simple_spinner_dropdown_item, folders as MutableList<String>)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        folder_spinner.adapter = adapter

    }
}