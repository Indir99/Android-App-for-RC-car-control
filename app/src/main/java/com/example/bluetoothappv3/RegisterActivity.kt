package com.example.bluetoothappv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bluetoothappv3.databinding.ActivityLoginBinding
import com.example.bluetoothappv3.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnRpRegister.setOnClickListener(){
            addRecord()
            Log.d("Indir","User added")
        }
    }


    fun addRecord() {
        val userName = binding.etRpUserName.text.toString()
        val firstName = binding.etRpFirstName.text.toString()
        val lastName = binding.etRpLastName.text.toString()
        val email = binding.RpEmail.text.toString()
        val password = binding.RpPassword.text.toString()
        val carID = binding.etRpCarID.text.toString().toInt()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(!userName.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            val status = databaseHandler.addUser(User(0,userName,firstName,lastName,email,password, carID))
            if(status>-1){
                Log.d("Indir","Status > -1")
                Toast.makeText(applicationContext,"Record Saved", Toast.LENGTH_LONG).show()
                binding.etRpUserName.text.clear()
                binding.etRpFirstName.text.clear()
                binding.etRpLastName.text.clear()
                binding.RpEmail.text.clear()
                binding.RpPassword.text.clear()
                binding.RpRetypePassword.text.clear()
                binding.etRpCarID.text.clear()
            }
            else{
                Log.d("Indir","Status  = -1")
            }
        }
    }
    fun getItemList() :  List<User> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        return  databaseHandler.viewUser()

    }
}