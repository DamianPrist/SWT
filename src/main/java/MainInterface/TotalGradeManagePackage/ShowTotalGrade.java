package MainInterface.TotalGradeManagePackage;

import Entity.Student;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 总成绩管理前端界面类
 */
public class ShowTotalGrade {

    private Composite parent;
    private Table gradeTable;
    private TotalGradeManage totalGradeManage;
    private Text searchText;

    // 分页相关变量
    private int currentPage = 1; // 当前页码
    private int pageSize = 10;   // 每页显示条数
    private int totalCount = 0;  // 总记录数
    private List<Student> allStudents = new ArrayList<>(); // 所有学生数据

    // 分页控件
    private Label pageInfoLabel;
    private Button prevPageButton;
    private Button nextPageButton;
    private Composite paginationComposite;

    public ShowTotalGrade(Composite parent) {
        this.parent = parent;
        this.totalGradeManage = new TotalGradeManage();
        createContent();
    }

    /**
     * 创建总成绩管理界面内容
     */
    private void createContent() {
        // 设置布局
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.verticalSpacing = 10;
        parent.setLayout(layout);

        // 创建搜索区域
        createSearchArea();

        // 创建表格区域 - 修改为填充整个区域
        createTableArea();

        // 创建分页控件区域
        createPaginationArea();

        // 创建按钮区域 - 只保留刷新按钮
        createButtonArea();

        // 加载学生成绩数据
        loadStudentGradeData();
    }

