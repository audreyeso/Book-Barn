package com.test.book_barn.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.book_barn.R;
import com.test.book_barn.adapters.CustomCursorAdapter;
import com.test.book_barn.helpers.ClassroomOpenHelper;
import com.test.book_barn.models.Classroom;
import com.test.book_barn.models.Student;

import org.parceler.Parcels;

import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10;
    static final String STUDENT_KEY = "student";
    static final String CLASSROOM_KEY = "classroom";
    static final String ID_KEY = "id";
    private Classroom classroom;
    private TextView classroomTitleTextView;
    private ClassroomOpenHelper db;
    private ListView studentListView;
    private EditText studentNameEditText;
    private ArrayList<Student> studentArrayList;
    private Button addNewStudentButton, viewStudentDataButton;
    private ImageButton backHomeButton;
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
            setViewStudentIntent();

            }
      });

        /**
         * add new student to the database
         */

        addNewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getStudentAndAddToDatabase();
            }
        });

        /**
         * return home from add student activity
         */
        
        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /**
         * get student that was clicked on from the database and let them scan a book
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
                    intent.putExtra(STUDENT_KEY, Parcels.wrap(selectedStudent));
                    intent.putExtra(CLASSROOM_KEY, Parcels.wrap(classroom)); // just added
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
        classroom = (Classroom) Parcels.unwrap(getIntent().getParcelableExtra(CLASSROOM_KEY));
        idClassroom = classroom.getId();
        Intent intent = new Intent(this, ClassroomOpenHelper.class);
        intent.putExtra(ID_KEY, Parcels.wrap(idClassroom));
        classroomTitleTextView.setText(classroom.getClassroomName());
    }

    private void setViews() {
        studentListView = (ListView) findViewById(R.id.add_student_activity_class_list_view);
        addNewStudentButton = (Button) findViewById(R.id.add_student_activity_class_button);
        studentNameEditText = (EditText) findViewById(R.id.add_student_activity_edit_text_class_name);
        viewStudentDataButton = (Button) findViewById(R.id.add_student_activity_view_data_button);
        backHomeButton = (ImageButton)findViewById(R.id.add_student_activity_back__home_button);
        classroomTitleTextView = (TextView) findViewById(R.id.activity_add_student_classroom_title_textview);
    }

    private void getStudentAndAddToDatabase() {
        String studentNameString = studentNameEditText.getText().toString();
        if (studentNameString.isEmpty()) {
            Toast.makeText(AddStudentActivity.this, R.string.give_student_name_toast, Toast.LENGTH_LONG).show();
        } else {
            student = new Student(studentNameString, null, idClassroom);
            student.setName(studentNameString);
            studentArrayList.add(student);
            classroom.setStudents(studentArrayList);
            db.addStudent(student);
            customCursorAdapter.swapCursor(db.getStudents(idClassroom));
            studentNameEditText.setText("");
        }
    }

    private void setViewStudentIntent() {
        Intent intent = new Intent(AddStudentActivity.this, StudentBookDataActivity.class);
        intent.putExtra(STUDENT_KEY, Parcels.wrap(student));
        intent.putExtra(CLASSROOM_KEY, Parcels.wrap(classroom));
        startActivity(intent);
    }
}
