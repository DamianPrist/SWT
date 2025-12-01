package MainInterface.TotalGradeManagePackage;

import DAO.StudentDAO;
import Entity.Student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 总成绩管理后端逻辑类
 * 处理学生成绩的查询和排序操作
 */
public class TotalGradeManage {

    private StudentDAO studentDAO;

    public TotalGradeManage() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * 获取所有学生并按总成绩降序排列
     */
    public List<Student> getAllStudentsOrderByTotalGrade() {
        try {
            // 首先获取所有学生
            List<Student> allStudents = studentDAO.getAllStudents();

            // 对每个学生，如果平时成绩和考试成绩都存在，则计算并更新总成绩
            for (Student student : allStudents) {
                recalculateAndUpdateTotalGrade(student);
            }

            // 重新获取按总成绩排序的学生列表
            return studentDAO.getAllStudentsOrderByTotalGrade();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 根据关键字搜索学生并按总成绩降序排列
     */
    public List<Student> searchStudentsOrderByTotalGrade(String keyword) {
        try {
            // 首先搜索学生
            List<Student> students = studentDAO.searchStudents(keyword);

            // 对每个学生，如果平时成绩和考试成绩都存在，则计算并更新总成绩
            for (Student student : students) {
                recalculateAndUpdateTotalGrade(student);
            }

            // 重新获取按总成绩排序的学生列表
            return studentDAO.searchStudentsOrderByTotalGrade(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 重新计算并更新总成绩
     */
    private void recalculateAndUpdateTotalGrade(Student student) {
        try {
            if (student.getUsualGrade() != null && student.getExamGrade() != null) {
                // 计算总成绩：平时成绩*0.4 + 考试成绩*0.6
                BigDecimal usualGrade = student.getUsualGrade();
                BigDecimal examGrade = student.getExamGrade();
                BigDecimal totalGrade = usualGrade.multiply(new BigDecimal("0.4"))
                        .add(examGrade.multiply(new BigDecimal("0.6")));

                // 更新学生对象
                student.setTotalGrade(totalGrade);

                // 更新到数据库
                studentDAO.updateStudentGrade(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}