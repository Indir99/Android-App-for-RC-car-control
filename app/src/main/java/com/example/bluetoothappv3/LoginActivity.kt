package com.example.bluetoothappv3

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.bluetoothappv3.databinding.ActivityLoginBinding
import java.util.*

class LoginActivity : AppCompatActivity() {


    lateinit var myBltDevice: BluetoothDevice
    private lateinit var binding: ActivityLoginBinding

    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var takePermission: ActivityResultLauncher<String>
    lateinit var takeResultLauncher: ActivityResultLauncher<Intent>


    //UUID witch is specific for HC-05/HC-06 bluetooth modules
    val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    //Bluetooth Socket for communication with HC module
    private lateinit var btSocket: BluetoothSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Permission
        takePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                takeResultLauncher.launch(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Bluetooth Permission is not Granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        takeResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    if (result.resultCode == RESULT_OK) {
                        Toast.makeText(applicationContext, "Bluetooth enabled", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(applicationContext, "Bluetooth disabled", Toast.LENGTH_SHORT)
                            .show()
                    }
                })


        requestPermissions()




        var userName = intent.getStringExtra("EXTRA_USERNAME")
        var carModel = "RC-car V1"
        val user = getUser(userName!!)
        val firstName = user.firstName
        val lastName = user.lastName


        binding.tvInfo.text =
            "Logged in as: $userName" + "\n" + "Name: $firstName" + "\n" + "Last name: $lastName" + "\n" + "RC car model: $carModel"

        binding.btnBluetoothOn.setOnClickListener() {
            takePermission.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
        }

        binding.btnBluetoothOff.setOnClickListener() {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter.disable()
                Toast.makeText(applicationContext, "Bluetooth Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Getting paired devices
        binding.btnBluetoothPaired.setOnClickListener() {
            val data: StringBuffer = StringBuffer() //Buffer for paired Devices
            val pairedDevices = bluetoothAdapter.bondedDevices // Get paired devices
            // pushing devices data in buffer and searching for HC module
            for (device in pairedDevices) {
                data.append("Device name: " + device.name + "\nDevice address: " + device.address)
                if (device.name == "HC-05" || device.name == "HC-06") {
                    myBltDevice = device
                }
            }
            if (data.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Paired devices are not found",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (myBltDevice.name == "HC-05" || myBltDevice.name == "HC-06") {
                    Toast.makeText(
                        applicationContext,
                        myBltDevice.name + "\n" + myBltDevice.address,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You have paired devices, but HC is not paired",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnBluetoothConnect.setOnClickListener() {
            Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
            Intent(this, ConnectedActivity::class.java).also {
                it.putExtra("EXTRA_BLT_DEVICE", myBltDevice)
                startActivity(it)
            }

            // Creating socket
            //btSocket = myBltDevice.createRfcommSocketToServiceRecord(MY_UUID)
            //try {
            //    btSocket.connect()
            //    Toast.makeText(this, "Connected to HC module", Toast.LENGTH_SHORT).show()
            //} catch (e: Exception) {
            //    Toast.makeText(this, "Can't Connect to HC module", Toast.LENGTH_SHORT).show()
            //}
        }
    }
    private fun hasBluetoothPermission() = ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED

    private fun hasBluetoothAdminPermission() = ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED

    private fun hasBluetoothConnectPermission() =ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(){
        var permissionToRequest = mutableListOf<String>()
        if(hasBluetoothPermission()) {
            permissionToRequest.add(Manifest.permission.BLUETOOTH)
        }
        if(hasBluetoothAdminPermission()) {
            permissionToRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        if(hasBluetoothConnectPermission()) {
            permissionToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if(permissionToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionToRequest.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    Log.d("MainActivityPermissions","${permissions[i]} is granted")
                }
            }
        }
    }


    fun getItemList() :  List<User> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        return  databaseHandler.viewUser()

    }

    fun getUser(userName: String) : User{
        val userList = getItemList()
        for(user in userList){
            if(user.userName == userName){
                return user
            }
        }
        return User(0,"None","None","None","None","None",0)
    }
}