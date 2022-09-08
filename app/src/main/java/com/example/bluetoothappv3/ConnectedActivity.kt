package com.example.bluetoothappv3

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bluetoothappv3.databinding.ActivityConnectedBinding
import java.io.IOException
import java.util.*


class ConnectedActivity : AppCompatActivity() {

    //UUID witch is specific for HC-05/HC-06 bluetooth modules
    val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var isConnected = false

    //Bluetooth Socket for communication with HC module
    private lateinit var btSocket: BluetoothSocket

    private lateinit var binding: ActivityConnectedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
        val myBltDevice = intent.extras!!.getParcelable<BluetoothDevice>("EXTRA_BLT_DEVICE")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvBltDevice.text=myBltDevice?.name+"\n"+myBltDevice?.address
        }

        // Creating socket
        btSocket = myBltDevice!!.createRfcommSocketToServiceRecord(MY_UUID)
        if(isConnected == false) {
            try {
                btSocket.connect()
                isConnected=true
                Toast.makeText(this, "Connected to HC module", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Can't Connect to HC module", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnForward.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> sendCommand("1")
                    MotionEvent.ACTION_UP -> sendCommand("0")
                    MotionEvent.ACTION_BUTTON_PRESS -> sendCommand("1")
                    MotionEvent.ACTION_BUTTON_RELEASE -> sendCommand("0")
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
        binding.btnBackward.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> sendCommand("2")
                    MotionEvent.ACTION_UP -> sendCommand("0")
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding.btnLeft.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> sendCommand("3")
                    MotionEvent.ACTION_UP -> sendCommand("0")
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding.btnRight.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> sendCommand("4")
                    MotionEvent.ACTION_UP -> sendCommand("0")
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
//        binding.btnForward.setOnClickListener(){
//            sendCommand("1")
//        }
//        binding.btnBackward.setOnClickListener(){
//            sendCommand("2")
//        }
//        binding.btnLeft.setOnClickListener(){
//            sendCommand("3")
//        }
//        binding.btnRight.setOnClickListener(){
//            sendCommand("4")
//        }

    }

    private fun sendCommand(input: String) {
        if (btSocket != null) {
            try{
                btSocket!!.outputStream.write(input.toByteArray())
                //Toast.makeText(this, "Message sent:\n"+input, Toast.LENGTH_SHORT).show()
            } catch(e: IOException) {
                e.printStackTrace()
                //Toast.makeText(this, "Can't send", Toast.LENGTH_SHORT).show()
            }
        }
    }
}