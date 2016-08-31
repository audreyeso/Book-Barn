package com.test.book_barn.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.test.book_barn.R;
import com.test.book_barn.adapters.CustomCursorAdapterStudentData;
import com.test.book_barn.helpers.ClassroomOpenHelper;
import com.test.book_barn.models.Classroom;
import com.test.book_barn.models.Student;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * User can view list of students and the number of books he/she has read
 */

public class StudentBookDataActivity extends AppCompatActivity {

    static final String STUDENT_KEY = "student";
    static final String CLASSROOM_KEY = "classroom";

    private ListView listView;
    private Student student;
    private Classroom classroom;
    private long idStudent;
    private Cursor cursor;
    private CustomCursorAdapterStudentData customCursorAdapterStudentData;

    private ArrayList<String> studentsAndBooks;

    private ClassroomOpenHelper db;

    public StudentBookDataActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_data);

        setTitle("");

        db = ClassroomOpenHelper.getInstance(this);

        listView = (ListView) findViewById(R.id.student_book_data_activity_listview);

        classroom = (Classroom) Parcels.unwrap(getIntent().getParcelableExtra(CLASSROOM_KEY));
        student =(Student) Parcels.unwrap (getIntent().getParcelableExtra(STUDENT_KEY));
        idStudent = classroom.getId();
        setUpCursorAdapter();

    }

    /**
     * set up cursor adapter
     */
    public void setUpCursorAdapter() {
        cursor = db.getStudents(idStudent);
        customCursorAdapterStudentData = new CustomCursorAdapterStudentData(this, cursor);
        listView.setAdapter(customCursorAdapterStudentData);

    }
}
