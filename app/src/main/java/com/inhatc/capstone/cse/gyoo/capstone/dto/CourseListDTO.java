package com.inhatc.capstone.cse.gyoo.capstone.dto;

/**
 * Created by Gyoo on 2017-05-15.
 */

public class CourseListDTO {

    private String studentID;
    private String studentName;
    private String dATE;
    private String subjectName;
    private String subjectID;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
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

    public String getdATE() {
        return dATE;
    }

    public void setdATE(String dATE) {
        this.dATE = dATE;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "CourseListDTO{" +
                "studentID='" + studentID + '\'' +
                ", studentName='" + studentName + '\'' +
                ", dATE='" + dATE + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectID='" + subjectID + '\'' +
                '}';
    }
}
