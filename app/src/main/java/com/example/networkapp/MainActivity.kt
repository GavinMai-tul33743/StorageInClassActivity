package com.example.networkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView

    private val internalFilename = "my_file"
    private lateinit var file : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        file = File(filesDir, internalFilename)

        loadData()

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

    }

    private fun saveData(_data : String){
        try {
        val outputStream = FileOutputStream(file)
        outputStream.write(_data.toByteArray())
            outputStream.close()
         } catch (e: Exception) {
        e.printStackTrace()
      }
    }

    private fun loadData(){
        try {
            val br = BufferedReader(FileReader(file))
            var line: String?
            if (br.readLine().also { line = it } != null) {
                Log.d("TEST", line.toString())
                titleTextView.text = line
            }
            if (br.readLine().also { line = it } != null) {
                Log.d("TEST", line.toString())
                descriptionTextView.text = line
            }
            if (br.readLine().also { line = it } != null) {
                Log.d("TEST", line.toString())
                Picasso.get().load(line).into(comicImageView)
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        saveData(comicObject.getString("title") + "\n" + comicObject.getString("alt") + "\n" + comicObject.getString("img"))
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
    }


}