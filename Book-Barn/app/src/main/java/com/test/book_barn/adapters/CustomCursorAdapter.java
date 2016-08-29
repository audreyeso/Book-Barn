package com.test.book_barn.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.book_barn.R;
import com.test.book_barn.helpers.ClassroomOpenHelper;

/**
 * Created by audreyeso on 8/22/16.
 */
public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recycler_view_student_list, parent, false);
    }

    /**
     * set up textviews for student name and set an icon depending on the length of the name
     */

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView textView = (TextView) view.findViewById(R.id.reycler_view_student_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.recycler_view_student_image);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.recycler_view_delete_button);

        final String classId = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_CLASSROOM_ID_KEY));
        String studentName = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_STUDENT_NAME));
        textView.setText(studentName);

        if (studentName.length() <= 2 || studentName.length() == 10) {
            imageView.setImageResource(R.drawable.cat);
        } else if (studentName.length() == 3 || studentName.length() == 7) {
            imageView.setImageResource(R.drawable.turtle);
        } else if (studentName.length() == 4 || studentName.length() == 8) {
            imageView.setImageResource(R.drawable.bunny);
        } else if (studentName.length() == 5 || studentName.length() == 12) {
            imageView.setImageResource(R.drawable.dog);
        } else if (studentName.length() == 9 || studentName.length() == 11) {
            imageView.setImageResource(R.drawable.chicken);
        } else {
            imageView.setImageResource(R.drawable.raccoon);
        }

        final int id = cursor.getInt(cursor.getColumnIndex(ClassroomOpenHelper.COL_ID_STUDENTS));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassroomOpenHelper classroomOpenHelper = ClassroomOpenHelper.getInstance(context);
                classroomOpenHelper.removeStudent(id);

                Log.d("Cursor", "remove student");
                swapCursor(classroomOpenHelper.getStudents(Long.parseLong(classId)));
            }
        });
    }

}

