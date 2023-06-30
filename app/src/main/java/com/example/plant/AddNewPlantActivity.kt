package com.example.plant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.plant.models.Plant
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import com.google.firebase.storage.FirebaseStorage as FirebaseStorage1


class AddNewPlantActivity : AppCompatActivity() {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var uri : Uri
    private val items = listOf("Hoa", "Cây ăn quả", "Cây gỗ", "Rau", "Cây cảnh")
    lateinit var dropdown : AutoCompleteTextView
    lateinit var plantName : TextInputEditText
    lateinit var plantDescription : TextInputEditText
    lateinit var btnAdd : MaterialButton
    lateinit var imgPlant : ImageView
    lateinit var btnAddImage : MaterialButton
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("plants")
    private val storageRef = FirebaseStorage.getInstance().getReference("images")
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_plant)
        dropdown = findViewById(R.id.txtPlantType)
        plantName = findViewById(R.id.txtPlantName)
        plantDescription = findViewById(R.id.txtPlantDescription)
        btnAdd = findViewById(R.id.btnUpload)
        imgPlant = findViewById(R.id.imgPlant)
        btnAddImage = findViewById(R.id.btnAddImage)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Thêm mới"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra("image")) {
            //convert to bitmap
            val byteArray = intent.getByteArrayExtra("image")
            if (byteArray != null) {
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            }
        }

        if (intent.hasExtra("imageUri")) {
            //convert to bitmap
            val uri = intent.getStringExtra("imageUri")
            if (uri != null) {
                Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show()
                imgPlant.setImageURI(Uri.parse(uri))
            }
        }

        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        dropdown.setAdapter(adapter)

        btnAdd.setOnClickListener {
            if (plantName.text.toString() == "" || plantDescription.text.toString() == "" || dropdown.text.toString() == "") {
                Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show()
            } else {
                if (intent.hasExtra("imageUri")) {
                    val uriImg: Uri = Uri.parse(intent.getStringExtra("imageUri"))
                    Log.d("Uri", uriImg.toString())
                    uri = uriImg
                }
                saveData()
            }
        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                uri = it!!
                imgPlant.setImageURI(it)
            }
        )

        btnAddImage.setOnClickListener{
            galleryImage.launch("image/*")
        }


    }

    private fun saveData(){
        val name = plantName.text.toString()
        val type = dropdown.text.toString()
        val owner = auth.currentUser!!.email.toString()
        val des = plantDescription.text.toString()
        var plant = Plant("", "", "", "", "")
        val plantId = firebaseRef.push().key!!

        uri?.let {
            storageRef.child(plantId).putFile(it)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {url ->
                            val imgUrl = url.toString()
                            plant = Plant(name, owner, type, des, imgUrl, LocalDate.now().toString())

                            firebaseRef.child(plantId).setValue(plant)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
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