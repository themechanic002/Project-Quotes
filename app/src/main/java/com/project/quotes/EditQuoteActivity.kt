package com.project.quotes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_quote.*
import kotlinx.android.synthetic.main.activity_edit_quote.*

class EditQuoteActivity : AppCompatActivity() {

    lateinit var Quote_Detail_Folder: String
    lateinit var Quote_Detail_Sentence: String
    lateinit var Quote_Detail_Source: String
    lateinit var Quote_Detail_Description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quote)

        Quote_Detail_Folder = intent.getStringExtra("Quote_Detail_Folder (detail->edit)").toString()
        Quote_Detail_Sentence = intent.getStringExtra("Quote_Detail_Sentence (detail->edit)").toString()
        Quote_Detail_Source = intent.getStringExtra("Quote_Detail_Source (detail->edit)").toString()
        Quote_Detail_Description = intent.getStringExtra("Quote_Detail_Description (detail->edit)").toString()

        val position = intent.getIntExtra("Index (detail->edit)", 0)


        //QuoteDetailActivity에서 넘긴 폴더 리스트 정보 받기
        val folders = ArrayList<String>()
        val count = intent.getStringArrayListExtra("folders (detail->edit)")?.size
        if(count != null){
            for (i in 0 until count)
                folders.add(intent.getStringArrayListExtra("folders (detail->edit)")?.get(i).toString())
        }


        //폴더 리스트를 펼칠 Spinner 생성
        val adapter = ArrayAdapter(
            this@EditQuoteActivity,
            android.R.layout.simple_spinner_dropdown_item,
            folders as MutableList<String>
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_folder_spinner.adapter = adapter


        //스피너 기본값(초기값)을 수정 전 폴더로 정해주기
        var selectedIndex = 0
        for(i in 0 until folders.size){
            if(folders[i] == Quote_Detail_Folder.toString())
                selectedIndex = i
        }
        edit_folder_spinner.setSelection(selectedIndex)

        //editText들을 수정 전 값들로 정해주기
        edit_quote_sentence.setText(Quote_Detail_Sentence.toString())
        edit_quote_source.setText(Quote_Detail_Source.toString())
        edit_quote_description.setText(Quote_Detail_Description.toString())


        //새 폴더 만들기
        edit_new_folder.setOnClickListener {
            val inflater = LayoutInflater.from(this@EditQuoteActivity)
            val new_folder_dialog = inflater.inflate(R.layout.activity_new_folder_dialog, null)
            val alertDialog = AlertDialog.Builder(this@EditQuoteActivity).setView(new_folder_dialog)
                    .setTitle("새 폴더 만들기").show()


            //새 폴더 만들기에서 저장 버튼 눌렀을 때
            val new_folder_name = new_folder_dialog.findViewById<EditText>(R.id.new_folder_name)
            val new_folder_save = new_folder_dialog.findViewById<Button>(R.id.new_folder_save)
            new_folder_save.setOnClickListener {

                //폴더 이름에 아무것도 입력 안했을 때
                if(new_folder_name.text.isBlank()){
                    Toast.makeText(this@EditQuoteActivity, "폴더 이름을 입력하세요", Toast.LENGTH_SHORT).show()
                }
                else{

                    //새 폴더 이름을 기존 폴더 리스트에 추가
                    folders.add(new_folder_name?.text.toString())
                    folder_spinner.setSelection(folders.lastIndex)

                    Toast.makeText(this@EditQuoteActivity, ""+folders.lastIndex, Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }

            //새 폴더 만들기에서 취소 버튼 눌렀을 때
            val new_folder_cancel = new_folder_dialog.findViewById<Button>(R.id.new_folder_cancel)
            new_folder_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }



        //수정을 다 끝내고 저장 버튼 눌렀을 때
        edit_quote_save.setOnClickListener {

            //메인이 비어있다면
            if (edit_quote_sentence.text.isBlank())
                edit_quote_sentence.setText(" ")

            //출처가 비어있다면
            if (edit_quote_source.text.isBlank())
                edit_quote_source.setText(" ")

            //설명이 비어있다면
            if (edit_quote_description.text.isBlank())
                edit_quote_description.setText(" ")


            Toast.makeText(this@EditQuoteActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()

            val result = Intent()

            result.putExtra("Index of edited quote (Edit->Main)", position)
            result.putExtra("EditedFolder", edit_folder_spinner.selectedItem.toString())
            result.putExtra("EditedSentence", quote_sentence.text.toString())
            result.putExtra("EditedSource", quote_source.text.toString())
            result.putExtra("EditedDescription", quote_description.text.toString())


            setResult(200, result)
            finish()
        }


        edit_quote_cancel.setOnClickListener {
            goBack()
        }


    }

    override fun onBackPressed() {
        goBack()
    }

    //이 페이지에서 뒤로가는 모든 상황에 호출되는 함수
    fun goBack() {
        if (edit_quote_sentence.text.toString() == Quote_Detail_Sentence)
            if (edit_quote_source.text.toString() == Quote_Detail_Source)
                if (edit_quote_description.text.toString() == Quote_Detail_Description)
                    finish()
                else {
                }
            else {
            }
        else {
            val alertDialog = AlertDialog.Builder(
                this@EditQuoteActivity,
                android.R.style.Theme_DeviceDefault_Light_Dialog
            ).setTitle("수정 중인 내용이 있습니다.").setMessage("내용을 저장하지 않고 돌아갈까요?")
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