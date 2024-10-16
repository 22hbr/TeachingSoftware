import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainWindow extends JFrame {
    private JComboBox<String> architectureSelector;
    private JTextArea inputArea, outputArea, descriptionArea, codeArea;
    private JButton processButton, loadButton, saveButton;

    public MainWindow() {
        setTitle("经典软件体系结构教学软件");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();
        addListeners();
    }

    private void initComponents() {
        String[] architectures = {"主程序-子程序", "面向对象", "事件系统", "管道-过滤器"};
        architectureSelector = new JComboBox<>(architectures);

        inputArea = new JTextArea(10, 30);
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        descriptionArea = new JTextArea(10, 30);
        descriptionArea.setEditable(false);

        codeArea = new JTextArea(10, 30);
        codeArea.setEditable(false);

        processButton = new JButton("处理");
        loadButton = new JButton("加载文件");
        saveButton = new JButton("保存结果");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("选择架构风格:"));
        topPanel.add(architectureSelector);
        topPanel.add(processButton);
        topPanel.add(loadButton);
        topPanel.add(saveButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        centerPanel.add(new JScrollPane(inputArea));
        centerPanel.add(new JScrollPane(outputArea));
        centerPanel.add(new JScrollPane(descriptionArea));
        centerPanel.add(new JScrollPane(codeArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addListeners() {
        architectureSelector.addActionListener(e -> updateDescription());
        processButton.addActionListener(e -> processInput());
        loadButton.addActionListener(e -> loadFile());
        saveButton.addActionListener(e -> saveFile());
    }

    private void updateDescription() {
        String selectedArchitecture = (String) architectureSelector.getSelectedItem();
        switch (selectedArchitecture) {
            case "主程序-子程序":
                descriptionArea.setText("主程序-子程序风格:\n" +
                        "这种风格将程序分为一个主程序和多个子程序。主程序控制整体流程,子程序负责具体功能。\n" +
                        "优点: 结构清晰,易于理解和维护\n" +
                        "缺点: 灵活性较低,难以应对复杂的交互需求");
                codeArea.setText("public class MainSubProgram {\n" +
                        "    public static String process(String input) {\n" +
                        "        String[] lines = input.split(\"\\n\");\n" +
                        "        StringBuilder result = new StringBuilder();\n" +
                        "        for (String line : lines) {\n" +
                        "            result.append(processLine(line)).append(\"\\n\");\n" +
                        "        }\n" +
                        "        return result.toString().trim();\n" +
                        "    }\n" +
                        "    private static String processLine(String line) {\n" +
                        "        // KWIC处理逻辑\n" +
                        "    }\n" +
                        "}");
                break;
            case "面向对象":
                descriptionArea.setText("面向对象风格:\n" +
                        "这种风格将系统分解为多个对象,每个对象封装了数据和操作。对象之间通过消息传递进行交互。\n" +
                        "优点: 模块化程度高,易于扩展和重用\n" +
                        "缺点: 可能导致过度设计,影响性能");
                codeArea.setText("public class KWICSystem {\n" +
                        "    private List<String> lines;\n" +
                        "    public KWICSystem(String input) {\n" +
                        "        this.lines = Arrays.asList(input.split(\"\\n\"));\n" +
                        "    }\n" +
                        "    public String process() {\n" +
                        "        List<String> shifts = new ArrayList<>();\n" +
                        "        for (String line : lines) {\n" +
                        "            shifts.addAll(new LineShifter(line).getAllShifts());\n" +
                        "        }\n" +
                        "        Collections.sort(shifts);\n" +
                        "        return String.join(\"\\n\", shifts);\n" +
                        "    }\n" +
                        "}");
                break;
            case "事件系统":
                descriptionArea.setText("事件系统风格:\n" +
                        "这种风格基于事件的发布和订阅。组件之间通过事件进行松耦合的通信。\n" +
                        "优点: 高度解耦,易于扩展\n" +
                        "缺点: 控制流程不直观,调试困难");
                codeArea.setText("public class EventSystem {\n" +
                        "    private EventBus eventBus = new EventBus();\n" +
                        "    public EventSystem() {\n" +
                        "        eventBus.register(new InputHandler());\n" +
                        "        eventBus.register(new ShiftHandler());\n" +
                        "        eventBus.register(new SortHandler());\n" +
                        "        eventBus.register(new OutputHandler());\n" +
                        "    }\n" +
                        "    public String process(String input) {\n" +
                        "        eventBus.post(new InputEvent(input));\n" +
                        "        return outputHandler.getResult();\n" +
                        "    }\n" +
                        "}");
                break;
            case "管道-过滤器":
                descriptionArea.setText("管道-过滤器风格:\n" +
                        "这种风格将数据处理分解为一系列独立的过滤器,通过管道连接。每个过滤器独立工作,不依赖其他过滤器。\n" +
                        "优点: 组件复用性高,易于并行处理\n" +
                        "缺点: 可能存在数据转换开销");
                codeArea.setText("public class PipelineSystem {\n" +
                        "    public String process(String input) {\n" +
                        "        return new Pipeline<>()\n" +
                        "            .addFilter(new InputFilter())\n" +
                        "            .addFilter(new ShiftFilter())\n" +
                        "            .addFilter(new SortFilter())\n" +
                        "            .addFilter(new OutputFilter())\n" +
                        "            .process(input);\n" +
                        "    }\n" +
                        "}");
                break;
        }
    }

    private void processInput() {
        String selectedArchitecture = (String) architectureSelector.getSelectedItem();
        String input = inputArea.getText();
        String output;

        switch (selectedArchitecture) {
            case "主程序-子程序":
                output = MainSubProgram.process(input);
                break;
            case "面向对象":
                output = new ObjectOriented().process(input);
                break;
            case "事件系统":
                output = new EventSystem().process(input);
                break;
            case "管道-过滤器":
                output = new PipelineSystem().process(input);
                break;
            default:
                output = "未知架构风格";
        }

        outputArea.setText(output);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                inputArea.read(reader, null);
                reader.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "读取文件时出错: " + ex.getMessage());
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));
                outputArea.write(writer);
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存文件时出错: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}