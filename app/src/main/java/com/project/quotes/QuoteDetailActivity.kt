package com.project.quotes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_quote_detail.*

class QuoteDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote_detail)

        val position = intent.getIntExtra("Index (main->detail)", 0)
        val folders = intent.getStringArrayListExtra("folders (main->detail)")
        val Quote_Detail_Folder = intent.getStringExtra("Quote_Detail_Folder (main->detail)")
        val Quote_Detail_Sentence = intent.getStringExtra("Quote_Detail_Sentence (main->detail)")
        val Quote_Detail_Source = intent.getStringExtra("Quote_Detail_Source (main->detail)")
        val Quote_Detail_Description = intent.getStringExtra("Quote_Detail_Description (main->detail)")
        quote_detail_folder.setText(Quote_Detail_Folder)
        quote_detail_sentence.setText(Quote_Detail_Sentence)
        quote_detail_source.setText(Quote_Detail_Source)
        quote_detail_description.setText(Quote_Detail_Description)


        quote_detail_cancel.setOnClickListener {
            finish()
        }

        quote_detail_edit.setOnClickListener {
            val intent = Intent(this@QuoteDetailActivity, EditQuoteActivity::class.java)
            intent.putExtra("Index (detail->edit)", position)
            intent.putExtra("folders (detail->edit)", folders)
            intent.putExtra("Quote_Detail_Folder (detail->edit)", Quote_Detail_Folder)
            intent.putExtra("Quote_Detail_Sentence (detail->edit)", Quote_Detail_Sentence)
            intent.putExtra("Quote_Detail_Source (detail->edit)", Quote_Detail_Source)
            intent.putExtra("Quote_Detail_Description (detail->edit)", Quote_Detail_Description)
            startActivity(intent)
        }

        quote_detail_delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(
                    this@QuoteDetailActivity,
                    android.R.style.Theme_DeviceDefault_Light_Dialog
            ).setTitle("삭제").setMessage("해당 파일을 삭제하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        Toast.makeText(this@QuoteDetailActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .show()
        }

    }
}