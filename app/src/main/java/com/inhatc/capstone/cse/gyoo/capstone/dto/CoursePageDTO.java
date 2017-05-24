package com.inhatc.capstone.cse.gyoo.capstone.dto;

/**
 * Created by Gyoo on 2017-05-23.
 */

public class CoursePageDTO {

    int week;
    String studentPhoto;
    String studentID;
    String studentName;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "CoursePageDTO{" +
                "week=" + week +
                ", studentPhoto='" + studentPhoto + '\'' +
                ", studentID='" + studentID + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
