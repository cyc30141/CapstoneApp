package com.inhatc.capstone.cse.gyoo.capstone.dto;

/**
 * Created by Gyoo on 2017-04-24.
 */

public class StudentDTO {

    private String studentID;
    private String studentPW;
    private String studentName;
    private String studentPhoto;
    private String studentPhoneNumber;
    private String studentDeviceId;
    private String Grade;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentPW() {
        return studentPW;
    }

    public void setStudentPW(String studentPW) {
        this.studentPW = studentPW;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getStudentPhoneNumber() {
        return studentPhoneNumber;
    }

    public void setStudentPhoneNumber(String studentPhoneNumber) {
        this.studentPhoneNumber = studentPhoneNumber;
    }

    public String getStudentDeviceId() {
        return studentDeviceId;
    }

    public void setStudentDeviceId(String studentDeviceId) {
        this.studentDeviceId = studentDeviceId;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentID='" + studentID + '\'' +
                ", studentPW='" + studentPW + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentPhoto='" + studentPhoto + '\'' +
                ", studentPhoneNumber='" + studentPhoneNumber + '\'' +
                ", studentDeviceId='" + studentDeviceId + '\'' +
                ", Grade='" + Grade + '\'' +
                '}';
    }
}
