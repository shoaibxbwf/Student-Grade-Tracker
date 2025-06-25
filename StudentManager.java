import java.io.*;
import java.util.*;

public class StudentManager {
    private ArrayList<Student> students = new ArrayList<>();
    private final String filename = "students.txt";

    public void addStudent(Student s) {
        students.add(s);
        saveToFile();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Student s : students) {
                pw.println(s.getName() + "," + s.getGrade());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                students.add(new Student(parts[0], Integer.parseInt(parts[1])));
            }
        } catch (IOException e) {
            // File might not exist, ignore
        }
    }

    public double getAverage() {
        if (students.isEmpty()) return 0;
        int sum = 0;
        for (Student s : students) sum += s.getGrade();
        return (double) sum / students.size();
    }

    public int getHighest() {
        return students.stream().mapToInt(Student::getGrade).max().orElse(0);
    }

    public int getLowest() {
        return students.stream().mapToInt(Student::getGrade).min().orElse(0);
    }

    public void sortByName() {
        students.sort(Comparator.comparing(Student::getName));
    }

    public void sortByGrade() {
        students.sort(Comparator.comparingInt(Student::getGrade).reversed());
    }

    public Student searchByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    // ✅ This method is new
    public void clearAll() {
        students.clear();
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print(""); // Clear file content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
