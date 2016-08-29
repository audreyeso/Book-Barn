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

import com.test.book_barn.models.Student;

import org.parceler.Parcels;
import com.test.book_barn.R;

/**
 * Users can rate a book.
 */

public class StarRatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private ImageButton doneButton;
    private Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating);

        setTitle("");
        student = (Student) Parcels.unwrap(getIntent().getParcelableExtra("student"));
        doneButton = (ImageButton) findViewById(R.id.activity_star_done_button);
        setUpRatingBar();


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StarRatingActivity.this, MainActivity.class);
                intent.putExtra("student", Parcels.wrap(student));
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

