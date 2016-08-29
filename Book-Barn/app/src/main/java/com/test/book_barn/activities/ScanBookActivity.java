package com.test.book_barn.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.test.book_barn.adapters.CustomCursorAdapterBooks;
import com.test.book_barn.googleApiModels.Example;
import com.test.book_barn.helpers.ClassroomOpenHelper;
import com.test.book_barn.interfaces.GoogleBooksAPI;
import com.test.book_barn.models.Book;
import com.test.book_barn.models.Student;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Users can scan a new book into their library.
 */

public class ScanBookActivity extends AppCompatActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static final String SCAN_MODE = "SCAN_MODE";
    static final String PRODUCT_MODE = "PRODUCT_MODE";

    private TextView  scan;
    private ImageButton newScanner;
    private String baseUrl = "https://www.googleapis.com/books/v1/";
    private String contents, title, author, full, imageUrl;
    private Student student;
    private ClassroomOpenHelper db;
    private Book book;
    private ListView bookResultsListView;
    private ArrayList<Book> bookArrayList;
    private long idStudent;
    private Cursor cursor;
    private CustomCursorAdapterBooks customCursorAdapterBooks;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_book);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        setTitle("");

        bookArrayList = new ArrayList<>();

        db = ClassroomOpenHelper.getInstance(this);

        setUpTextViews();

        setUpCursorAdapter();

        Intent intent = new Intent(this, ClassroomOpenHelper.class);
        intent.putExtra("idStudent", Parcels.wrap(idStudent));

        newScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction(ACTION_SCAN);
                intent.putExtra("SAVE_HISTORY", true);
                intent.putExtra(SCAN_MODE, PRODUCT_MODE);
                startActivityForResult(intent, 0);

            }
           }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("SCAN_RESULT");
                DatabaseAsyncTask dbTask = new DatabaseAsyncTask();
                dbTask.execute();
                Intent intent = new Intent(ScanBookActivity.this, SplashAddedBookActivity.class);
                intent.putExtra("student", Parcels.wrap(student));
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.no_wifi + contents, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DatabaseAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getBookDescription();
            return null;
        }

        @Override
        protected void onPostExecute(Void value) {
            super.onPostExecute(value);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected void getBookDescription() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GoogleBooksAPI googleBooksAPI = retrofit.create(GoogleBooksAPI.class);

            full = getString(R.string.isbn) + contents;

            Call<Example> call = googleBooksAPI.getBookDescription(full);

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    try {
                        Log.d("on response", "onResponse: " + response.body().getItems().get(0).getVolumeInfo().getTitle().toString());
                        Log.d("on response", "onResponse: " + response.body().getItems().get(0).getVolumeInfo().getAuthors().get(0).toString());

                        title = response.body().getItems().get(0).getVolumeInfo().getTitle();
                        author = response.body().getItems().get(0).getVolumeInfo().getAuthors().get(0).toString();
                        imageUrl = response.body().getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail();

                        setUpBooks();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ScanBookActivity.this, R.string.no_wifi, Toast.LENGTH_LONG).show();
        }

    }


    /**
     * create new book and add it to the database
     */
    public void setUpBooks() {

        if (title == null) {
            Toast.makeText(ScanBookActivity.this, R.string.scan_new_book, Toast.LENGTH_LONG).show();

        } else {

            book = new Book(title, author, imageUrl, idStudent);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(contents);
            book.setId(idStudent);
            book.setBookImage(imageUrl);
            bookArrayList.add(book);
            student.setBookArrayList(bookArrayList);
            db.addBook(book);
            customCursorAdapterBooks.swapCursor(db.getBooks(idStudent));

        }
    }

    public void setUpTextViews(){

        scan = (TextView) findViewById(R.id.textview_scanner);
        newScanner = (ImageButton) findViewById(R.id.book_scanner_button);
        bookResultsListView = (ListView) findViewById(R.id.result_book_listview);
        student = (Student) Parcels.unwrap(getIntent().getParcelableExtra("student"));
        scan.setText(student.getName() + getString(R.string.s_books));
    }

    /**
     * setup cursor adapter and listviews
     */

    public void setUpCursorAdapter() {
        idStudent = student.getId();
        cursor = db.getBooks(idStudent);
        customCursorAdapterBooks = new CustomCursorAdapterBooks(this, cursor);
        bookResultsListView.setAdapter(customCursorAdapterBooks);
    }

}