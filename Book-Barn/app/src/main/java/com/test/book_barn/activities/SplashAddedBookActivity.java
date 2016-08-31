package com.test.book_barn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.test.book_barn.models.Student;

import org.parceler.Parcels;

public class SplashAddedBookActivity extends AppCompatActivity {

    /**
     * Splash screen for when a new book is added
     */

    static final String STUDENT_KEY = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                public void run() {
                    finish();
                    Student student = (Student) Parcels.unwrap(getIntent().getParcelableExtra(STUDENT_KEY));
                    Intent intent = new Intent(getBaseContext(), AnswerQuestionActivity.class);
                    intent.putExtra(STUDENT_KEY,Parcels.wrap(student));
                    startActivity(intent);

                }
            }, 3000);
        }
    }

