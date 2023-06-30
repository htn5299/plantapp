package com.example.plant.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.plant.AddNewArticleActivity
import com.example.plant.AddNewPlantActivity
import com.example.plant.ArticleDetailActivity
import com.example.plant.ArticleGridViewAdapter
import com.example.plant.ArticleListActivity
import com.example.plant.LoginActivity
import com.example.plant.MainActivity
import com.example.plant.PlantListActivity
import com.example.plant.R
import com.example.plant.models.Article
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var auth = Firebase.auth

    lateinit var imgProfile : ShapeableImageView
    lateinit var txtDisplayName : TextView
    lateinit var cardViewUpload : CardView
    lateinit var cardViewPlant : CardView
    lateinit var cardViewArticle : CardView
    lateinit var grvArticle : GridView
    lateinit var rltPlant1 : RelativeLayout
    lateinit var rltPlant2 : RelativeLayout
    lateinit var rltPlant3 : RelativeLayout
    lateinit var rltPlant4 : RelativeLayout
    lateinit var rltPlant5 : RelativeLayout
    private val items = listOf("Hoa", "Cây ăn quả", "Cây gỗ", "Rau", "Cây cảnh")
    private val cameraRequest = 1888
    var database = Firebase.database
    var myRef = database.getReference("articles")
    private lateinit var progress : ProgressDialog
    private lateinit var courseAdapter : ArticleGridViewAdapter
    lateinit var articles : List<Article>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imgProfile = view.findViewById(R.id.imgPofile)
        cardViewUpload = view.findViewById(R.id.cardViewUpload)
        cardViewPlant = view.findViewById(R.id.cardViewPlant)
        cardViewArticle = view.findViewById(R.id.cardViewArticle)
        grvArticle = view.findViewById(R.id.grvArticleHome)
        txtDisplayName = view.findViewById(R.id.txtDisplayName)
        rltPlant1 = view.findViewById(R.id.rltPlant1)
        rltPlant2 = view.findViewById(R.id.rltPlant2)
        rltPlant3 = view.findViewById(R.id.rltPlant3)
        rltPlant4 = view.findViewById(R.id.rltPlant4)
        rltPlant5 = view.findViewById(R.id.rltPlant5)
        progress = ProgressDialog(view.context)
        progress.setTitle("Đợi chút...")
        progress.setMessage("Đang tải dữ liệu...")
        progress.setCanceledOnTouchOutside(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtDisplayName.text = "Hello ${Firebase.auth.currentUser!!.email.toString().replace("@gmail.com", "")},"
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.show()
                Log.i("SNAPSHOT", snapshot.value.toString())
                articles = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(Article::class.java)!!
                }
                courseAdapter = ArticleGridViewAdapter(articleList = articles, view.context)
                grvArticle.adapter = courseAdapter

                Log.i("Array", articles.toString())
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progress.dismiss()
            }
        })

        grvArticle.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent1 = Intent(this.context, ArticleDetailActivity::class.java)
            intent1.putExtra("image", articles[position].image)
            intent1.putExtra("title", articles[position].title)
            intent1.putExtra("owner", articles[position].owner)
            intent1.putExtra("content", articles[position].content)
            intent1.putExtra("date", articles[position].date)
            intent1.putExtra("disable", "disable")
            startActivity(intent1)
        }
        imgProfile.setOnClickListener{
            val popup = PopupMenu(this.context, imgProfile)
            popup.menuInflater
                .inflate(R.menu.profile_menu, popup.menu);

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.one -> {
                        if (auth.currentUser != null) {
                            Firebase.auth.signOut()
                            val intent = Intent(this.context, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    R.id.two -> {
                        val intent = Intent(this.context, AddNewArticleActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            })
            popup.show()
        }

        cardViewUpload.setOnClickListener{
            val intent = Intent(view.context, AddNewPlantActivity::class.java)
            startActivity(intent)
        }




        cardViewArticle.setOnClickListener{
            val intent = Intent(this.context, ArticleListActivity::class.java)
            startActivity(intent)
        }

        cardViewPlant.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            startActivity(intent)
        }

        rltPlant1.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            intent.putExtra("type", items[0])
            startActivity(intent)
        }
        rltPlant2.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            intent.putExtra("type", items[1])
            startActivity(intent)
        }
        rltPlant3.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            intent.putExtra("type",  items[2])
            startActivity(intent)
        }
        rltPlant4.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            intent.putExtra("type", items[3])
            startActivity(intent)
        }
        rltPlant5.setOnClickListener{
            val intent = Intent(this.context, PlantListActivity::class.java)
            intent.putExtra("type",  items[4])
            startActivity(intent)
        }
    }


}