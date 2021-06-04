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

        //MainActivity에서 넘긴 폴더 리스트 정보 받기
        val folders= intent.getStringArrayListExtra("folders")

        //폴더 리스트를 펼칠 Spinner 생성
        val adapter = ArrayAdapter(this@AddQuoteActivity, android.R.layout.simple_spinner_dropdown_item, folders as MutableList<String>)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        folder_spinner.adapter = adapter

    }
}