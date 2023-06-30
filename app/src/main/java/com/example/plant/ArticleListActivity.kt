package com.example.plant

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.plant.models.Article
import com.example.plant.models.Plant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Objects

class ArticleListActivity : AppCompatActivity() {

    var database = Firebase.database
    var myRef = database.getReference("articles")
    private lateinit var progress : ProgressDialog
    lateinit var gridView: GridView
    private lateinit var courseAdapter : ArticleGridViewAdapter
    lateinit var articles : List<Article>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        val actionbar = supportActionBar
        actionbar!!.title = "Article List"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        progress = ProgressDialog(this)
        progress.setTitle("Đợi chút...")
        progress.setMessage("Đang tải dữ liệu...")
        progress.setCanceledOnTouchOutside(false)
        gridView = findViewById(R.id.gridView)



        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.show()
                Log.i("SNAPSHOT", snapshot.value.toString())
                articles = snapshot.children.map {
                        dataSnapshot ->
                    val plant = dataSnapshot.getValue(Article::class.java)!!.copy(id =  dataSnapshot.key)
                    plant
                }
                courseAdapter = ArticleGridViewAdapter(articleList = articles, this@ArticleListActivity)
                gridView.adapter = courseAdapter

                Log.i("Array", articles.toString())
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progress.dismiss()
            }
        })

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ArticleDetailActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("image", articles[position].image)
            intent.putExtra("title", articles[position].title)
            intent.putExtra("owner", articles[position].owner)
            intent.putExtra("content", articles[position].content)
            intent.putExtra("date", articles[position].date)
            intent.putExtra("id", articles[position].id)

            startActivity(intent)
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}