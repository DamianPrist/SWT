package DAO;

import Entity.Student;
import connect.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    /**
     * 新增学生
     */
    public boolean addStudent(Student student) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "INSERT INTO student (student_id, student_name, gender, class_name, phone) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, student.getStudentId());
                preparedStatement.setString(2, student.getStudentName());
                preparedStatement.setString(3, student.getGender());
                preparedStatement.setString(4, student.getClassName());
                preparedStatement.setString(5, student.getPhone());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加学生失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 根据ID删除学生
     */
    public boolean deleteStudent(String studentId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "DELETE FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除学生失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 更新学生信息,用于学生管理界面
     */
    public boolean updateStudent(Student student) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "UPDATE student SET student_name = ?, gender = ?, class_name = ?, phone = ? WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, student.getStudentName());
                preparedStatement.setString(2, student.getGender());
                preparedStatement.setString(3, student.getClassName());
                preparedStatement.setString(4, student.getPhone());
                preparedStatement.setString(5, student.getStudentId());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新学生信息失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 根据学号获取学生信息
     */
    public Student getStudentById(String studentId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRowToStudent(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询学生信息失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    /**
     * 查询所有学生列表
     */
    public List<Student> getAllStudents() {
        Connection connection = null;
        List<Student> studentList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Student student = mapRowToStudent(resultSet);
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

    /**
     * 根据关键字搜索学生（学号或姓名）
     */
    public List<Student> searchStudents(String keyword) {
        Connection connection = null;
        List<Student> studentList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student WHERE student_id LIKE ? OR student_name LIKE ? ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String searchPattern = "%" + keyword + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Student student = mapRowToStudent(resultSet);
                        studentList.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索学生失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return studentList;
    }

    /**
     * 检查学号是否存在
     */
    public boolean isStudentIdExists(String studentId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT COUNT(*) FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("检查学号是否存在失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }


    /**
     * 辅助方法：将 ResultSet 的当前行封装成 Student 对象
     */
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setStudentName(rs.getString("student_name"));
        student.setGender(rs.getString("gender"));
        student.setClassName(rs.getString("class_name"));
        student.setPhone(rs.getString("phone")); // 修复：这里应该是phone而不是className
        return student;
    }


    /**
     * 获取所有学生并按总成绩降序排列 - MySQL兼容版本
     */
    public List<Student> getAllStudentsOrderByTotalGrade() {
        Connection connection = null;
        List<Student> studentList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            // 首先，确保所有有平时成绩和考试成绩的学生都计算了总成绩
            String updateQuery = "UPDATE student SET total_grade = (usual_grade * 0.4 + exam_grade * 0.6) " +
                    "WHERE usual_grade IS NOT NULL AND exam_grade IS NOT NULL " +
                    "AND (total_grade IS NULL OR ABS(total_grade - (usual_grade * 0.4 + exam_grade * 0.6)) > 0.001)";

            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.executeUpdate();
            }

            // 方法1：使用IFNULL将NULL值转换为0，确保它们排在最后
            String query = "SELECT * FROM student ORDER BY IFNULL(total_grade, 0) DESC, student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Student student = mapRowToStudentWithGrade(resultSet);
                        studentList.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("按总成绩查询学生列表失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return studentList;
    }

    /**
     * 根据关键字搜索学生并按总成绩降序排列 - MySQL兼容版本
     */
    public List<Student> searchStudentsOrderByTotalGrade(String keyword) {
        Connection connection = null;
        List<Student> studentList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String searchPattern = "%" + keyword + "%";

            // 方法1：使用IFNULL将NULL值转换为0
            String query = "SELECT * FROM student WHERE student_id LIKE ? OR student_name LIKE ? " +
                    "ORDER BY IFNULL(total_grade, 0) DESC, student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Student student = mapRowToStudentWithGrade(resultSet);
                        studentList.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索学生并按总成绩排序失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return studentList;
    }

    /**
     * 更新学生成绩（自动计算总成绩）
     */
    public boolean updateStudentGrade(Student student) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 检查是否两个成绩都存在
            if (student.getUsualGrade() != null && student.getExamGrade() != null) {
                // 两个成绩都存在，计算总成绩
                BigDecimal totalGrade = student.getUsualGrade().multiply(new BigDecimal("0.4"))
                        .add(student.getExamGrade().multiply(new BigDecimal("0.6")));

                String query = "UPDATE student SET usual_grade = ?, exam_grade = ?, total_grade = ? WHERE student_id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setBigDecimal(1, student.getUsualGrade());
                    preparedStatement.setBigDecimal(2, student.getExamGrade());
                    preparedStatement.setBigDecimal(3, totalGrade);
                    preparedStatement.setString(4, student.getStudentId());

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // 至少一个成绩为NULL，将总成绩设为NULL
                String query = "UPDATE student SET " +
                        "usual_grade = ?, " +
                        "exam_grade = ?, " +
                        "total_grade = NULL " +
                        "WHERE student_id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setBigDecimal(1, student.getUsualGrade());
                    preparedStatement.setBigDecimal(2, student.getExamGrade());
                    preparedStatement.setString(3, student.getStudentId());

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新学生成绩失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 辅助方法：将 ResultSet 的当前行封装成 Student 对象（包含成绩字段）
     */
    private Student mapRowToStudentWithGrade(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setStudentName(rs.getString("student_name"));
        student.setGender(rs.getString("gender"));
        student.setClassName(rs.getString("class_name"));
        student.setPhone(rs.getString("phone"));

        // 获取成绩字段
        BigDecimal usualGrade = rs.getBigDecimal("usual_grade");
        BigDecimal examGrade = rs.getBigDecimal("exam_grade");
        BigDecimal totalGrade = rs.getBigDecimal("total_grade");

        if (usualGrade != null) {
            student.setUsualGrade(usualGrade);
        }
        if (examGrade != null) {
            student.setExamGrade(examGrade);
        }
        if (totalGrade != null) {
            student.setTotalGrade(totalGrade);
        }

        return student;
    }


    /**
     * 验证学生登录（通过学号和密码）
     */
    public Student validateStudentLogin(String studentId, String password) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 假设student表有password字段
            String query = "SELECT * FROM student WHERE student_id = ? AND password = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRowToStudent(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("验证学生登录失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    /**
     * 获取学生信息（包含成绩字段）
     */
    public Student getStudentWithGradeById(String studentId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRowToStudentWithGrade(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询学生信息失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    public Student getStudentGradeById(String studentId) {
        return getStudentWithGradeById(studentId);
    }
}
