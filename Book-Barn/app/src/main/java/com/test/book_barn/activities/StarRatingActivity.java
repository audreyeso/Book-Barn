package com.test.book_barn.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.test.book_barn.R;
import com.test.book_barn.models.Classroom;
import com.test.book_barn.models.Student;

import org.parceler.Parcels;

/**
 * Users can rate a book.
 */

public class StarRatingActivity extends AppCompatActivity {


    static final String STUDENT_KEY = "student";
    static final String CLASSROOM_KEY = "classroom";

    private RatingBar ratingBar;
    private ImageButton doneButton;
    private Student student;
    private Classroom classroom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating);

        setTitle("");
        student = (Student) Parcels.unwrap(getIntent().getParcelableExtra(STUDENT_KEY));
        classroom = (Classroom) Parcels.unwrap(getIntent().getParcelableExtra(CLASSROOM_KEY));
        doneButton = (ImageButton) findViewById(R.id.activity_star_done_button);
        setUpRatingBar();


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StarRatingActivity.this, AddStudentActivity.class);
                intent.putExtra(STUDENT_KEY, Parcels.wrap(student));
                intent.putExtra(CLASSROOM_KEY, Parcels.wrap(classroom));
                startActivity(intent);

            }
        });

    }

    /**
     * set up rating bar and change color of stars
     */

    public void setUpRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.activity_star_rating_bar_stars);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

    }

}

