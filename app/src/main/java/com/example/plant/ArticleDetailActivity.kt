package com.example.plant

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import com.example.plant.models.ArticleLike
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ArticleDetailActivity : AppCompatActivity() {
    lateinit var img : ImageView
    lateinit var title : TextView
    lateinit var owner : TextView
    lateinit var date : TextView
    lateinit var content : TextView
    lateinit var iconHeart : ImageView
    var color : Int = 0
    var database = Firebase.database
    var myRef = database.getReference("like_articles")
    lateinit var likes : List<ArticleLike>
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("like_articles")
    var position : Int = 0

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        img = findViewById(R.id.img)
        title = findViewById(R.id.txtTitle)
        owner = findViewById(R.id.txtOwner)
        content = findViewById(R.id.txtContent)
        date = findViewById(R.id.txtDate)
        iconHeart = findViewById(R.id.iconHeart)
        position = intent.getIntExtra("position", -1)

        if (intent.hasExtra("disable")){
            iconHeart.visibility = View.GONE
        }else{
            iconHeart.visibility = View.VISIBLE
        }


        myRef.child(auth.currentUser!!.email.toString().replace("@gmail.com", "")).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val like : List<ArticleLike> = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(ArticleLike::class.java)!!
                }
                likes = like
                if (!likes.any{ it.position == position}) {
                    Log.i("Like", true.toString())
                    color = R.color.pink
                    iconHeart.setColorFilter(ContextCompat.getColor(applicationContext, R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN)
                }else{
                    Log.i("Like", false.toString())
                    color = R.color.custom_color_secondary
                    iconHeart.setColorFilter(ContextCompat.getColor(applicationContext, R.color.custom_color_secondary), android.graphics.PorterDuff.Mode.SRC_IN)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        val actionbar = supportActionBar

        actionbar!!.title = "Bài viết"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        Log.i("Position", position.toString())
        title.text = intent.getStringExtra("title")
        owner.text = intent.getStringExtra("owner")
        content.text = intent.getStringExtra("content")
        date.text = intent.getStringExtra("date")

        Picasso.get().load(intent.getStringExtra("image")).into(img)

        iconHeart.setOnClickListener{
            if (color == R.color.pink){
                color = R.color.custom_color_secondary
                Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show()
                like()
            }else{
                color = R.color.pink
                Toast.makeText(this, "Disliked", Toast.LENGTH_SHORT).show()
                dislike()
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun like(){
        iconHeart.setColorFilter(ContextCompat.getColor(applicationContext, R.color.custom_color_secondary), android.graphics.PorterDuff.Mode.SRC_IN)
        val newData = firebaseRef!!.child(auth.currentUser!!.email.toString().replace("@gmail.com", "")).push()
        newData.setValue(hashMapOf("position" to position))
    }

    private fun dislike(){
        iconHeart.setColorFilter(ContextCompat.getColor(applicationContext, R.color.pink), android.graphics.PorterDuff.Mode.SRC_IN)
        val applesQuery: Query = myRef.child(auth.currentUser!!.email.toString().replace("@gmail.com", "")).orderByChild("position").equalTo(position.toDouble())

        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Remove", "onCancelled", databaseError.toException())
            }
        })

    }


}