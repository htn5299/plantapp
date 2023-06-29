package com.example.plant

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var progress : ProgressDialog
    private val sharedPrefFile = "isfirstusingapp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkUser()

        var txtDangKy = findViewById<TextView>(R.id.txtDangKy)
        var txtEmail = findViewById<TextInputEditText>(R.id.txtEmail)
        var txtMatKhau = findViewById<TextInputEditText>(R.id.txtMatKhau)
        var btnDangNhap = findViewById<MaterialButton>(R.id.btnDangNhap)
        var txtQuenMK = findViewById<TextView>(R.id.txtQuenMK)

        progress = ProgressDialog(this)
        progress.setTitle("Đợi chút...")
        progress.setMessage("Đang đăng nhập...")
        progress.setCanceledOnTouchOutside(false)

        btnDangNhap.setOnClickListener{
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
                Context.MODE_PRIVATE)
            val sharedIdValue = sharedPreferences.getInt("check",0)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putInt("check", 1)
            editor.apply()
            editor.commit()
            var email = txtEmail.text.toString().trim()
            var password = txtMatKhau.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show()
            }else if (TextUtils.isEmpty(password) || password.length < 8){
                Toast.makeText(this, "Mật khẩu không được để trống và có hơn 7 kí tự", Toast.LENGTH_SHORT).show()
            }else{
                login(email, password)
            }

        }

        txtQuenMK.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtDangKy.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email : String, password : String) {
        progress.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progress.dismiss()
                val email = auth.currentUser!!.email
                Toast.makeText(this, "Đăng nhập thành công với email $email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener{
                progress.dismiss()
                Toast.makeText(this, "Tài khoản không khớp", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun checkUser(){
        val user = auth.currentUser
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}