package com.test.book_barn.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.book_barn.R;
import com.test.book_barn.models.Student;
import com.test.book_barn.views.DrawingView;

import org.parceler.Parcels;

import java.util.UUID;


/**
 * Users can sketch their favorite part of the story in this activity.
 */
public class AnswerQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    //you can get the name of student here as a toast to make sure that you are drawing in the correct
    //student profile

    private DrawingView drawView;
    //private Dialog brushDialog;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, homeBtn;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        getStudentName();
        setTitle("");
        setBrushSizes();
        setUpButtons();
        setUpViews();

        /**
         * return home
         */

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswerQuestionActivity.this, StarRatingActivity.class);
                intent.putExtra("student", Parcels.wrap(student));
                startActivity(intent);
            }
        });

    }

    /**
     * if paint is clicked, change and update color
     */

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.brush_button) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.brush_size);
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();

        } else if (view.getId() == R.id.eraser_button) {
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle(R.string.eraser_size);
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        } else if (view.getId() == R.id.paper_button) {
            createNewDrawing();

        } else if (view.getId() == R.id.save_button) {
            saveDrawing();
        }
    }

    /**
     * dialog box to create new drawing
     */
    private void createNewDrawing() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle(R.string.new_drawing);
        newDialog.setMessage(R.string.start_new_drawing);
        newDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }


    /**
     * save drawing to device or choose not to
     */
    private void saveDrawing() {

        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle(R.string.save_drawing);
        saveDialog.setMessage(R.string.save_drawing_to_device);
        saveDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //save sketch
                drawView.setDrawingCacheEnabled(true);
                //attempt to save
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString() + getString(R.string.png), getString(R.string.drawing));
               //tell user if dawing has saved or not saved
                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            R.string.drawing_saved, Toast.LENGTH_SHORT);
                    savedToast.show();
                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            R.string.drawing_not_saved, Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();

    }

    /**
     * get student name from previous activity
     */

    private void getStudentName() {
        student = (Student) Parcels.unwrap(getIntent().getParcelableExtra("student"));

    }

    /**
     * get brush sizes
     */

    private void setBrushSizes() {
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
    }

    private void setUpButtons() {
        drawBtn = (ImageButton) findViewById(R.id.brush_button);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) findViewById(R.id.eraser_button);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton) findViewById(R.id.paper_button);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);

        homeBtn = (ImageButton) findViewById(R.id.home_button);
    }

    private void setUpViews() {

        drawView = (DrawingView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawView.setBrushSize(mediumBrush);

    }
}
