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

/**
 * 成绩数据访问对象
 * 处理成绩相关的数据库操作
 */
public class ScoreDAO {

    /**
     * 获取所有成绩记录（平时成绩和考试成绩分开展示）
     */
    public List<Student> getAllScoreRecords() {
        Connection connection = null;
        List<Student> scoreRecords = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            // 修改字段名为下划线格式
            String query = "SELECT student_id, student_name, gender, class_name, phone, usual_grade, exam_grade FROM student ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 处理平时成绩记录
                        if (resultSet.getBigDecimal("usual_grade") != null) {
                            Student usualScore = new Student();
                            usualScore.setStudentId(resultSet.getString("student_id"));
                            usualScore.setStudentName(resultSet.getString("student_name"));
                            usualScore.setUsualGrade(resultSet.getBigDecimal("usual_grade"));
                            scoreRecords.add(usualScore);
                        }

                        // 处理考试成绩记录
                        if (resultSet.getBigDecimal("exam_grade") != null) {
                            Student examScore = new Student();
                            examScore.setStudentId(resultSet.getString("student_id"));
                            examScore.setStudentName(resultSet.getString("student_name"));
                            examScore.setExamGrade(resultSet.getBigDecimal("exam_grade"));
                            scoreRecords.add(examScore);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询成绩记录失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return scoreRecords;
    }

    /**
     * 搜索成绩记录（按学号或姓名）
     */
    public List<Student> searchScoreRecords(String keyword) {
        Connection connection = null;
        List<Student> scoreRecords = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            // 修改字段名为下划线格式
            String query = "SELECT student_id, student_name, gender, class_name, phone, usual_grade, exam_grade FROM student " +
                    "WHERE student_id LIKE ? OR student_name LIKE ? ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String searchPattern = "%" + keyword + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 处理平时成绩记录
                        if (resultSet.getBigDecimal("usual_grade") != null) {
                            Student usualScore = new Student();
                            usualScore.setStudentId(resultSet.getString("student_id"));
                            usualScore.setStudentName(resultSet.getString("student_name"));
                            usualScore.setUsualGrade(resultSet.getBigDecimal("usual_grade"));
                            scoreRecords.add(usualScore);
                        }

                        // 处理考试成绩记录
                        if (resultSet.getBigDecimal("exam_grade") != null) {
                            Student examScore = new Student();
                            examScore.setStudentId(resultSet.getString("student_id"));
                            examScore.setStudentName(resultSet.getString("student_name"));
                            examScore.setExamGrade(resultSet.getBigDecimal("exam_grade"));
                            scoreRecords.add(examScore);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索成绩记录失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return scoreRecords;
    }

    /**
     * 添加成绩记录
     * @param studentId 学生学号
     * @param scoreType 成绩类型（usual:平时成绩, exam:考试成绩）
     * @param score 成绩值
     */
    public boolean addScore(String studentId, String scoreType, BigDecimal score) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 先检查学生是否存在
            if (!isStudentExists(studentId)) {
                return false;
            }

            String query;
            if ("usual".equals(scoreType)) {
                // 修改字段名为下划线格式
                query = "UPDATE student SET usual_grade = ? WHERE student_id = ?";
            } else {
                // 修改字段名为下划线格式
                query = "UPDATE student SET exam_grade = ? WHERE student_id = ?";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, score);
                preparedStatement.setString(2, studentId);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加成绩失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 编辑成绩记录
     * @param studentId 学生学号
     * @param scoreType 成绩类型
     * @param score 新成绩值
     */
    public boolean editScore(String studentId, String scoreType, BigDecimal score) {
        return addScore(studentId, scoreType, score); // 逻辑相同，复用方法
    }

    /**
     * 删除成绩记录
     * @param studentId 学生学号
     * @param scoreType 成绩类型
     */
    public boolean deleteScore(String studentId, String scoreType) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            String query;
            if ("usual".equals(scoreType)) {
                // 修改字段名为下划线格式
                query = "UPDATE student SET usual_grade = NULL WHERE student_id = ?";
            } else {
                // 修改字段名为下划线格式
                query = "UPDATE student SET exam_grade = NULL WHERE student_id = ?";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除成绩失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 检查学生是否存在
     */
    private boolean isStudentExists(String studentId) {
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
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    /**
     * 根据学号和成绩类型获取成绩
     */
    public BigDecimal getScoreByStudentIdAndType(String studentId, String scoreType) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            // 修改字段名为下划线格式
            String query = "SELECT " + ("usual".equals(scoreType) ? "usual_grade" : "exam_grade") +
                    " FROM student WHERE student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    /**
     * 根据类型获取成绩记录
     * @param scoreType 类型：all-全部, usual-平时成绩, exam-考试成绩
     */
    public List<Student> getScoreRecordsByType(String scoreType) {
        Connection connection = null;
        List<Student> scoreRecords = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT student_id, student_name, gender, class_name, phone, usual_grade, exam_grade FROM student ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 根据类型筛选
                        if ("usual".equals(scoreType) || "all".equals(scoreType)) {
                            if (resultSet.getBigDecimal("usual_grade") != null) {
                                Student usualScore = new Student();
                                usualScore.setStudentId(resultSet.getString("student_id"));
                                usualScore.setStudentName(resultSet.getString("student_name"));
                                usualScore.setUsualGrade(resultSet.getBigDecimal("usual_grade"));
                                scoreRecords.add(usualScore);
                            }
                        }

                        if ("exam".equals(scoreType) || "all".equals(scoreType)) {
                            if (resultSet.getBigDecimal("exam_grade") != null) {
                                Student examScore = new Student();
                                examScore.setStudentId(resultSet.getString("student_id"));
                                examScore.setStudentName(resultSet.getString("student_name"));
                                examScore.setExamGrade(resultSet.getBigDecimal("exam_grade"));
                                scoreRecords.add(examScore);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询成绩记录失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return scoreRecords;
    }

    /**
     * 搜索并筛选成绩记录
     * @param keyword 搜索关键词
     * @param scoreType 成绩类型
     */
    public List<Student> searchScoreRecordsByType(String keyword, String scoreType) {
        Connection connection = null;
        List<Student> scoreRecords = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT student_id, student_name, gender, class_name, phone, usual_grade, exam_grade FROM student " +
                    "WHERE student_id LIKE ? OR student_name LIKE ? ORDER BY student_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String searchPattern = "%" + keyword + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // 根据类型筛选
                        if ("usual".equals(scoreType) || "all".equals(scoreType)) {
                            if (resultSet.getBigDecimal("usual_grade") != null) {
                                Student usualScore = new Student();
                                usualScore.setStudentId(resultSet.getString("student_id"));
                                usualScore.setStudentName(resultSet.getString("student_name"));
                                usualScore.setUsualGrade(resultSet.getBigDecimal("usual_grade"));
                                scoreRecords.add(usualScore);
                            }
                        }

                        if ("exam".equals(scoreType) || "all".equals(scoreType)) {
                            if (resultSet.getBigDecimal("exam_grade") != null) {
                                Student examScore = new Student();
                                examScore.setStudentId(resultSet.getString("student_id"));
                                examScore.setStudentName(resultSet.getString("student_name"));
                                examScore.setExamGrade(resultSet.getBigDecimal("exam_grade"));
                                scoreRecords.add(examScore);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("搜索成绩记录失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return scoreRecords;
    }
}