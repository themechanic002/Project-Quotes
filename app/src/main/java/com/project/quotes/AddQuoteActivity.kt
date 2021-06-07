package com.project.quotes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_add_quote.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_folder_dialog.*

class AddQuoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quote)

        val folder_spinner = findViewById<Spinner>(R.id.folder_spinner)

        //MainActivity에서 넘긴 폴더 리스트 정보 받기
        val folders = ArrayList<String>()
        val count = intent.getStringArrayListExtra("folders")?.size
        if (count != null) {
            for (i in 0 until count)
                folders.add(intent.getStringArrayListExtra("folders")?.get(i).toString())
        }

        //처음에 아무런 item도 없을 때 폴더가 하나라도 없으면 spinner가 작동을 안하기 때문에 기본 폴더 먼저 만들기
        if(folders.size == 0){
            folders.add("new folder")
        }


        //폴더 리스트를 펼칠 Spinner 생성
        val adapter = ArrayAdapter(
            this@AddQuoteActivity,
            android.R.layout.simple_spinner_dropdown_item,
            folders as MutableList<String>
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        folder_spinner.adapter = adapter

        folder_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                folder_spinner.setSelection(position, true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        //새 폴더 만들기
        new_folder.setOnClickListener {

            val inflater = LayoutInflater.from(this@AddQuoteActivity)
            val new_folder_dialog = inflater.inflate(R.layout.activity_new_folder_dialog, null)
            val alertDialog = AlertDialog.Builder(this@AddQuoteActivity).setView(new_folder_dialog)
                .setTitle("새 폴더 만들기").show()


            //새 폴더 만들기에서 저장 버튼 눌렀을 때
            val new_folder_name = new_folder_dialog.findViewById<EditText>(R.id.new_folder_name)
            val new_folder_save = new_folder_dialog.findViewById<Button>(R.id.new_folder_save)
            new_folder_save.setOnClickListener {

                //폴더 이름에 아무것도 입력 안했을 때
                if(new_folder_name.text.isBlank()){
                    Toast.makeText(this@AddQuoteActivity, "폴더 이름을 입력하세요", Toast.LENGTH_SHORT).show()
                }
                else{

                    //새 폴더 이름을 기존 폴더 리스트에 추가
                    folders.add(new_folder_name?.text.toString())
                    folder_spinner.setSelection(folders.lastIndex)

                    Toast.makeText(this@AddQuoteActivity, ""+folders.lastIndex, Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }

            //새 폴더 만들기에서 취소 버튼 눌렀을 때
            val new_folder_cancel = new_folder_dialog.findViewById<Button>(R.id.new_folder_cancel)
            new_folder_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }


        //뒤로가기 버튼 눌렀을 때
        quote_cancel.setOnClickListener {
            goBack()
        }


        //저장 버튼 눌렀을 때
        quote_save.setOnClickListener {

            //폴더를 선택하지 않은 경우
            if (folder_spinner.selectedItem == null) {
                val alertDialog = AlertDialog.Builder(
                    this@AddQuoteActivity,
                    android.R.style.Theme_DeviceDefault_Light_Dialog
                ).setTitle("선택된 폴더가 없습니다.").setMessage("새 폴더를 추가해주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .show()
            }

            //폴더를 선택한 경우
            else {
                //메인이 비어있다면
                if (quote_sentence.text.isBlank())
                    quote_sentence.setText(" ")

                //설명이 비어있다면
                if (quote_description.text.isBlank())
                    quote_description.setText(" ")

                //출처가 비어있다면
                if (quote_source.text.isBlank())
                    quote_source.setText(" ")


                Toast.makeText(this@AddQuoteActivity, "저장되었습니다.", Toast.LENGTH_SHORT).show()

                val result = Intent()

                result.putExtra("SavedFolder", folder_spinner.selectedItem.toString())
                result.putExtra("SavedSentence", quote_sentence.text.toString())
                result.putExtra("SavedSource", quote_source.text.toString())
                result.putExtra("SavedDescription", quote_description.text.toString())


                setResult(RESULT_OK, result)
                finish()

            }
        }


    }

    override fun onBackPressed() {
        goBack()
    }


    //이 페이지에서 뒤로가는 모든 상황에 호출되는 함수
    fun goBack() {
        if (quote_sentence.text.isBlank())
            if (quote_source.text.isBlank())
                if (quote_description.text.isBlank())
                    finish()
                else {
                }
            else {
            }
        else {
            val alertDialog = AlertDialog.Builder(
                this@AddQuoteActivity,
                android.R.style.Theme_DeviceDefault_Light_Dialog
            ).setTitle("작성 중인 내용이 있습니다.").setMessage("내용을 저장하지 않고 돌아갈까요?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    finish()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .show()
        }
    }
}