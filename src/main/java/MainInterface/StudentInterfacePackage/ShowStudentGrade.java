package MainInterface.StudentInterfacePackage;

import Entity.Student;
import DAO.StudentDAO;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.math.BigDecimal;

/**
 * 学生成绩查看界面
 */
public class ShowStudentGrade {

    private Composite parent;
    private Student currentStudent;
    private StudentDAO studentDAO;

    public ShowStudentGrade(Composite parent, Student student) {
        this.parent = parent;
        this.currentStudent = student;
        this.studentDAO = new StudentDAO();
        createContent();
    }

    /**
     * 创建学生成绩界面内容
     */
    private void createContent() {
        // 设置布局
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 20;
        layout.marginHeight = 20;
        layout.verticalSpacing = 20;
        parent.setLayout(layout);

        // 创建学生信息卡片
        createStudentInfoCard();

        // 创建成绩信息卡片
        createGradeCard();

        // 创建总成绩卡片
        createTotalGradeCard();
    }

    /**
     * 创建学生信息卡片 - 修改布局解决班级显示不完整问题
     */
    private void createStudentInfoCard() {
        Composite infoComposite = new Composite(parent, SWT.NONE);
        infoComposite.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        GridData infoData = new GridData(SWT.FILL, SWT.TOP, true, false);
        infoData.heightHint = 150; // 增加高度以容纳班级信息
        infoComposite.setLayoutData(infoData);

        // 使用网格布局，4列
        GridLayout infoLayout = new GridLayout(4, false);
        infoLayout.marginWidth = 30;
        infoLayout.marginHeight = 20;
        infoLayout.horizontalSpacing = 10;
        infoLayout.verticalSpacing = 15;
        infoComposite.setLayout(infoLayout);

        // 学号信息
        createInfoItem(infoComposite, "学号：", currentStudent.getStudentId(), 120);

        // 姓名信息
        createInfoItem(infoComposite, "姓名：", currentStudent.getStudentName(), 120);

        // 班级信息 - 使用更宽的布局
        Label classLabel = new Label(infoComposite, SWT.NONE);
        classLabel.setText("班级：");
        classLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 13, SWT.BOLD));
        classLabel.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

        Label classValue = new Label(infoComposite, SWT.NONE);
        classValue.setText(currentStudent.getClassName() != null ? currentStudent.getClassName() : "未设置");
        classValue.setFont(new Font(parent.getDisplay(), "微软雅黑", 13, SWT.NORMAL));
        classValue.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        GridData classData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        classData.widthHint = 250; // 增加班级显示的宽度
        classValue.setLayoutData(classData);

        // 性别信息
        createInfoItem(infoComposite, "性别：", currentStudent.getGender(), 80);
    }

    /**
     * 创建信息项辅助方法
     */
    private void createInfoItem(Composite parent, String labelText, String value, int widthHint) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labelText);
        label.setFont(new Font(parent.getDisplay(), "微软雅黑", 13, SWT.BOLD));
        label.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

        Label valueLabel = new Label(parent, SWT.NONE);
        valueLabel.setText(value != null ? value : "未设置");
        valueLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 13, SWT.NORMAL));
        valueLabel.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        data.widthHint = widthHint;
        valueLabel.setLayoutData(data);
    }

    /**
     * 创建成绩信息卡片 - 简化，只显示总成绩
     */
    private void createGradeCard() {
        Composite gradeComposite = new Composite(parent, SWT.NONE);
        gradeComposite.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        GridData gradeData = new GridData(SWT.FILL, SWT.TOP, true, false);
        gradeData.heightHint = 250;
        gradeComposite.setLayoutData(gradeData);

        // 单列布局，只显示总成绩
        GridLayout gradeLayout = new GridLayout(1, false);
        gradeLayout.marginWidth = 20;
        gradeLayout.marginHeight = 20;
        gradeComposite.setLayout(gradeLayout);

        // 获取最新的学生成绩信息
        Student studentWithGrade = studentDAO.getStudentGradeById(currentStudent.getStudentId());
        if (studentWithGrade == null) {
            studentWithGrade = currentStudent;
        }

        // 总成绩标题
        Label totalTitle = new Label(gradeComposite, SWT.CENTER);
        totalTitle.setText("我的总成绩");
        totalTitle.setFont(new Font(parent.getDisplay(), "微软雅黑", 20, SWT.BOLD));
        totalTitle.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        totalTitle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        // 计算总成绩
        BigDecimal totalGrade = calculateTotalGrade(studentWithGrade);

        // 总成绩值
        Label totalValue = new Label(gradeComposite, SWT.CENTER);
        if (totalGrade != null) {
            totalValue.setText(String.format("%.2f", totalGrade));
        } else {
            totalValue.setText("暂无成绩");
        }
        totalValue.setFont(new Font(parent.getDisplay(), "微软雅黑", 48, SWT.BOLD));

        // 根据成绩设置颜色
        Color gradeColor;
        if (totalGrade == null) {
            gradeColor = new Color(parent.getDisplay(), 128, 128, 128); // 灰色
        } else if (totalGrade.compareTo(new BigDecimal("90")) >= 0) {
            gradeColor = new Color(parent.getDisplay(), 76, 175, 80); // 绿色，优秀
        } else if (totalGrade.compareTo(new BigDecimal("80")) >= 0) {
            gradeColor = new Color(parent.getDisplay(), 255, 193, 7); // 黄色，良好
        } else if (totalGrade.compareTo(new BigDecimal("60")) >= 0) {
            gradeColor = new Color(parent.getDisplay(), 255, 152, 0); // 橙色，及格
        } else {
            gradeColor = new Color(parent.getDisplay(), 244, 67, 54); // 红色，不及格
        }
        totalValue.setForeground(gradeColor);
        totalValue.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        totalValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

        // 成绩评价
        Label evaluation = new Label(gradeComposite, SWT.CENTER);
        evaluation.setFont(new Font(parent.getDisplay(), "微软雅黑", 14, SWT.BOLD));
        evaluation.setForeground(gradeColor);
        evaluation.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

        String evaluationText;
        if (totalGrade == null) {
            evaluationText = "成绩尚未录入";
        } else if (totalGrade.compareTo(new BigDecimal("90")) >= 0) {
            evaluationText = "优秀！继续保持！";
        } else if (totalGrade.compareTo(new BigDecimal("80")) >= 0) {
            evaluationText = "良好，还有提升空间！";
        } else if (totalGrade.compareTo(new BigDecimal("60")) >= 0) {
            evaluationText = "及格，需要更加努力！";
        } else {
            evaluationText = "不及格，请认真复习！";
        }
        evaluation.setText(evaluationText);
        evaluation.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
    }

    /**
     * 计算总成绩
     */
    private BigDecimal calculateTotalGrade(Student student) {
        // 优先从数据库获取已计算好的总成绩
        if (student.getTotalGrade() != null) {
            return student.getTotalGrade();
        }
        // 如果数据库中没有总成绩，但平时成绩和考试成绩都有，则计算
        else if (student.getUsualGrade() != null && student.getExamGrade() != null) {
            // 计算总成绩：平时成绩*0.4 + 考试成绩*0.6
            return student.getUsualGrade().multiply(new BigDecimal("0.4"))
                    .add(student.getExamGrade().multiply(new BigDecimal("0.6")));
        }
        return null;
    }

    /**
     * 创建总成绩卡片 - 简化版，只显示计算说明
     */
    private void createTotalGradeCard() {
        Composite totalComposite = new Composite(parent, SWT.NONE);
        totalComposite.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        GridData totalData = new GridData(SWT.FILL, SWT.TOP, true, false);
        totalData.heightHint = 80;
        totalComposite.setLayoutData(totalData);

        GridLayout totalLayout = new GridLayout(1, false);
        totalLayout.marginWidth = 20;
        totalLayout.marginHeight = 10;
        totalComposite.setLayout(totalLayout);

        // 计算说明
        Label calcDesc = new Label(totalComposite, SWT.CENTER);
        calcDesc.setText("注：总成绩 = 平时成绩 × 0.4 + 考试成绩 × 0.6");
        calcDesc.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));
        calcDesc.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
        calcDesc.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    }
}