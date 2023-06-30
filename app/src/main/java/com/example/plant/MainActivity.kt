package com.example.plant

import android.bluetooth.BluetoothCsipSetCoordinator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.plant.fragment.HomeFragment
import com.example.plant.fragment.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var chipNavigationBar: ChipNavigationBar
    lateinit var coordinator: CoordinatorLayout
    lateinit var btnAddNewPlant: FloatingActionButton
    private val cameraRequest = 1888
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        chipNavigationBar = findViewById(R.id.chip_app_bar)
        btnAddNewPlant = findViewById(R.id.btnAddNewPlant)
        coordinator = findViewById(R.id.coordinator)
        chipNavigationBar.setItemSelected(R.id.menu_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.root_layout, HomeFragment()).commit()
        coordinator.setBackgroundColor(getColor(R.color.black))

        bottomMenu()



        btnAddNewPlant.setOnClickListener{
            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), cameraRequest)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val intent = Intent(this, AddNewPlantActivity::class.java)
            val uri = covert(bitmap = photo, context = applicationContext)
            var bStream = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 50, bStream)
            val byteArray = bStream.toByteArray()
            intent.putExtra("image", byteArray)
            intent.putExtra("imageUri", uri.toString())
            startActivity(intent)
        }
    }

    private fun covert(bitmap: Bitmap, context: Context): Uri? {
        val imgFolders: File = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imgFolders.mkdirs()
            val file = File(imgFolders, "capture.jpeg")
            val output: FileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output.flush()
            output.close()
            uri = FileProvider.getUriForFile(context.applicationContext, "com.example.plant" + ".provider", file)

            return uri
        } catch (ex: Exception) {
        }
        return uri
    }

    private fun bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener {
            when (it) {
                R.id.menu_home -> {
                    coordinator.setBackgroundColor(getColor(R.color.white))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.root_layout, HomeFragment()).commit()
                }

                R.id.menu_profile -> {
                    coordinator.setBackgroundColor(getColor(R.color.white))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.root_layout, ProfileFragment()).commit()
                }
            }
        }
    }
}