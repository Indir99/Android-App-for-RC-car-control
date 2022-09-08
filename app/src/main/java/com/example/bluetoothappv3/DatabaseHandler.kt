package com.example.bluetoothappv3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_CONTACTS = "UserTable"

        private const val KEY_ID = "_id"
        private const val KEY_USERNAME = "userName"
        private const val KEY_FIRSTNAME = "firstName"
        private const val KEY_LASTNAME = "lastName"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD ="password"
        private const val KEY_CARID ="carID"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // crating table with fields
        // CREATE TABLE UserTable(_id INTEGER PRIMARY KEY, userName TEXT, firstName TEXT, lastName TEXT, email TEXT)
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_CARID + " INTEGER" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    fun addUser (user: User) : Long {
        //When we want to write, we need writable Database
        val db = this.writableDatabase
        // Content Values that we want to write into database
        val contentValues = ContentValues()
        contentValues.put(KEY_USERNAME,user.userName)
        contentValues.put(KEY_FIRSTNAME,user.firstName)
        contentValues.put(KEY_LASTNAME,user.lastName)
        contentValues.put(KEY_EMAIL,user.email)
        contentValues.put(KEY_PASSWORD,user.password)
        contentValues.put(KEY_CARID,user.carID)
        //Inserting row
        val success = db.insert(TABLE_CONTACTS,null, contentValues)
        //Closing database
       // db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewUser(): List<User> {

        val userList: MutableList<User> = mutableListOf()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

            var id: Int
            var userName: String
            var firstName: String
            var lastName: String
            var email: String
            var password: String
            var carID: Int

            if(cursor.moveToFirst()){
                do {
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    userName = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
                    firstName = cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME))
                    lastName = cursor.getString(cursor.getColumnIndex(KEY_LASTNAME))
                    email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                    password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                    carID = cursor.getInt(cursor.getColumnIndex(KEY_CARID))

                    val user = User (id,userName,firstName,lastName,email,password, carID)
                    userList.add(user)

                } while(cursor.moveToNext())
            }
            return userList

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return mutableListOf()
        }
    }

    fun updateUser(user: User): Int {
        //When we want to write, we need writable Database
        val db = this.writableDatabase
        // Content Values that we want to write into database
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,user.id)
        contentValues.put(KEY_USERNAME,user.userName)
        contentValues.put(KEY_FIRSTNAME,user.firstName)
        contentValues.put(KEY_LASTNAME,user.lastName)
        contentValues.put(KEY_EMAIL,user.email)
        contentValues.put(KEY_PASSWORD,user.password)
        contentValues.put(KEY_CARID,user.carID)
        //Inserting row
        val success = db.update(TABLE_CONTACTS,contentValues, KEY_ID + "=" + user.id, null)
        //Closing database
        db.close()
        return success
    }

    fun deleteUser(user: User) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.id)
        val success = db.delete(TABLE_CONTACTS, KEY_ID+"="+user.id, null)

        db.close()
        return success
    }

}