    /**
     * 创建搜索区域
     */
    private void createSearchArea() {
        Composite searchComposite = new Composite(parent, SWT.NONE);
        GridData searchData = new GridData(SWT.CENTER, SWT.TOP, true, false);
        searchData.widthHint = 500;
        searchComposite.setLayoutData(searchData);

        GridLayout searchLayout = new GridLayout(3, false);
        searchLayout.marginWidth = 0;
        searchLayout.marginHeight = 0;
        searchLayout.horizontalSpacing = 10;
        searchComposite.setLayout(searchLayout);

        // 搜索标签
        Label searchLabel = new Label(searchComposite, SWT.NONE);
        searchLabel.setText("搜索：");
        searchLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));

        // 搜索输入框
        searchText = new Text(searchComposite, SWT.BORDER | SWT.SEARCH);
        GridData textData = new GridData(250, SWT.DEFAULT);
        searchText.setLayoutData(textData);
        searchText.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));
        searchText.setMessage("请输入学号或姓名");

        // 搜索按钮
        Button searchButton = new Button(searchComposite, SWT.PUSH);
        searchButton.setText("搜索");
        searchButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.BOLD));
        searchButton.setBackground(new Color(parent.getDisplay(), 74, 144, 226));
        searchButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));

        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        buttonData.widthHint = 80;
        buttonData.heightHint = 35;
        searchButton.setLayoutData(buttonData);

        // 搜索按钮事件
        searchButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                performSearch();
            }
        });

        // 回车搜索
        searchText.addListener(SWT.DefaultSelection, e -> performSearch());
    }

    /**
     * 执行搜索
     */
    private void performSearch() {
        String keyword = searchText.getText().trim();

        if (keyword.isEmpty()) {
            allStudents = totalGradeManage.getAllStudentsOrderByTotalGrade();
        } else {
            allStudents = totalGradeManage.searchStudentsOrderByTotalGrade(keyword);
        }

        currentPage = 1; // 搜索后重置到第一页
        refreshTableWithPagination();
    }

    /**
     * 创建表格区域 - 修改为填充整个可用空间
     */
    private void createTableArea() {
        // 创建表格容器 - 设置填充数据
        Composite tableComposite = new Composite(parent, SWT.NONE);
        GridData tableCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableCompositeData.minimumHeight = 350; // 增加最小高度
        tableCompositeData.heightHint = 350; // 设置推荐高度
        tableComposite.setLayoutData(tableCompositeData);

        GridLayout tableLayout = new GridLayout(1, false);
        tableLayout.marginWidth = 0;
        tableLayout.marginHeight = 0;
        tableComposite.setLayout(tableLayout);

        // 创建表格 - 设置填充数据
        gradeTable = new Table(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableData.minimumHeight = 300;
        gradeTable.setLayoutData(tableData);
        gradeTable.setHeaderVisible(true);
        gradeTable.setLinesVisible(true);
        gradeTable.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        // 创建列：学号、姓名、班级、平时成绩、考试成绩、总成绩
        String[] columns = {"学号", "姓名", "班级", "平时成绩", "考试成绩", "总成绩"};
        // 修改列宽设置：让表格自动填充，使用动态调整
        int[] columnWidths = {150, 120, 200, 120, 120, 120};

        for (int i = 0; i < columns.length; i++) {
            TableColumn column = new TableColumn(gradeTable, SWT.CENTER);
            column.setText(columns[i]);
            if (i == columns.length - 1) {
                // 最后一列设置为可调整大小，自动填充剩余空间
                column.setResizable(true);
            } else {
                column.setWidth(columnWidths[i]);
            }
        }

        // 添加表格大小监听器，动态调整列宽
        gradeTable.addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                adjustTableColumns();
            }
        });
    }

    /**
     * 调整表格列宽，确保填满整个表格宽度
     */
    private void adjustTableColumns() {
        if (gradeTable.isDisposed()) {
            return;
        }

        // 获取表格客户区宽度
        int tableWidth = gradeTable.getClientArea().width;

        // 计算前5列的固定宽度
        int fixedWidth = 150 + 120 + 200 + 120 + 120; // 学号150 + 姓名120 + 班级200 + 平时成绩120 + 考试成绩120

        // 计算最后一列的宽度（剩余空间）
        int lastColumnWidth = tableWidth - fixedWidth;

        // 确保最后一列有最小宽度
        if (lastColumnWidth < 120) {
            lastColumnWidth = 120;
            // 如果表格太窄，需要重新分配宽度
            if (tableWidth < 700) {
                // 重新计算各列宽度，按比例分配
                int[] newWidths = new int[6];
                int totalColumns = 6;
                int availableWidth = tableWidth - 20; // 减去一些边距

                // 为每列分配宽度
                newWidths[0] = (int)(availableWidth * 0.20); // 学号20%
                newWidths[1] = (int)(availableWidth * 0.15); // 姓名15%
                newWidths[2] = (int)(availableWidth * 0.25); // 班级25%
                newWidths[3] = (int)(availableWidth * 0.15); // 平时成绩15%
                newWidths[4] = (int)(availableWidth * 0.15); // 考试成绩15%
                newWidths[5] = (int)(availableWidth * 0.10); // 总成绩10%

                // 设置列宽
                for (int i = 0; i < 6; i++) {
                    TableColumn column = gradeTable.getColumn(i);
                    if (column != null && !column.isDisposed()) {
                        column.setWidth(newWidths[i]);
                    }
                }
                return;
            }
        }

        // 设置最后一列宽度
        TableColumn lastColumn = gradeTable.getColumn(5);
        if (lastColumn != null && !lastColumn.isDisposed()) {
            lastColumn.setWidth(lastColumnWidth);
        }
    }

    /**
     * 创建分页控件区域
     */
    private void createPaginationArea() {
        paginationComposite = new Composite(parent, SWT.NONE);
        GridData paginationData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        paginationComposite.setLayoutData(paginationData);

        GridLayout paginationLayout = new GridLayout(5, false);
        paginationLayout.marginWidth = 0;
        paginationLayout.marginHeight = 5;
        paginationLayout.horizontalSpacing = 10;
        paginationComposite.setLayout(paginationLayout);

        // 上一页按钮
        prevPageButton = new Button(paginationComposite, SWT.PUSH);
        prevPageButton.setText("上一页");
        prevPageButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 10, SWT.NORMAL));
        prevPageButton.setEnabled(false);

        GridData prevData = new GridData(60, 25);
        prevPageButton.setLayoutData(prevData);

        // 页码信息
        pageInfoLabel = new Label(paginationComposite, SWT.CENTER);
        pageInfoLabel.setText("第 0 页 / 共 0 页 (共 0 条)");
        pageInfoLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 10, SWT.NORMAL));

        GridData labelData = new GridData(180, SWT.DEFAULT); // 增加宽度以显示更多信息
        pageInfoLabel.setLayoutData(labelData);

        // 下一页按钮
        nextPageButton = new Button(paginationComposite, SWT.PUSH);
        nextPageButton.setText("下一页");
        nextPageButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 10, SWT.NORMAL));
        nextPageButton.setEnabled(false);

        GridData nextData = new GridData(60, 25);
        nextPageButton.setLayoutData(nextData);

        // 每页显示条数选择
        Label pageSizeLabel = new Label(paginationComposite, SWT.NONE);
        pageSizeLabel.setText("每页显示：");
        pageSizeLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 10, SWT.NORMAL));

        Combo pageSizeCombo = new Combo(paginationComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        pageSizeCombo.setItems(new String[]{"10", "5", "7", "20"});
        pageSizeCombo.select(0); // 默认选择10条
        pageSizeCombo.setFont(new Font(parent.getDisplay(), "微软雅黑", 10, SWT.NORMAL));

        GridData comboData = new GridData(60, SWT.DEFAULT);
        pageSizeCombo.setLayoutData(comboData);

        // 按钮事件
        prevPageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    refreshTableWithPagination();
                }
            }
        });

        nextPageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int totalPages = (int) Math.ceil((double) totalCount / pageSize);
                if (currentPage < totalPages) {
                    currentPage++;
                    refreshTableWithPagination();
                }
            }
        });

        pageSizeCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                pageSize = Integer.parseInt(pageSizeCombo.getText());
                currentPage = 1; // 切换每页条数后回到第一页
                refreshTableWithPagination();
            }
        });
    }

    /**
     * 创建按钮区域 - 修改为只保留刷新按钮
     */
    private void createButtonArea() {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridData buttonData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
        buttonData.verticalIndent = 10;
        buttonComposite.setLayoutData(buttonData);

        GridLayout buttonLayout = new GridLayout(1, false);
        buttonLayout.marginWidth = 0;
        buttonLayout.marginHeight = 0;
        buttonComposite.setLayout(buttonLayout);

        // 刷新按钮
        Button refreshButton = new Button(buttonComposite, SWT.PUSH);
        refreshButton.setText("刷新数据");
        refreshButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.BOLD));
        refreshButton.setBackground(new Color(parent.getDisplay(), 33, 150, 243));
        refreshButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));

        GridData refreshButtonData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        refreshButtonData.widthHint = 120;
        refreshButtonData.heightHint = 40;
        refreshButton.setLayoutData(refreshButtonData);

        // 刷新按钮事件
        refreshButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadStudentGradeData();
            }
        });
    }

    /**
     * 加载学生成绩数据到表格
     */
    private void loadStudentGradeData() {
        // 重新从数据库加载数据，确保获取最新数据
        allStudents = totalGradeManage.getAllStudentsOrderByTotalGrade();
        refreshTableWithPagination();
    }

    /**
     * 带分页的刷新表格数据
     */
    private void refreshTableWithPagination() {
        if (allStudents == null) {
            allStudents = new ArrayList<>();
        }

        totalCount = allStudents.size();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 确保当前页在有效范围内
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        } else if (totalPages == 0) {
            currentPage = 0;
        }

        // 更新分页信息
        updatePaginationInfo(totalPages);

        // 获取当前页的数据
        List<Student> currentPageStudents = new ArrayList<>();
        if (currentPage > 0) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalCount);

            if (startIndex < totalCount) {
                currentPageStudents = allStudents.subList(startIndex, endIndex);
            }
        }

        // 刷新表格显示当前页数据
        refreshTable(currentPageStudents);
    }

    /**
     * 更新分页控件状态
     */
    private void updatePaginationInfo(int totalPages) {
        if (pageInfoLabel != null && !pageInfoLabel.isDisposed()) {
            if (totalCount == 0) {
                pageInfoLabel.setText("暂无数据");
            } else {
                pageInfoLabel.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页 (共 " + totalCount + " 条)");
            }
        }

        if (prevPageButton != null && !prevPageButton.isDisposed()) {
            prevPageButton.setEnabled(currentPage > 1);
        }

        if (nextPageButton != null && !nextPageButton.isDisposed()) {
            nextPageButton.setEnabled(currentPage < totalPages);
        }

        if (paginationComposite != null && !paginationComposite.isDisposed()) {
            paginationComposite.layout();
        }
    }

    /**
     * 刷新表格数据
     */
    private void refreshTable(List<Student> students) {
        // 检查控件是否有效
        if (gradeTable.isDisposed()) {
            return;
        }

        // 清空表格
        gradeTable.removeAll();

        // 添加数据行
        for (Student student : students) {
            TableItem item = new TableItem(gradeTable, SWT.NONE);
            item.setText(0, student.getStudentId());
            item.setText(1, student.getStudentName());
            item.setText(2, student.getClassName());

            // 显示平时成绩，如果没有则为空
            if (student.getUsualGrade() != null) {
                item.setText(3, String.format("%.1f", student.getUsualGrade()));
            } else {
                item.setText(3, "-");
            }

            // 显示考试成绩，如果没有则为空
            if (student.getExamGrade() != null) {
                item.setText(4, String.format("%.1f", student.getExamGrade()));
            } else {
                item.setText(4, "-");
            }

            // 显示总成绩，优先从数据库获取，如果没有则计算
            BigDecimal totalGrade = null;

            // 首先尝试从数据库获取已计算好的总成绩
            if (student.getTotalGrade() != null) {
                totalGrade = student.getTotalGrade();
            }
            // 如果数据库中没有总成绩，但平时成绩和考试成绩都有，则计算
            else if (student.getUsualGrade() != null && student.getExamGrade() != null) {
                // 计算总成绩：平时成绩*0.4 + 考试成绩*0.6
                totalGrade = student.getUsualGrade().multiply(new BigDecimal("0.4"))
                        .add(student.getExamGrade().multiply(new BigDecimal("0.6")));

                // 将计算后的总成绩更新到数据库（如果需要）
                // 注意：这里不直接更新数据库，由成绩管理界面更新
            }

            if (totalGrade != null) {
                item.setText(5, String.format("%.2f", totalGrade));
            } else {
                item.setText(5, "-");
            }
        }

        // 刷新表格并调整列宽
        gradeTable.layout();
        adjustTableColumns();
    }

    /**
     * 释放资源
     */
    public void dispose() {
        // 清理资源
    }
}