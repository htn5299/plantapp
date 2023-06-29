package com.example.plant

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progress : ProgressDialog
    private val sharedPrefFile = "isfirstusingapp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtDangNhap = findViewById<TextView>(R.id.txtDangNhap)
        var txtEmail = findViewById<TextInputEditText>(R.id.txtEmail)
        var txtMatKhau = findViewById<TextInputEditText>(R.id.txtMatKhau)
        var txtNhapLaiMK = findViewById<TextInputEditText>(R.id.txtNhapLaiMatKhau)
        var btnDangKy = findViewById<MaterialButton>(R.id.btnDangKy)
        var txtQuenMK = findViewById<TextView>(R.id.txtQuenMK)

        progress = ProgressDialog(this)
        progress.setTitle("Đợi chút...")
        progress.setMessage("Đang đăng ký...")
        progress.setCanceledOnTouchOutside(false)

        btnDangKy.setOnClickListener{
            var email = txtEmail.text.toString().trim()
            var password = txtMatKhau.text.toString().trim()
            var repassword = txtNhapLaiMK.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.contains("@gmail.com")){
                Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show()
            }else if (TextUtils.isEmpty(password) || password.length < 8){
                Toast.makeText(this, "Mật khẩu không được để trống và có hơn 7 kí tự", Toast.LENGTH_SHORT).show()
            }else if (!password.equals(repassword)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
            }else{
                register(email, password)
            }

        }
        txtQuenMK.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtDangNhap.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(email: String, password: String) {
        progress.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
                    Context.MODE_PRIVATE)
                val sharedIdValue = sharedPreferences.getInt("check",0)
                val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putInt("check", 1)
                editor.apply()
                editor.commit()
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Đăng ký thất bại ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}