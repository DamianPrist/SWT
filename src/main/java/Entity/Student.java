package Entity;

import java.math.BigDecimal;

public class Student {
    private String studentId;
    private String studentName;
    private String gender;
    private String className;
    private String phone;
    private BigDecimal usualGrade;
    private BigDecimal examGrade;

    public Student() {
    }

    // 修复构造器 - 移除测试用的错误构造器
    public Student(String studentId, String studentName, String gender, String className, String phone) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.className = className;
        this.phone = phone;
    }

    // 全参构造器
    public Student(String studentId, String studentName, String gender, String className, String phone, BigDecimal usualGrade, BigDecimal examGrade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.className = className;
        this.phone = phone;
        this.usualGrade = usualGrade;
        this.examGrade = examGrade;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getUsualGrade() {
        return usualGrade;
    }

    public void setUsualGrade(BigDecimal usualGrade) {
        this.usualGrade = usualGrade;
    }

    public BigDecimal getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(BigDecimal examGrade) {
        this.examGrade = examGrade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", gender='" + gender + '\'' +
                ", className='" + className + '\'' +
                ", phone='" + phone + '\'' +
                ", usualGrade=" + usualGrade +
                ", examGrade=" + examGrade +
                '}';
    }
}