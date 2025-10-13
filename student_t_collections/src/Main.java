import java.io.*;
import java.util.*;

class Student implements Comparable<Student> {
    private long num;
    private String name;
    private int group;
    private double grade;

    public Student() {}
    public Student(long num, String name, int gr, double grade) {
        this.num = num;
        this.name = name;
        this.group = gr;
        this.grade = grade;
    }
    public Student(Student a) {
        this.num = a.num;
        this.name = a.name;
        this.group = a.group;
        this.grade = a.grade;
    }

    @Override
    public int compareTo(Student other) {
        return Long.compare(this.num, other.num);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student s = (Student) obj;
        return num == s.num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }

    @Override
    public String toString() {
        return num + " " + name + " " + group + " " + grade;
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("input.txt"));
            sc.useLocale(Locale.US);
            PrintWriter out = new PrintWriter("output.txt");

            int n = sc.nextInt();
            Set<Student> set1 = new TreeSet<>();
            for (int i = 0; i < n; i++) {
                long num = sc.nextLong();
                String name = sc.next();
                int group = sc.nextInt();
                double grade = sc.nextDouble();
                set1.add(new Student(num, name, group, grade));
            }

            int m = sc.nextInt();
            Set<Student> set2 = new TreeSet<>();
            for (int i = 0; i < m; i++) {
                long num = sc.nextLong();
                String name = sc.next();
                int group = sc.nextInt();
                double grade = sc.nextDouble();
                set2.add(new Student(num, name, group, grade));
            }
            Set<Student> union = new TreeSet<>(set1);
            union.addAll(set2);
            Set<Student> intersection = new TreeSet<>(set1);
            intersection.retainAll(set2);
            Set<Student> difference = new TreeSet<>(set1);
            difference.removeAll(set2);

            out.println("Список студентов 1:");
            for (Student s : set1) out.println(s);
            out.println("\nСписок студентов 2:");
            for (Student s : set2) out.println(s);

            out.println("\nОбъединение:");
            for (Student s : union) out.println(s);

            out.println("\nПересечение:");
            for (Student s : intersection) out.println(s);

            out.println("\nРазность (X - Y):");
            for (Student s : difference) out.println(s);

            sc.close();
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }
    }
}
