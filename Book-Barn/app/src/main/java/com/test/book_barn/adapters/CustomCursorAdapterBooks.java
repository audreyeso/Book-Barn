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

import com.squareup.picasso.Picasso;
import com.test.sql.R;
import com.test.sql.helpers.ClassroomOpenHelper;

/**
 * Created by audreyeso on 8/22/16.
 */

public class CustomCursorAdapterBooks extends CursorAdapter {
    private ImageView imageView;
    private String image;

    public CustomCursorAdapterBooks(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_custom_cursor_books, parent, false);
    }

    /**
     * set the title, author, and thumbnail when a book is scanned
     */

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView textViewTitle= (TextView) view.findViewById(R.id.adapter_custom_cursor_books_book_title);
        TextView textViewAuthor= (TextView) view.findViewById(R.id.adapter_custom_cursor_books_book_author);
        imageView = (ImageView) view.findViewById(R.id.adapter_custom_cursor_books_thumbnail);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.adapter_custom_cursor_books_delete_button);

        String title = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_BOOK_NAME));
        String author = cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_BOOK_AUTHOR));

        final String bookId= cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_STUDENT_BOOK_ID));

        textViewTitle.setText(title);
        textViewAuthor.setText(author);

        image = (cursor.getString(cursor.getColumnIndex(ClassroomOpenHelper.COL_BOOK_IMAGE)));


        Picasso.with(context)
                .load(image)
                .resize(90, 110)
                .into(imageView);

        final int id = cursor.getInt(cursor.getColumnIndex(ClassroomOpenHelper.COL_ID_BOOK));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassroomOpenHelper classroomOpenHelper = ClassroomOpenHelper.getInstance(context);
                classroomOpenHelper.removeBooks(id);

                Log.d("Cursor", "remove student");
                swapCursor(classroomOpenHelper.getBooks(Long.parseLong(bookId)));
            }
        });

    }
}


