package com.test.book_barn.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by audreyeso on 8/18/16.
 */
@Parcel
public class Student {

    String name;
    int studentImage;
    ArrayList<Book> bookArrayList;
    long id;

    public Student() {

    }

    public Student(String name, ArrayList<Book> bookArrayList, long id) {
        this.name =name;
        this.bookArrayList = bookArrayList;
        this.id = id;
    }


    public int getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(int studentImage) {
        this.studentImage = studentImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Book> getBookArrayList() {
        return bookArrayList;
    }

    public void setBookArrayList(ArrayList<Book> bookArrayList) {
        this.bookArrayList = bookArrayList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}