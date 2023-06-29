package com.example.plant.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.GridView
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.plant.ArticleDetailActivity
import com.example.plant.ArticleGridViewAdapter
import com.example.plant.GridViewAdapter
import com.example.plant.PlantDetailActivity
import com.example.plant.PlantGridViewAdapter
import com.example.plant.R
import com.example.plant.models.Article
import com.example.plant.models.ArticleLike
import com.example.plant.models.Plant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    lateinit var email: TextView
    lateinit var grvPlant : GridView
    lateinit var grvArticle : GridView
    var database = Firebase.database
    lateinit var scrArticle : ScrollView
    private lateinit var articleAdapter : ArticleGridViewAdapter
    lateinit var articles : List<Article>
    private lateinit var plantAdapter : PlantGridViewAdapter
    lateinit var plants : List<Plant>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        email = view.findViewById(R.id.txtEmail)
        grvPlant = view.findViewById(R.id.grvPlant)
        grvArticle = view.findViewById(R.id.grvArticle)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email.text = Firebase.auth.currentUser!!.email.toString()
        init(view)

        grvPlant.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent1 = Intent(view.context, PlantDetailActivity::class.java)
            intent1.putExtra("disable", "disable")
            intent1.putExtra("position", position)
            intent1.putExtra("image", plants[position].image)
            intent1.putExtra("name", plants[position].name)
            intent1.putExtra("owner", plants[position].owner)
            intent1.putExtra("type", plants[position].type)
            intent1.putExtra("date", plants[position].date)
            intent1.putExtra("description", plants[position].description)

            startActivity(intent1)
        }
        grvArticle.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(view.context, ArticleDetailActivity::class.java)
            intent.putExtra("disable", "disable")
            intent.putExtra("position", position)
            intent.putExtra("image", articles[position].image)
            intent.putExtra("title", articles[position].title)
            intent.putExtra("owner", articles[position].owner)
            intent.putExtra("content", articles[position].content)
            intent.putExtra("date", articles[position].date)

            startActivity(intent)
        }

    }

    private fun init(view : View){
        database.getReference("articles").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())
                articles = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(Article::class.java)!!
                }


                Log.i("Array", articles.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        var article_likes : List<Article> = emptyList()
        database.getReference("like_articles").child(Firebase.auth.currentUser!!.email.toString().replace("@gmail.com", "")).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val like : List<ArticleLike> = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(ArticleLike::class.java)!!
                }

                for (item in like){
                    article_likes = article_likes + articles[item.position!!]

                }
                Log.i("Article like", article_likes.toString())
                articleAdapter = ArticleGridViewAdapter(articleList = article_likes, view.context)
                grvArticle.adapter = articleAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        database.getReference("plants").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())
                plants = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(Plant::class.java)!!
                }


                Log.i("Array", articles.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        var plants_like : List<Plant> = emptyList()
        database.getReference("like_plants").child(Firebase.auth.currentUser!!.email.toString().replace("@gmail.com", "")).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val like : List<ArticleLike> = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(ArticleLike::class.java)!!
                }

                for (item in like){
                    plants_like = plants_like + plants[item.position!!]

                }
                Log.i("Plant like", plants_like.toString())
                plantAdapter = PlantGridViewAdapter(courseList = plants_like, view.context)
                grvPlant.adapter = plantAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }
}