package com.test.book_barn.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.test.book_barn.R;
import com.test.book_barn.adapters.CustomCursorAdapterClassrooms;
import com.test.book_barn.fragments.AboutUsFragment;
import com.test.book_barn.fragments.ContactUsFragment;
import com.test.book_barn.fragments.CurrentAppFeaturesFragment;
import com.test.book_barn.fragments.InstructionsFragment;
import com.test.book_barn.helpers.ClassroomOpenHelper;
import com.test.book_barn.models.Classroom;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Main Activity: Users can find out about the app and create a classroom
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ClassroomOpenHelper db;
    private ListView classListView;
    private Button addNewClassButton;
    private EditText classNameEditText;
    private ArrayList<Classroom> classroomArrayList;
    private static final int REQUEST_CODE = 100;
    private Classroom newClassroom;
    private CustomCursorAdapterClassrooms customCursorAdapterClassrooms;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI(findViewById(R.id.drawer_layout));
        setTitle("");
        setUpNavigationDrawer();
        db = ClassroomOpenHelper.getInstance(this);
        setViews();
        setUpCursorAdapter();


        /**
         * add new class to the database
         */
        addNewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classroomNameString = classNameEditText.getText().toString();
                newClassroom = new Classroom(classroomNameString, null);
                newClassroom.setClassroomName(classroomNameString);
                db.addClassroom(newClassroom);
                customCursorAdapterClassrooms.swapCursor(db.getClassrooms());
                classNameEditText.setText("");
            }
        });

        /**
         * user can go into class selected and create a new class
         */

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) customCursorAdapterClassrooms.getItem(position);
                Classroom selectedClassroom;

                if (cursor.moveToPosition(position)) {
                    selectedClassroom = new Classroom();
                    selectedClassroom.setId(cursor.getLong(cursor.getColumnIndex("_id")));

                    String myName = cursor.getString(cursor.getColumnIndex("NAME"));
                    selectedClassroom.setClassroomName(myName);
                    selectedClassroom.setId(selectedClassroom.getId());

                    Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                    intent.putExtra("classroom", Parcels.wrap(selectedClassroom));
                    intent.putExtra("name", Parcels.wrap(myName));
                    intent.putExtra("id", Parcels.wrap(selectedClassroom.getId()));
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Classroom classroom = (Classroom) Parcels.unwrap(getIntent().getParcelableExtra("classroom"));
                int position = data.getIntExtra("position", 0);
                classroomArrayList.set(position, classroom);
                customCursorAdapterClassrooms.swapCursor(db.getClassrooms());

            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * return home button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * switch to different parts of navigation...4 options
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_bookbarn) {
            InstructionsFragment fragment = new InstructionsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_how_to_use) {
            CurrentAppFeaturesFragment fragment = new CurrentAppFeaturesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_who_created) {
            AboutUsFragment fragment = new AboutUsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_contact_us) {
            ContactUsFragment fragment = new ContactUsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * set up cursor adapter and listview
     */
    public void setUpCursorAdapter() {
        Cursor cursor = db.getClassrooms();
        customCursorAdapterClassrooms = new CustomCursorAdapterClassrooms(this, cursor);
        classListView.setAdapter(customCursorAdapterClassrooms);
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void setUpNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setViews() {
        classListView = (ListView) findViewById(R.id.main_activity_class_list_view);
        addNewClassButton = (Button) findViewById(R.id.main_activity_button_add_new_class);
        classNameEditText = (EditText) findViewById(R.id.main_activity_edit_text_class_name);
    }

}
