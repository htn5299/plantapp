package com.example.plant

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.plant.models.Article
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate


class AddNewArticleActivity : AppCompatActivity() {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var uri : Uri
    lateinit var title : TextInputEditText
    lateinit var content : TextInputEditText
    lateinit var btnAdd : MaterialButton
    lateinit var btnAddImage : MaterialButton
    lateinit var imgArticle : ImageView
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("articles")
    private val storageRef = FirebaseStorage.getInstance().getReference("images")
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_article)
        title = findViewById(R.id.txtTitle)
        content = findViewById(R.id.txtContent)
        btnAdd = findViewById(R.id.btnUpload)
        btnAddImage = findViewById(R.id.btnAddImage)
        imgArticle = findViewById(R.id.imgArticle)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Thêm mới"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)



        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                uri = it!!
                imgArticle.setImageURI(it)
            }
        )

        btnAddImage.setOnClickListener{
            galleryImage.launch("image/*")
        }

        btnAdd.setOnClickListener {
            if (title.text.toString() == "" || content.text.toString() == "" || uri == null) {
                Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show()
            } else {

                saveData()
            }
        }


    }

    private fun saveData(){
        val txtTitle = title.text.toString()
        val txtContent = content.text.toString()
        val owner = auth.currentUser!!.email.toString()
        var article = Article("", "", "", "", "")
        val articleId = firebaseRef.push().key!!

        uri?.let {
            storageRef.child(articleId).putFile(it)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {url ->
                            val imgUrl = url.toString()
                            article = Article(txtTitle, owner, txtContent, imgUrl, LocalDate.now().toString())

                            firebaseRef.child(articleId).setValue(article)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, ArticleListActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this, "Save fail", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Save fail", Toast.LENGTH_SHORT).show()
                }
        }
    }


}