package MainInterface.ScoreManagePackage;

import Entity.Student;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
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
 * 成绩管理前端界面类
 */
public class ShowScoreManage {

    private Composite parent;
    private Table scoreTable;
    private ScoreManage scoreManage;
    private Text searchText;
    private List<TableEditor> tableEditors;
    private Combo filterCombo; // 筛选下拉框

    // 分页相关变量
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalCount = 0;
    private List<Student> allScoreRecords = new ArrayList<>();

    // 分页控件
    private Label pageInfoLabel;
    private Button prevPageButton;
    private Button nextPageButton;
    private Composite paginationComposite;

    public ShowScoreManage(Composite parent) {
        this.parent = parent;
        this.scoreManage = new ScoreManage();
        this.tableEditors = new ArrayList<>();
        createContent();
    }

    /**
     * 创建成绩管理界面内容
     */
    private void createContent() {
        // 设置布局
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.verticalSpacing = 10;
        parent.setLayout(layout);

        // 创建搜索和筛选区域
        createSearchAndFilterArea();

        // 创建表格区域
        createTableArea();

        // 创建分页控件区域
        createPaginationArea();

        // 创建按钮区域
        createButtonArea();

        // 加载成绩数据
        loadScoreData();
    }

    /**
     * 创建搜索和筛选区域
     */
    private void createSearchAndFilterArea() {
        Composite searchFilterComposite = new Composite(parent, SWT.NONE);
        GridData searchFilterData = new GridData(SWT.CENTER, SWT.TOP, true, false);
        searchFilterData.widthHint = 650;
        searchFilterComposite.setLayoutData(searchFilterData);

        GridLayout searchFilterLayout = new GridLayout(4, false);
        searchFilterLayout.marginWidth = 0;
        searchFilterLayout.marginHeight = 0;
        searchFilterLayout.horizontalSpacing = 10;
        searchFilterComposite.setLayout(searchFilterLayout);

        // 搜索标签
        Label searchLabel = new Label(searchFilterComposite, SWT.NONE);
        searchLabel.setText("搜索：");
        searchLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));

        // 搜索输入框
        searchText = new Text(searchFilterComposite, SWT.BORDER | SWT.SEARCH);
        GridData textData = new GridData(200, SWT.DEFAULT);
        searchText.setLayoutData(textData);
        searchText.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));
        searchText.setMessage("请输入学号或姓名");

        // 筛选下拉框
        filterCombo = new Combo(searchFilterComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        filterCombo.setItems(new String[]{"全部成绩", "平时成绩", "考试成绩"});
        filterCombo.select(0);
        filterCombo.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.NORMAL));
        GridData filterData = new GridData(120, SWT.DEFAULT);
        filterCombo.setLayoutData(filterData);

        // 筛选事件
        filterCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String selected = filterCombo.getText();
                String filterType = "all";

                if ("平时成绩".equals(selected)) {
                    filterType = "usual";
                } else if ("考试成绩".equals(selected)) {
                    filterType = "exam";
                }

                scoreManage.setCurrentFilterType(filterType);
                currentPage = 1;
                loadScoreData();
            }
        });

        // 搜索按钮
        Button searchButton = new Button(searchFilterComposite, SWT.PUSH);
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
     * 执行搜索（带筛选）
     */
    private void performSearch() {
        String keyword = searchText.getText().trim();

        if (keyword.isEmpty()) {
            allScoreRecords = scoreManage.getAllScoreRecords();
        } else {
            allScoreRecords = scoreManage.searchScoreRecords(keyword);
        }

        currentPage = 1;
        refreshTableWithPagination();
    }

    /**
     * 创建表格区域
     */
    private void createTableArea() {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        GridData tableCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableCompositeData.minimumHeight = 300;
        tableComposite.setLayoutData(tableCompositeData);

        GridLayout tableLayout = new GridLayout(1, false);
        tableLayout.marginWidth = 0;
        tableLayout.marginHeight = 0;
        tableComposite.setLayout(tableLayout);

        // 创建表格
        scoreTable = new Table(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableData.minimumHeight = 250;
        scoreTable.setLayoutData(tableData);
        scoreTable.setHeaderVisible(true);
        scoreTable.setLinesVisible(true);
        scoreTable.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        // 创建列
        String[] columns = {"学号", "姓名", "考试类型", "成绩", "操作"};
        int[] columnWidths = {120, 100, 120, 100, SWT.DEFAULT};

        for (int i = 0; i < columns.length; i++) {
            TableColumn column = new TableColumn(scoreTable, SWT.CENTER);
            column.setText(columns[i]);
            if (i < columns.length - 1) {
                column.setWidth(columnWidths[i]);
            } else {
                column.setResizable(true);
            }
        }

        // 添加表格大小监听器
        scoreTable.addListener(SWT.Resize, event -> adjustTableColumns());
    }

    /**
     * 调整表格列宽
     */
    private void adjustTableColumns() {
        if (scoreTable.isDisposed()) {
            return;
        }

        int tableWidth = scoreTable.getClientArea().width;
        int fixedWidth = 120 + 100 + 120 + 100;
        int lastColumnWidth = tableWidth - fixedWidth;

        if (lastColumnWidth < 150) {
            lastColumnWidth = 150;
        }

        TableColumn lastColumn = scoreTable.getColumn(4);
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

        GridData labelData = new GridData(180, SWT.DEFAULT);
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
        pageSizeCombo.select(0);
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
                currentPage = 1;
                refreshTableWithPagination();
            }
        });
    }

    /**
     * 创建按钮区域
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

        // 添加成绩按钮
        Button addButton = new Button(buttonComposite, SWT.PUSH);
        addButton.setText("添加成绩记录");
        addButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.BOLD));
        addButton.setBackground(new Color(parent.getDisplay(), 102, 187, 106));
        addButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));

        GridData addButtonData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        addButtonData.widthHint = 150;
        addButtonData.heightHint = 40;
        addButton.setLayoutData(addButtonData);

        // 添加按钮事件
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showAddScoreDialog();
            }
        });
    }

    /**
     * 加载成绩数据
     */
    private void loadScoreData() {
        allScoreRecords = scoreManage.getAllScoreRecords();
        refreshTableWithPagination();
    }

    /**
     * 带分页的刷新表格数据
     */
    private void refreshTableWithPagination() {
        if (allScoreRecords == null) {
            allScoreRecords = new ArrayList<>();
        }

        totalCount = allScoreRecords.size();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        } else if (totalPages == 0) {
            currentPage = 0;
        }

        updatePaginationInfo(totalPages);

        List<Student> currentPageRecords = new ArrayList<>();
        if (currentPage > 0) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalCount);

            if (startIndex < totalCount) {
                currentPageRecords = allScoreRecords.subList(startIndex, endIndex);
            }
        }

        refreshTable(currentPageRecords);
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
    private void refreshTable(List<Student> scoreRecords) {
        cleanupTableEditors();
        scoreTable.removeAll();

        for (Student record : scoreRecords) {
            TableItem item = new TableItem(scoreTable, SWT.NONE);
            item.setText(0, record.getStudentId());
            item.setText(1, record.getStudentName());

            // 判断成绩类型
            String scoreType = record.getUsualGrade() != null ? "平时成绩" : "考试成绩";
            BigDecimal scoreValue = record.getUsualGrade() != null ? record.getUsualGrade() : record.getExamGrade();

            item.setText(2, scoreType);
            item.setText(3, scoreValue.toString());

            // 保存成绩类型到item数据中
            item.setData("scoreType", record.getUsualGrade() != null ? "usual" : "exam");
            item.setData("studentId", record.getStudentId());

            // 创建操作按钮容器
            Composite actionComposite = new Composite(scoreTable, SWT.NONE);
            GridLayout actionLayout = new GridLayout(2, true);
            actionLayout.marginWidth = 2;
            actionLayout.marginHeight = 2;
            actionLayout.horizontalSpacing = 5;
            actionComposite.setLayout(actionLayout);

            // 编辑按钮
            Button editButton = new Button(actionComposite, SWT.PUSH);
            editButton.setText("编辑");
            editButton.setData("studentId", record.getStudentId());
            editButton.setData("scoreType", record.getUsualGrade() != null ? "usual" : "exam");
            editButton.setBackground(new Color(parent.getDisplay(), 255, 193, 7));
            editButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));
            editButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 9, SWT.NORMAL));

            GridData editData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            editData.widthHint = 60;
            editButton.setLayoutData(editData);

            // 删除按钮
            Button deleteButton = new Button(actionComposite, SWT.PUSH);
            deleteButton.setText("删除");
            deleteButton.setData("studentId", record.getStudentId());
            deleteButton.setData("scoreType", record.getUsualGrade() != null ? "usual" : "exam");
            deleteButton.setBackground(new Color(parent.getDisplay(), 239, 83, 80));
            deleteButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));
            deleteButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 9, SWT.NORMAL));

            GridData deleteData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            deleteData.widthHint = 60;
            deleteButton.setLayoutData(deleteData);

            // 使用TableEditor嵌入按钮容器
            TableEditor editor = new TableEditor(scoreTable);
            editor.grabHorizontal = true;
            editor.grabVertical = true;
            editor.setEditor(actionComposite, item, 4);
            tableEditors.add(editor);

            // 按钮事件
            editButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (!editButton.isDisposed()) {
                        String studentId = (String) editButton.getData("studentId");
                        String scoreType = (String) editButton.getData("scoreType");
                        showEditScoreDialog(studentId, scoreType);
                    }
                }
            });

            deleteButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (!deleteButton.isDisposed()) {
                        String studentId = (String) deleteButton.getData("studentId");
                        String scoreType = (String) deleteButton.getData("scoreType");
                        showDeleteConfirmDialog(studentId, scoreType);
                    }
                }
            });
        }

        scoreTable.layout();
        adjustTableColumns();
    }

    /**
     * 清理表格编辑器
     */
    private void cleanupTableEditors() {
        List<TableEditor> editorsToRemove = new ArrayList<>();

        for (TableEditor editor : tableEditors) {
            if (editor != null) {
                try {
                    Control control = editor.getEditor();
                    if (control != null && !control.isDisposed()) {
                        control.dispose();
                    }
                    editor.dispose();
                } catch (Exception e) {
                    // 忽略异常
                }
            }
            editorsToRemove.add(editor);
        }

        tableEditors.removeAll(editorsToRemove);
    }

    /**
     * 显示添加成绩对话框
     */
    private void showAddScoreDialog() {
        if (parent.isDisposed() || parent.getShell() == null || parent.getShell().isDisposed()) {
            return;
        }

        Shell shell = parent.getShell();
        Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        dialog.setText("添加成绩记录");
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(2, false));

        // 居中显示
        dialog.setLocation(
                shell.getLocation().x + (shell.getSize().x - dialog.getSize().x) / 2,
                shell.getLocation().y + (shell.getSize().y - dialog.getSize().y) / 2
        );

        // 学号
        Label idLabel = new Label(dialog, SWT.NONE);
        idLabel.setText("学生学号：");
        idLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Text idText = new Text(dialog, SWT.BORDER);
        idText.setLayoutData(new GridData(200, SWT.DEFAULT));
        idText.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        // 成绩类型
        Label typeLabel = new Label(dialog, SWT.NONE);
        typeLabel.setText("成绩类型：");
        typeLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Combo typeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
        typeCombo.setItems(new String[]{"平时成绩", "考试成绩"});
        typeCombo.select(0);
        typeCombo.setLayoutData(new GridData(200, SWT.DEFAULT));
        typeCombo.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        // 成绩值
        Label scoreLabel = new Label(dialog, SWT.NONE);
        scoreLabel.setText("成绩值：");
        scoreLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Text scoreText = new Text(dialog, SWT.BORDER);
        scoreText.setLayoutData(new GridData(200, SWT.DEFAULT));
        scoreText.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));
        scoreText.setMessage("请输入0-100之间的数值");

        // 按钮区域
        Composite buttonComposite = new Composite(dialog, SWT.NONE);
        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        buttonData.horizontalSpan = 2;
        buttonComposite.setLayoutData(buttonData);
        buttonComposite.setLayout(new GridLayout(2, true));

        Button okButton = new Button(buttonComposite, SWT.PUSH);
        okButton.setText("确定");
        okButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.BOLD));
        okButton.setBackground(new Color(parent.getDisplay(), 102, 187, 106));
        okButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));
        okButton.setLayoutData(new GridData(80, 35));

        Button cancelButton = new Button(buttonComposite, SWT.PUSH);
        cancelButton.setText("取消");
        cancelButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));
        cancelButton.setLayoutData(new GridData(80, 35));

        // 按钮事件
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String studentId = idText.getText().trim();
                String scoreTypeText = typeCombo.getText();
                String scoreStr = scoreText.getText().trim();

                // 验证输入
                if (studentId.isEmpty() || scoreStr.isEmpty()) {
                    MessageBox warning = new MessageBox(dialog, SWT.ICON_WARNING);
                    warning.setText("输入不完整");
                    warning.setMessage("请填写所有必填字段！");
                    warning.open();
                    return;
                }

                BigDecimal score;
                try {
                    score = new BigDecimal(scoreStr);
                    if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    MessageBox warning = new MessageBox(dialog, SWT.ICON_WARNING);
                    warning.setText("输入错误");
                    warning.setMessage("请输入0-100之间的有效数值！");
                    warning.open();
                    return;
                }

                String scoreType = "平时成绩".equals(scoreTypeText) ? "usual" : "exam";

                // 调用添加方法
                boolean success = scoreManage.addScore(studentId, scoreType, score);

                if (success) {
                    MessageBox info = new MessageBox(dialog, SWT.ICON_INFORMATION);
                    info.setText("添加成功");
                    info.setMessage("成绩记录添加成功！");
                    info.open();
                    dialog.close();
                    loadScoreData();
                } else {
                    MessageBox error = new MessageBox(dialog, SWT.ICON_ERROR);
                    error.setText("添加失败");
                    error.setMessage("添加成绩失败，学生学号不存在！");
                    error.open();
                }
            }
        });

        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dialog.close();
            }
        });

        dialog.open();
    }

    /**
     * 显示编辑成绩对话框
     */
    private void showEditScoreDialog(String studentId, String scoreType) {
        if (parent.isDisposed() || parent.getShell() == null || parent.getShell().isDisposed()) {
            return;
        }

        BigDecimal currentScore = scoreManage.getScoreByStudentIdAndType(studentId, scoreType);
        if (currentScore == null) {
            MessageBox error = new MessageBox(parent.getShell(), SWT.ICON_ERROR);
            error.setText("错误");
            error.setMessage("未找到该成绩记录！");
            error.open();
            return;
        }

        String studentName = scoreManage.getStudentNameById(studentId);
        String scoreTypeText = "usual".equals(scoreType) ? "平时成绩" : "考试成绩";

        Shell shell = parent.getShell();
        Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        dialog.setText("编辑成绩记录");
        dialog.setSize(400, 320);
        dialog.setLayout(new GridLayout(2, false));

        // 居中显示
        dialog.setLocation(
                shell.getLocation().x + (shell.getSize().x - dialog.getSize().x) / 2,
                shell.getLocation().y + (shell.getSize().y - dialog.getSize().y) / 2
        );

        // 学号
        Label idLabel = new Label(dialog, SWT.NONE);
        idLabel.setText("学生学号：");
        idLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Label idDisplay = new Label(dialog, SWT.NONE);
        idDisplay.setText(studentId);
        idDisplay.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.BOLD));
        idDisplay.setLayoutData(new GridData(200, SWT.DEFAULT));

        // 姓名
        Label nameLabel = new Label(dialog, SWT.NONE);
        nameLabel.setText("学生姓名：");
        nameLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Label nameDisplay = new Label(dialog, SWT.NONE);
        nameDisplay.setText(studentName != null ? studentName : "未知");
        nameDisplay.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.BOLD));
        nameDisplay.setLayoutData(new GridData(200, SWT.DEFAULT));

        // 成绩类型
        Label typeLabel = new Label(dialog, SWT.NONE);
        typeLabel.setText("成绩类型：");
        typeLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Label typeDisplay = new Label(dialog, SWT.NONE);
        typeDisplay.setText(scoreTypeText);
        typeDisplay.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.BOLD));
        typeDisplay.setLayoutData(new GridData(200, SWT.DEFAULT));

        // 成绩值
        Label scoreLabel = new Label(dialog, SWT.NONE);
        scoreLabel.setText("新成绩值：");
        scoreLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));

        Text scoreText = new Text(dialog, SWT.BORDER);
        scoreText.setText(currentScore.toString());
        scoreText.setLayoutData(new GridData(200, SWT.DEFAULT));
        scoreText.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));
        scoreText.setMessage("请输入0-100之间的数值");

        // 按钮区域
        Composite buttonComposite = new Composite(dialog, SWT.NONE);
        GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        buttonData.horizontalSpan = 2;
        buttonComposite.setLayoutData(buttonData);
        buttonComposite.setLayout(new GridLayout(2, true));

        Button okButton = new Button(buttonComposite, SWT.PUSH);
        okButton.setText("确定");
        okButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.BOLD));
        okButton.setBackground(new Color(parent.getDisplay(), 255, 193, 7));
        okButton.setForeground(new Color(parent.getDisplay(), 255, 255, 255));
        okButton.setLayoutData(new GridData(80, 35));

        Button cancelButton = new Button(buttonComposite, SWT.PUSH);
        cancelButton.setText("取消");
        cancelButton.setFont(new Font(parent.getDisplay(), "微软雅黑", 11, SWT.NORMAL));
        cancelButton.setLayoutData(new GridData(80, 35));

        // 按钮事件
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String scoreStr = scoreText.getText().trim();

                // 验证输入
                if (scoreStr.isEmpty()) {
                    MessageBox warning = new MessageBox(dialog, SWT.ICON_WARNING);
                    warning.setText("输入不完整");
                    warning.setMessage("请填写成绩值！");
                    warning.open();
                    return;
                }

                BigDecimal score;
                try {
                    score = new BigDecimal(scoreStr);
                    if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    MessageBox warning = new MessageBox(dialog, SWT.ICON_WARNING);
                    warning.setText("输入错误");
                    warning.setMessage("请输入0-100之间的有效数值！");
                    warning.open();
                    return;
                }

                // 调用更新方法
                boolean success = scoreManage.editScore(studentId, scoreType, score);

                if (success) {
                    MessageBox info = new MessageBox(dialog, SWT.ICON_INFORMATION);
                    info.setText("更新成功");
                    info.setMessage("成绩记录更新成功！");
                    info.open();
                    dialog.close();
                    loadScoreData();
                } else {
                    MessageBox error = new MessageBox(dialog, SWT.ICON_ERROR);
                    error.setText("更新失败");
                    error.setMessage("更新成绩记录失败！");
                    error.open();
                }
            }
        });

        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dialog.close();
            }
        });

        dialog.open();
    }

    /**
     * 显示删除确认对话框
     */
    private void showDeleteConfirmDialog(String studentId, String scoreType) {
        if (parent.isDisposed() || parent.getShell() == null || parent.getShell().isDisposed()) {
            return;
        }

        String studentName = scoreManage.getStudentNameById(studentId);
        String scoreTypeText = "usual".equals(scoreType) ? "平时成绩" : "考试成绩";

        MessageBox confirmDialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        confirmDialog.setText("确认删除");
        confirmDialog.setMessage("确定要删除 " + (studentName != null ? studentName : studentId) +
                " 的" + scoreTypeText + "记录吗？");

        int result = confirmDialog.open();
        if (result == SWT.YES) {
            boolean success = scoreManage.deleteScore(studentId, scoreType);
            if (success) {
                MessageBox successDialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION);
                successDialog.setText("删除成功");
                successDialog.setMessage("成绩记录删除成功！");
                successDialog.open();
                loadScoreData();
            } else {
                MessageBox errorDialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR);
                errorDialog.setText("删除失败");
                errorDialog.setMessage("删除成绩记录失败！");
                errorDialog.open();
            }
        }
    }

    /**
     * 释放资源
     */
    public void dispose() {
        cleanupTableEditors();
    }
}