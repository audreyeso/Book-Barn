package com.test.book_barn.adapters;

import android.content.Context;
import android.database.Cursor;
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
public class CustomCursorAdapterClassrooms extends CursorAdapter {

    public CustomCursorAdapterClassrooms(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_custom_cursor_classrooms, parent, false);
    }

    /**
     * get and set the classroom name and barn icon
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final TextView textView = (TextView) view.findViewById(R.id.adapter_custom_cursor_classroom_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.adapter_custom_cursor_barn_image);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.adapter_custom_cursor_delete_button);

        final String classroomName = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_CLASSROOM_NAME));

        textView.setText(classroomName);

        final int id = cursor.getInt(cursor.getColumnIndex(ClassroomOpenHelper.COL_ID));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassroomOpenHelper classroomOpenHelper = ClassroomOpenHelper.getInstance(context);
                classroomOpenHelper.removeClassroom(id);
                swapCursor(classroomOpenHelper.getClassrooms());
            }
        });
    }
}

