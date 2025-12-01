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
     * 编辑成绩记录
     */
    public boolean editScore(String studentId, String scoreType, BigDecimal score) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 根据成绩类型设置不同的列
            String gradeColumn = "usual".equals(scoreType) ? "usual_grade" : "exam_grade";

            // 先获取另一个成绩
            String getOtherGradeQuery = "SELECT " +
                    ("usual".equals(scoreType) ? "exam_grade" : "usual_grade") +
                    " FROM student WHERE student_id = ?";

            BigDecimal otherGrade = null;
            try (PreparedStatement getStmt = connection.prepareStatement(getOtherGradeQuery)) {
                getStmt.setString(1, studentId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        otherGrade = rs.getBigDecimal(1);
                    }
                }
            }

            // 更新当前成绩并计算总成绩
            String updateQuery;
            if (otherGrade != null) {
                // 如果两个成绩都有，计算总成绩
                BigDecimal totalGrade;
                if ("usual".equals(scoreType)) {
                    totalGrade = score.multiply(new BigDecimal("0.4"))
                            .add(otherGrade.multiply(new BigDecimal("0.6")));
                } else {
                    totalGrade = otherGrade.multiply(new BigDecimal("0.4"))
                            .add(score.multiply(new BigDecimal("0.6")));
                }

                updateQuery = "UPDATE student SET " + gradeColumn + " = ?, total_grade = ? WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setBigDecimal(1, score);
                    preparedStatement.setBigDecimal(2, totalGrade);
                    preparedStatement.setString(3, studentId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // 如果只有一个成绩，不计算总成绩
                updateQuery = "UPDATE student SET " + gradeColumn + " = ? WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setBigDecimal(1, score);
                    preparedStatement.setString(2, studentId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("编辑成绩失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 添加成绩记录
     */
    public boolean addScore(String studentId, String scoreType, BigDecimal score) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 根据成绩类型设置不同的列
            String gradeColumn = "usual".equals(scoreType) ? "usual_grade" : "exam_grade";

            // 先获取另一个成绩
            String getOtherGradeQuery = "SELECT " +
                    ("usual".equals(scoreType) ? "exam_grade" : "usual_grade") +
                    " FROM student WHERE student_id = ?";

            BigDecimal otherGrade = null;
            try (PreparedStatement getStmt = connection.prepareStatement(getOtherGradeQuery)) {
                getStmt.setString(1, studentId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        otherGrade = rs.getBigDecimal(1);
                    }
                }
            }

            // 更新成绩并计算总成绩
            String updateQuery;
            if (otherGrade != null) {
                // 如果两个成绩都有，计算总成绩
                BigDecimal totalGrade;
                if ("usual".equals(scoreType)) {
                    totalGrade = score.multiply(new BigDecimal("0.4"))
                            .add(otherGrade.multiply(new BigDecimal("0.6")));
                } else {
                    totalGrade = otherGrade.multiply(new BigDecimal("0.4"))
                            .add(score.multiply(new BigDecimal("0.6")));
                }

                updateQuery = "UPDATE student SET " + gradeColumn + " = ?, total_grade = ? WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setBigDecimal(1, score);
                    preparedStatement.setBigDecimal(2, totalGrade);
                    preparedStatement.setString(3, studentId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // 如果只有一个成绩，不计算总成绩
                updateQuery = "UPDATE student SET " + gradeColumn + " = ? WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setBigDecimal(1, score);
                    preparedStatement.setString(2, studentId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加成绩失败: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    /**
     * 删除成绩记录
     */
    public boolean deleteScore(String studentId, String scoreType) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 根据成绩类型确定要删除的字段
            String gradeColumn = "usual".equals(scoreType) ? "usual_grade" : "exam_grade";
            String otherGradeColumn = "usual".equals(scoreType) ? "exam_grade" : "usual_grade";

            // 首先获取另一个成绩，判断是否需要清除总成绩
            String checkQuery = "SELECT " + otherGradeColumn + " FROM student WHERE student_id = ?";
            BigDecimal otherGrade = null;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, studentId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        otherGrade = rs.getBigDecimal(1);
                    }
                }
            }

            // 构建更新语句
            String updateQuery;
            if (otherGrade != null) {
                // 如果另一个成绩存在，将当前成绩设为NULL，并计算新的总成绩
                // 注意：实际上只有一个成绩时，总成绩应该为NULL
                // 这里将总成绩设为NULL，因为计算总成绩需要两个成绩
                updateQuery = "UPDATE student SET " + gradeColumn + " = NULL, total_grade = NULL WHERE student_id = ?";
            } else {
                // 如果另一个成绩也不存在，直接将当前成绩设为NULL，总成绩设为NULL
                updateQuery = "UPDATE student SET " + gradeColumn + " = NULL, total_grade = NULL WHERE student_id = ?";
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
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