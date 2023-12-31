package com.example.plant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.plant.onboarding.OnboardingItem
import com.example.plant.onboarding.OnboardingItemsAdapter
import com.google.android.material.button.MaterialButton

class IntroductActivity : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private val sharedPrefFile = "isfirstusingapp"

    lateinit var btnGetStart : MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduct)
        btnGetStart = findViewById(R.id.btnGetStarted)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getInt("check",0)
        if (sharedIdValue == 0){
            setOnBoardingItems()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnGetStart.setOnClickListener{
            navigate()
        }

//        findViewById<TextView>(R.id.txtSkip).setOnClickListener{
//            navigate()
//        }



    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getInt("check", 0)
        if (sharedIdValue == 0){
            setOnBoardingItems()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getInt("check", 0)
        if (sharedIdValue == 0){
            setOnBoardingItems()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun navigate(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setOnBoardingItems(){
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.intro1,
                    title = "Identify Plants",
                    content = "You can identify the plants you don't know through your camera"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro2,
                    title = "Learn Many Plants Species",
                    content = "Let's learn about the many plant species that exist in this world"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro3,
                    title = "Read Many Articles About Plant",
                    content = "Let's learn more about beautiful plants and read many articles about plants and gardening"
                ),
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingItemsAdapter
        findViewById<ImageView>(R.id.imgNext).setOnClickListener{
            if (onboardingViewPager.currentItem + 1 < onboardingItemsAdapter.itemCount){
                onboardingViewPager.currentItem += 1
            }else{
                navigate()
            }
        }
    }
}