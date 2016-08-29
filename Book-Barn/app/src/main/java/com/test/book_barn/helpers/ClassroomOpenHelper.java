package com.test.book_barn.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.book_barn.models.Book;
import com.test.book_barn.models.Classroom;
import com.test.book_barn.models.Student;

/**
 * Created by audreyeso on 8/18/16.
 */
public class ClassroomOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = "ClassroomOpenHelper";

    public static ClassroomOpenHelper curInstance;

    private static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "CLASSROOM_DB";

    public static final String CLASSROOM_TABLE_NAME = "CLASSROOM";

    public static final String COL_ID = "_id";
    public static final String COL_CLASSROOM_NAME = "NAME";
    public static final String COL_CLASSROOM_STUDENT_NAMES = "STUDENTNAME";

    public static final String[] CLASSROOM_COLUMNS = {COL_ID, COL_CLASSROOM_NAME};

    public static final String CREATE_CLASSROOM_TABLE = "CREATE TABLE " + CLASSROOM_TABLE_NAME +
            "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CLASSROOM_NAME + " TEXT, " +
            COL_CLASSROOM_STUDENT_NAMES + " TEXT)";


    public static final String STUDENT_TABLE_NAME = "STUDENT";

    public static final String COL_ID_STUDENTS = "_id";
    public static final String COL_CLASSROOM_ID_KEY = "CLASSID";
    public static final String COL_STUDENT_NAME = "STUDENTNAMESCOLUMN";


    public static final String[] STUDENT_COLUMNS = {COL_ID_STUDENTS, COL_STUDENT_NAME, COL_CLASSROOM_ID_KEY};

    public static final String CREATE_STUDENT_TABLE = "CREATE TABLE " + STUDENT_TABLE_NAME +
            "(" +
            COL_ID_STUDENTS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CLASSROOM_ID_KEY + " TEXT, " +
            COL_STUDENT_NAME + " TEXT)";

    public static final String BOOK_TABLE_NAME = "BOOK";
    public static final String COL_ID_BOOK = "_id";
    public static final String COL_BOOK_ISBN = "ISBN";
    public static final String COL_STUDENT_BOOK_ID = "BOOKSTUDENTID";
    public static final String COL_BOOK_NAME = "BOOKNAME";
    public static final String COL_BOOK_AUTHOR = "AUTHOR";
    public static final String COL_BOOK_IMAGE = "IMAGE";

    public static final String[] BOOK_COLUMNS = {COL_ID_BOOK, COL_BOOK_ISBN, COL_STUDENT_BOOK_ID, COL_BOOK_NAME, COL_BOOK_AUTHOR, COL_BOOK_IMAGE};

    public static final String CREATE_BOOK_TABLE = "CREATE TABLE " + BOOK_TABLE_NAME +
            "(" +
            COL_ID_BOOK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_BOOK_ISBN + " TEXT, " +
            COL_STUDENT_BOOK_ID + " TEXT, " +
            COL_BOOK_NAME + " TEXT, " +
            COL_BOOK_AUTHOR + " TEXT, " +
            COL_BOOK_IMAGE + " TEXT)";

    public ClassroomOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ClassroomOpenHelper getInstance(Context context) {
        if (curInstance == null) {
            curInstance = new ClassroomOpenHelper(context.getApplicationContext());
        }
        return curInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASSROOM_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CLASSROOM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_BOOK_TABLE);
        this.onCreate(db);
    }

    public void addClassroom(Classroom classroom) {
        ContentValues values = new ContentValues();
        values.put(COL_CLASSROOM_NAME, classroom.getClassroomName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(CLASSROOM_TABLE_NAME, null, values);
        db.close();
    }

    public void addStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, student.getName());
        values.put(COL_CLASSROOM_ID_KEY, student.getId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(STUDENT_TABLE_NAME, null, values);
        db.close();
    }


    public void addBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_NAME, book.getTitle());
        values.put(COL_BOOK_AUTHOR, book.getAuthor());
        values.put(COL_STUDENT_BOOK_ID, book.getId());
        values.put(COL_BOOK_IMAGE, book.getBookImage());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(BOOK_TABLE_NAME, null, values);
        db.close();
    }

    public void removeClassroom(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CLASSROOM_TABLE_NAME, COL_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public void removeStudent(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(STUDENT_TABLE_NAME, COL_ID_STUDENTS + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeBooks(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(BOOK_TABLE_NAME, COL_ID_BOOK + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getClassrooms() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CLASSROOM_TABLE_NAME, CLASSROOM_COLUMNS, null, null, null, null, null);
        return cursor;
    }

    public Cursor getStudents(long id) {

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ClassroomOpenHelper.STUDENT_TABLE_NAME,
                null,
                ClassroomOpenHelper.COL_CLASSROOM_ID_KEY + " = ?",
                new String[]{id + ""},
                null, null, null);
        cursor.moveToFirst();
        return cursor;

    }


    public Cursor getBooks(long id) {

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ClassroomOpenHelper.BOOK_TABLE_NAME,
                null,
                ClassroomOpenHelper.COL_STUDENT_BOOK_ID + " = ?",
                new String[]{id + ""},
                null, null, null);
        cursor.moveToFirst();
        return cursor;

    }

    public int getNumberofBooks(long id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ClassroomOpenHelper.BOOK_TABLE_NAME,
                null,
                ClassroomOpenHelper.COL_STUDENT_BOOK_ID + " = ?",
                new String[]{id + ""},
                null, null, null);
        cursor.moveToFirst();

        return cursor.getCount();
    }
}



