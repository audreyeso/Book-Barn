package com.test.book_barn.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.test.book_barn.R;
import com.test.book_barn.adapters.CustomCursorAdapter;
import com.test.book_barn.helpers.ClassroomOpenHelper;
import com.test.book_barn.models.Classroom;
import com.test.book_barn.models.Student;

import org.parceler.Parcels;

import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {

    private Classroom classroom;
    private TextView classroomTitleTextView;
    private ClassroomOpenHelper db;
    private ListView studentListView;
    private EditText studentNameEditText;
    private ArrayList<Student> studentArrayList;
    private static final int REQUEST_CODE = 10;
    private Button addNewStudentButton, viewStudentDataButton;
    private Student student;
    private long idClassroom;
    private CustomCursorAdapter customCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        setTitle("");
        studentArrayList = new ArrayList<>();
        db = ClassroomOpenHelper.getInstance(this);
        setViews();
        setUpClassroomTitle();
        setupCursorAdapter();

        /**
         * check out the number of books a student has read
         */

      viewStudentDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudentActivity.this, StudentBookDataActivity.class);
                intent.putExtra("student", Parcels.wrap(student));
                intent.putExtra("classroom", Parcels.wrap(classroom));
                startActivity(intent);
            }
      });

        /**
         * add new student to the database
         */

        addNewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentNameString = studentNameEditText.getText().toString();
                student = new Student(studentNameString, null,idClassroom);
                student.setName(studentNameString);
                studentArrayList.add(student);
                classroom.setStudents(studentArrayList);
                db.addStudent(student);
                customCursorAdapter.swapCursor(db.getStudents(idClassroom));
                studentNameEditText.setText("");
            }
        });

        /**
         * get stucdent that was clicked on from the database and let them scan a book
         */

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) customCursorAdapter.getItem(position);
                Student selectedStudent;

                if (cursor.moveToPosition(position)) {
                    selectedStudent = new Student();
                    selectedStudent.setId(cursor.getLong(cursor.getColumnIndex("_id")));

                    String myName = cursor.getString(cursor.getColumnIndex("STUDENTNAMESCOLUMN"));
                    selectedStudent.setName(myName);

                    Intent intent = new Intent(AddStudentActivity.this, ScanBookActivity.class);
                    intent.putExtra("student", Parcels.wrap(selectedStudent));
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    /**
     * set cursor adapter and listview
     */

    private void setupCursorAdapter() {
        Cursor cursor = db.getStudents(idClassroom);
        customCursorAdapter = new CustomCursorAdapter(this, cursor);
        studentListView.setAdapter(customCursorAdapter);
    }

    /**
     * get classroom from Main Activity and set it
     * create intent to pass to next activity
     */
    private void setUpClassroomTitle() {
        classroom = (Classroom) Parcels.unwrap(getIntent().getParcelableExtra("classroom"));
        idClassroom = classroom.getId();
        Intent intent = new Intent(this, ClassroomOpenHelper.class);
        intent.putExtra("id", Parcels.wrap(idClassroom));
        classroomTitleTextView.setText(classroom.getClassroomName());
    }

    private void setViews() {
        studentListView = (ListView) findViewById(R.id.add_student_activity_class_list_view);
        addNewStudentButton = (Button) findViewById(R.id.add_student_activity_class_button);
        studentNameEditText = (EditText) findViewById(R.id.add_student_activity_edit_text_class_name);
        viewStudentDataButton = (Button) findViewById(R.id.add_student_activity_view_data_button);
        classroomTitleTextView = (TextView) findViewById(R.id.activity_add_student_classroom_title_textview);

    }
}
