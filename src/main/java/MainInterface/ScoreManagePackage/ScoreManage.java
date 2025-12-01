package MainInterface.ScoreManagePackage;

import DAO.ScoreDAO;
import Entity.Student;

import java.math.BigDecimal;
import java.util.List;

/**
 * 成绩管理后端逻辑类
 * 处理成绩数据的增删改查操作
 */
public class ScoreManage {

    private ScoreDAO scoreDAO;
    private String currentFilterType = "all"; // 默认显示全部

    public ScoreManage() {
        this.scoreDAO = new ScoreDAO();
    }

    /**
     * 设置当前筛选类型
     * @param filterType all-全部, usual-平时成绩, exam-考试成绩
     */
    public void setCurrentFilterType(String filterType) {
        this.currentFilterType = filterType;
    }

    /**
     * 获取当前筛选类型
     */
    public String getCurrentFilterType() {
        return currentFilterType;
    }

    /**
     * 获取所有成绩记录（带筛选）
     */
    public List<Student> getAllScoreRecords() {
        try {
            return scoreDAO.getScoreRecordsByType(currentFilterType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 搜索成绩记录（带筛选）
     */
    public List<Student> searchScoreRecords(String keyword) {
        try {
            return scoreDAO.searchScoreRecordsByType(keyword, currentFilterType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加成绩记录
     */
    public boolean addScore(String studentId, String scoreType, BigDecimal score) {
        try {
            return scoreDAO.addScore(studentId, scoreType, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 编辑成绩记录
     */
    public boolean editScore(String studentId, String scoreType, BigDecimal score) {
        try {
            return scoreDAO.editScore(studentId, scoreType, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除成绩记录
     */
    public boolean deleteScore(String studentId, String scoreType) {
        try {
            return scoreDAO.deleteScore(studentId, scoreType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取学生姓名
     */
    public String getStudentNameById(String studentId) {
        try {
            DAO.StudentDAO studentDAO = new DAO.StudentDAO();
            Student student = studentDAO.getStudentById(studentId);
            return student != null ? student.getStudentName() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取成绩值
     */
    public BigDecimal getScoreByStudentIdAndType(String studentId, String scoreType) {
        try {
            return scoreDAO.getScoreByStudentIdAndType(studentId, scoreType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}