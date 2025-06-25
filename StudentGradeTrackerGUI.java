import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public class StudentGradeTrackerGUI extends JFrame {
    private JTextField nameField;
    private JTextField gradeField;
    private JTextField searchField;
    private JTextArea outputArea;
    private StudentManager manager = new StudentManager();

    public StudentGradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setSize(650, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        manager.loadFromFile();

        // Fonts
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // Top Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        nameField = createTextField(font);
        gradeField = createTextField(font);
        searchField = createTextField(font);

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("Search Name:"));
        inputPanel.add(searchField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        JButton addBtn = createRoundedButton("Add");
        JButton summaryBtn = createRoundedButton("Summary");
        JButton sortNameBtn = createRoundedButton("Sort by Name");
        JButton sortGradeBtn = createRoundedButton("Sort by Grade");
        JButton refreshBtn = createRoundedButton("Refresh All");
        JLabel placeholder = new JLabel(); // empty label to fill grid

        buttonPanel.add(addBtn);
        buttonPanel.add(summaryBtn);
        buttonPanel.add(sortNameBtn);
        buttonPanel.add(sortGradeBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(placeholder);

        // Output Area
        outputArea = new JTextArea(15, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBorder(new LineBorder(Color.GRAY));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        refreshOutput();

        // Enter key actions
        nameField.addActionListener(e -> gradeField.requestFocus());
        gradeField.addActionListener(e -> addBtn.doClick());
        searchField.addActionListener(e -> {
            String name = searchField.getText();
            Student s = manager.searchByName(name);
            outputArea.append("\n--- Search Result ---\n");
            if (s != null) {
                outputArea.append(s.toString() + "\n");
            } else {
                outputArea.append("Student not found.\n");
            }
        });

        // Button actions
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String gradeText = gradeField.getText().trim();
            try {
                int grade = Integer.parseInt(gradeText);
                if (grade < 0 || grade > 100) {
                    JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.");
                    return;
                }
                manager.addStudent(new Student(name, grade));
                nameField.setText("");
                gradeField.setText("");
                refreshOutput();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid grade!");
            }
        });

        summaryBtn.addActionListener(e -> {
            outputArea.append("\n--- Summary ---\n");
            outputArea.append("Average Grade: " + manager.getAverage() + "\n");
            outputArea.append("Highest Grade: " + manager.getHighest() + "\n");
            outputArea.append("Lowest Grade : " + manager.getLowest() + "\n");
        });

        sortNameBtn.addActionListener(e -> {
            manager.sortByName();
            refreshOutput();
        });

        sortGradeBtn.addActionListener(e -> {
            manager.sortByGrade();
            refreshOutput();
        });

        refreshBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all records?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.getStudents().clear();
                manager.saveToFile();
                nameField.setText("");
                gradeField.setText("");
                searchField.setText("");
                refreshOutput();
            }
        });

        setVisible(true);
    }

    private JTextField createTextField(Font font) {
        JTextField tf = new JTextField();
        tf.setFont(font);
        tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return tf;
    }

    private JButton createRoundedButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(200, 221, 242));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        return btn;
    }

    private void refreshOutput() {
        outputArea.setText("--- Student List ---\n");
        Iterator<Student> iterator = manager.getStudents().iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            outputArea.append(s.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeTrackerGUI::new);
    }
}
