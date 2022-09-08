package com.example.bluetoothappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.bluetoothappv3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener() {
            Intent(this, LoginActivity::class.java).also {
                val userName = binding.etUserName.text.toString()
                val password = binding.etPassword.text.toString()
                val flag = checkData(userName,password)
                if(flag){
                    it.putExtra("EXTRA_USERNAME", userName)
                    startActivity(it)
                }
                else{
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.btnRegister.setOnClickListener(){
            Intent(this,RegisterActivity::class.java).also{
                startActivity(it)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miBluetooth -> Toast.makeText(this, "You clicked on bluetooth", Toast.LENGTH_LONG ).show()
            R.id.miSettings -> Toast.makeText(this, "You clicked on Settings", Toast.LENGTH_LONG ).show()
            R.id.miFavorites -> Toast.makeText(this, "You clicked on Favorites", Toast.LENGTH_LONG ).show()
            R.id.miFeedback -> Toast.makeText(this, "You clicked on Feedback", Toast.LENGTH_LONG ).show()
            R.id.miClose -> finish()
        }
        return true
    }

    fun getItemList() :  List<User> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        return  databaseHandler.viewUser()

    }

    fun checkData(userName: String, password: String) : Boolean{
        val userList = getItemList()
        for(user in userList){
            if(user.userName == userName && user.password == password){
                return true
            }
        }
        return false
    }
}