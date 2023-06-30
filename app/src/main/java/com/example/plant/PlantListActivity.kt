package com.example.plant

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.plant.models.Plant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Objects

class PlantListActivity : AppCompatActivity() {

    var database = Firebase.database
    var myRef = database.getReference("plants")
    private lateinit var progress : ProgressDialog
    lateinit var gridView: GridView
    private lateinit var courseAdapter : GridViewAdapter
    lateinit var plants : List<Plant>
    var type : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_list)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Plant List"
        //set back button
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
                plants = snapshot.children.map {
                        dataSnapshot ->
                    val plantId = dataSnapshot.key // Lấy ID của nút con
                    val plant = dataSnapshot.getValue(Plant::class.java)!!.copy(id = plantId)
                    Log.i("plantWid", plant.toString())
                    plant
                }
                if (intent.hasExtra("type")){
                    Log.i("Type", intent.getStringExtra("type").toString())
                    type = intent.getStringExtra("type").toString()
                    var items : List<Plant> = emptyList()
                    for (item in plants){
                        if (item.type.equals(type)) {
                            Log.i("Equal", item.type.toString())
                            items = items + item
                        }
                    }
                    plants = items
                }
                courseAdapter = GridViewAdapter(courseList = plants, this@PlantListActivity)
                gridView.adapter = courseAdapter

                Log.i("Array", plants.toString())
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progress.dismiss()
            }
        })

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent1 = Intent(this, PlantDetailActivity::class.java)
            if (intent.hasExtra("type")){
                intent1.putExtra("disable", "disable")
            }
            intent1.putExtra("position", position)
            intent1.putExtra("image", plants[position].image)
            intent1.putExtra("name", plants[position].name)
            intent1.putExtra("owner", plants[position].owner)
            intent1.putExtra("type", plants[position].type)
            intent1.putExtra("date", plants[position].date)
            intent1.putExtra("description", plants[position].description)
            intent1.putExtra("id", plants[position].id)
            startActivity(intent1)
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}