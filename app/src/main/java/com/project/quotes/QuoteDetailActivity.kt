package com.project.quotes

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_quote_detail.*

class QuoteDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote_detail)

        val realmManager = RealmManager(this@QuoteDetailActivity)

        val position = intent.getIntExtra("Index (main->detail)", 0)
        /*val folders = intent.getStringArrayListExtra("folders (main->detail)")
        var Quote_Detail_Folder = intent.getStringExtra("Quote_Detail_Folder (main->detail)")
        var Quote_Detail_Sentence = intent.getStringExtra("Quote_Detail_Sentence (main->detail)")
        var Quote_Detail_Source = intent.getStringExtra("Quote_Detail_Source (main->detail)")
        var Quote_Detail_Description = intent.getStringExtra("Quote_Detail_Description (main->detail)")*/

        val folders = realmManager.findFolders()
        var Quote_Detail_Folder = realmManager.getFolder(position)
        var Quote_Detail_Sentence = realmManager.getSentence(position)
        var Quote_Detail_Source = realmManager.getSource(position)
        var Quote_Detail_Description = realmManager.getDescription(position)

        quote_detail_folder.setText(Quote_Detail_Folder)
        quote_detail_sentence.setText(Quote_Detail_Sentence)
        if (Quote_Detail_Source != " ")
            quote_detail_source.setText("- " + Quote_Detail_Source + " ")
        else
            quote_detail_source.setText(" ")
        quote_detail_description.setText(Quote_Detail_Description)


        val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == 200) {

                        //이 Activity 새로고침
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)

                    }
                }


        /*//intent로 보냈던 데이터들 다시 받기
        val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == 200) {
                        val intentData = result.data

                        Quote_Detail_Folder = intentData?.getStringExtra("EditedFolder").toString()
                        Quote_Detail_Sentence = intentData?.getStringExtra("EditedSentence").toString()
                        Quote_Detail_Source = intentData?.getStringExtra("EditedSource").toString()
                        Quote_Detail_Description = intentData?.getStringExtra("EditedDescription").toString()
                        quote_detail_folder.setText(Quote_Detail_Folder)
                        quote_detail_sentence.setText(Quote_Detail_Sentence)
                        quote_detail_source.setText(Quote_Detail_Source)
                        quote_detail_description.setText(Quote_Detail_Description)

                    }
                }*/


        //뒤로가기 버튼 눌렀을 때
        quote_detail_cancel.setOnClickListener {
            finish()
        }

        //편집하기 버튼 눌렀을 때
        quote_detail_edit.setOnClickListener {
            val intent = Intent(this@QuoteDetailActivity, EditQuoteActivity::class.java)
            intent.putExtra("Index (detail->edit)", position)
            intent.putExtra("folders (detail->edit)", folders)
            intent.putExtra("Quote_Detail_Folder (detail->edit)", Quote_Detail_Folder)
            intent.putExtra("Quote_Detail_Sentence (detail->edit)", Quote_Detail_Sentence)
            intent.putExtra("Quote_Detail_Source (detail->edit)", Quote_Detail_Source)
            intent.putExtra("Quote_Detail_Description (detail->edit)", Quote_Detail_Description)
            resultLauncher.launch(intent)
        }


    }
}