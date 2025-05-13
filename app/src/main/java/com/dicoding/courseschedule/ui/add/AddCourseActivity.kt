package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Calendar.HOUR
import java.util.Calendar.MINUTE
import java.util.Locale


class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener{

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var courseNameInput: TextInputEditText
    private lateinit var lecturerInput: TextInputEditText
    private lateinit var noteInput: TextInputEditText
    private lateinit var spinnerDay: Spinner
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_course)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModelFactory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddCourseViewModel::class.java]

        supportActionBar?.setTitle(R.string.add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        courseNameInput = findViewById(R.id.ed_course_name)
        lecturerInput = findViewById(R.id.ed_lecturer)
        noteInput = findViewById(R.id.ed_note)
        spinnerDay = findViewById(R.id.spinner_day)
        startTimeTextView = findViewById(R.id.tv_start_time)
        endTimeTextView = findViewById(R.id.tv_end_time)

        findViewById<ImageButton>(R.id.ib_start_time).setOnClickListener {
            showTimePicker("START_TIME_PICKER")
        }

        findViewById<ImageButton>(R.id.ib_end_time).setOnClickListener {
            showTimePicker("END_TIME_PICKER")
        }

        viewModel.saved.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isSaved ->
                if (isSaved) {
                    Toast.makeText(this, "Success To Save Course", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Success To Save Course", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                saveCourse()
                true

            }
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun saveCourse() {
        val courseName = courseNameInput.text.toString().trim()
        val lecturer = lecturerInput.text.toString().trim()
        val note = noteInput.text.toString().trim()
        val day = spinnerDay.selectedItemPosition
        val startTime = startTimeTextView.text.toString().trim()
        val endTime = endTimeTextView.text.toString().trim()

        if (courseName.isEmpty() || lecturer.isEmpty() || note.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)

    }

    private fun showTimePicker(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.format(calendar.time)
        when (tag) {
            "START_TIME_PICKER" -> startTimeTextView.text = time
            "END_TIME_PICKER" -> endTimeTextView.text = time
        }
    }
}