package com.test.book_barn.models;

/**
 * Created by audreyeso on 8/18/16.
 */

import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class Classroom {

    String classroomName;
    ArrayList<Student> students;
    long id;

    ArrayList<Long>studentIds;

    public Classroom() {

    }

    public Classroom(String classroomName, ArrayList<Student> studentArrayList) {
        this.classroomName = classroomName;
        students = new ArrayList<>();
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }


    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addProduct(long id){
        if (!studentIds.contains(id)){
            studentIds.add(id);
        }
    }

    public void removeProduct(int productId){
        studentIds.remove(Long.valueOf(productId));
    }
}
