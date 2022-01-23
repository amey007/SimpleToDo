package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener =  object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTasks.removeAt(position)

                // Notify the adapter for the change
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

//        // detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // Write code to be executed after the user clicks the add button
//            Log.i( "Amy","The user has clicked the add button")
//        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recycleView = findViewById<RecyclerView>(R.id.recycleview)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recycleView.adapter = adapter
        // Set layout manager to position the items
        recycleView.layoutManager = LinearLayoutManager(this)

        // set up thr button and input field, so that the user can enter the task
        val inputTextField = findViewById<EditText>(R.id.addtaskfield)

        // Get reference of the button and add listener to it
        findViewById<Button>(R.id.button).setOnClickListener {
            // Grab the text that the user has inputted
            val userinput = inputTextField.text.toString()

            // Add the string to our listOfTasks
            listOfTasks.add(userinput)

            // Notify data adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size -1)

            // Clear the input field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data the user has inputted by reading and writing in the file

    //Create a method to get the file that we need
    fun getDataFile(): File{
        return File(filesDir, "data.txt")
    }

    // load the items by reading every line in the file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioexception: IOException){
            ioexception.printStackTrace()
        }
    }
    // Save items by writing in the file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioexception: IOException){
            ioexception.printStackTrace()
        }
    }
}