package com.example.skytah2.login

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.skytah2.MainActivity
import com.example.skytah2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val appUtils: AppUtils = AppUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        binding.tvClaveCompuesta.setOnClickListener {
            val content = "${appUtils.getDeviceId()}_${appUtils.getDeviceName()}"
            val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
            val clip = ClipData.newPlainText("Id Compuesto", content)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Clave compuesta copiada", Toast.LENGTH_SHORT).show()
        }
    }
}