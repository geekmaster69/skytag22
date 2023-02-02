package com.example.skytah2.login

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.skytah2.MainActivity
import com.example.skytah2.databinding.ActivityLoginBinding
import com.example.skytah2.login.data.network.model.LoginDto
import com.example.skytah2.login.viewmodel.LoginViewModel
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dateFormat: SimpleDateFormat
    private val mLoginViewModel: LoginViewModel by viewModels()
    private val appUtils: AppUtils = AppUtils(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        binding.btnLogin.setOnClickListener {
           // login()
            loginFast()

        }

        binding.tvClaveCompuesta.setOnClickListener {
            val content = "${appUtils.getDeviceId()}_${appUtils.getDeviceName()}"
            val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
            val clip = ClipData.newPlainText("Id Compuesto", content)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Clave compuesta copiada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginFast() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun login() {
        val user = binding.etUser.text.toString().trim()
        val password = binding.etPassword.toString().trim()
        val fecha = dateFormat.format(Date())

        mLoginViewModel.onLogin(LoginDto(fecha = fecha))

        mLoginViewModel.loginModel.observe(this){
            if (it.mensaje == "Evento guardado con exito"){
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Usuario o Controaseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}