package MainInterface.StudentManagePackage;

import DAO.StudentDAO;
import Entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生管理后端逻辑类
 * 处理学生数据的增删改查操作
 */
public class StudentManage {

    private StudentDAO studentDAO;

    public StudentManage() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * 获取所有学生列表
     */
    public List<Student> getAllStudents() {
        try {
            return studentDAO.getAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
            // 如果数据库操作失败，返回空列表
            return new ArrayList<>();
        }
    }

    /**
     * 根据关键字搜索学生（支持学号或姓名模糊查询）
     */
    public List<Student> searchStudents(String keyword) {
        try {
            return studentDAO.searchStudents(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 添加学生
     */
    public boolean addStudent(Student student) {
        try {
            // 检查学号是否已存在
            if (studentDAO.isStudentIdExists(student.getStudentId())) {
                return false;
            }
            return studentDAO.addStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新学生信息
     */
    public boolean updateStudent(Student student) {
        try {
            return studentDAO.updateStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除学生
     */
    public boolean deleteStudent(String studentId) {
        try {
            return studentDAO.deleteStudent(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据学号获取学生信息
     */
    public Student getStudentById(String studentId) {
        try {
            return studentDAO.getStudentById(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}