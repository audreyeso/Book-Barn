package com.test.book_barn.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.test.sql.R;
import com.test.sql.helpers.ClassroomOpenHelper;

/**
 * Created by audreyeso on 8/24/16.
 */
public class CustomCursorAdapterStudentData extends CursorAdapter {

    public CustomCursorAdapterStudentData(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_custom_cursor_student_data, parent, false);
    }

    /**
     * get the students names from the class and the number of books that they have scanned into their db
     */

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final TextView textViewName = (TextView) view.findViewById(R.id.adapter_custom_cursor_student_data_name);
        final TextView textViewNumOfBooks = (TextView) view.findViewById(R.id.adapter_custom_cursor_student_data_number_of_books);


        final String classId = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_CLASSROOM_ID_KEY));
        final int id = cursor.getInt(cursor.getColumnIndex(ClassroomOpenHelper.COL_ID_STUDENTS));
        String studentName = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_STUDENT_NAME));

        ClassroomOpenHelper classroomOpenHelper = ClassroomOpenHelper.getInstance(context);
        classroomOpenHelper.getStudents((classroomOpenHelper.getStudents(Long.parseLong(classId)).getColumnIndex(ClassroomOpenHelper.COL_STUDENT_NAME)));

        textViewName.setText(studentName);
        int numBook = classroomOpenHelper.getNumberofBooks(id);
        textViewNumOfBooks.setText("Books Read: " +numBook);

    }
}

