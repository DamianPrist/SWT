package DAO;

import Entity.Student;
import connect.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // TODO: 添加学生数据访问逻辑

    /**
     * 新增学生
     *
     * @param student 学生对象
     * @return true 如果插入成功, false 如果失败
     */
    public boolean addStudent(Student student) {

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "INSERT INTO student (student_id, student_name, gender, usual_grade, exam_grade) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, student.getStudentId());
                preparedStatement.setString(2, student.getStudentName());
                preparedStatement.setString(3, student.getGender());
                preparedStatement.setBigDecimal(4, student.getUsualGrade());
                preparedStatement.setBigDecimal(5, student.getExamGrade());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库查询失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 根据ID删除学生
     *
     * @param studentId 学生ID
     * @return true 如果删除成功, false 如果失败
     */
    public boolean deleteStudent(String studentId) {

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "DELETE FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                // 执行删除操作
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库删除失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 辅助方法：将 ResultSet 的当前行封装成 Student 对象
     */
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setStudentName(rs.getString("student_name"));
        student.setGender(rs.getString("gender"));
        student.setUsualGrade(rs.getBigDecimal("usual_grade"));
        student.setExamGrade(rs.getBigDecimal("exam_grade"));
        return student;
    }
    /**
     * 查询所有学生列表
     * @return 包含所有学生的 List
     */
    public List<Student> getAllStudents() {
        Connection connection = null;
        List<Student> studentList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                // 执行查询
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // 循环遍历每一行数据
                    while (resultSet.next()) {
                        Student student = mapRowToStudent(resultSet); // 调用辅助方法
                        studentList.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询学生列表失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }

        return studentList;
    }
}
