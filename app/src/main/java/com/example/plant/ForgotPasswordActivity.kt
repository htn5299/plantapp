package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        var email = findViewById<TextInputEditText>(R.id.txtEmail)
        var btnReset = findViewById<MaterialButton>(R.id.btnQuenMK)

        btnReset.setOnClickListener{
            auth.sendPasswordResetEmail(email.text.toString().trim())
                .addOnSuccessListener {
                    Toast.makeText(this, "Kiểm tra email của bạn", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Thay đổi mật khẩu thất bại ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}