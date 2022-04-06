package com.example.connectdb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_items_std.*

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edContact: EditText
    private lateinit var edAddress: EditText
    private lateinit var btnAdd:Button
    private lateinit var btnView:Button
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter:StudentAdapter?=null
    private var std:StudentModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener {addStudent()}
        btnView.setOnClickListener {getStudent()}
        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edEmail.setText(it.email)
            edContact.setText(it.contact)
            edAddress.setText(it.address)
            std=it
        }
        adapter?.setonClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun deleteStudent(id:Int) {
        if (id==null)return
        val builder= AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete item?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){
                dialog, _ ->
                sqLiteHelper.deleteStudentById(id)
                getStudent()
                dialog.dismiss()
            }
        builder.setNegativeButton("No"){
                dialog, _ ->
            dialog.dismiss()
        }
        var alert = builder.create()
        alert.show()
    }


    private fun getStudent() {
        val stdList = sqLiteHelper.getAllStudent()
        Log.e("pppp","${stdList.size}")
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name=edName.text.toString()
        val email=edEmail.text.toString()
        val contact = edContact.text.toString()
        val address = edAddress.text.toString()
        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty()){
            Toast.makeText(this, "Please enter required field",Toast.LENGTH_SHORT).show()
        }else{
            val std=StudentModel(name = name,email = email,contact = contact,address = address)
            val status= sqLiteHelper.insertStudent(std)
            //check insert success or not success
            if(status>-1){
                Toast.makeText(this, "Student add . . .",Toast.LENGTH_SHORT).show()
                clearEditText()
            }else{
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edContact.setText("")
        edAddress.setText("")
        edName.requestFocus()
    }
    private fun initRecyclerView(){
        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter=adapter
    }
    private fun initView() {
        edName=findViewById(R.id.edName)
        edEmail=findViewById(R.id.edEmail)
        edContact=findViewById(R.id.edContact)
        edAddress=findViewById(R.id.edAddress)
        btnAdd=findViewById(R.id.btnAdd)
        btnView=findViewById(R.id.btnView)
        recyclerView=findViewById(R.id.recyclerView)
    }
}
