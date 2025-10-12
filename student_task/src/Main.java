import java.io.*;
import java.util.*;

class Student implements Comparable<Student>
{private long num;//номер зачетки
 private String name;//имя студента
 private int group;
 private double grade;


 public Student() {}
 public Student(long num,String name, int gr,double grade )
    {
        this.num = num;
        this.name = name;
        this.group = gr;
        this.grade = grade;
    }
    public Student(Student a)
    {
        this.num = a.num;
        this.name = a.name;
        this.group = a.group;
        this.grade = a.grade;
    }
    @Override
    public int compareTo(Student other) {
        return Long.compare(this.num, other.num); // сравнение по номеру зачетки
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return num == student.num;
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
    static ArrayList<Student> sub (ArrayList<Student> x, ArrayList<Student> y) //разность x-y
    {
        Collections.sort(x);
        Collections.sort(y);
        int i=0, k=0, j=0;
        int n= x.size();
        int m= y.size();
        ArrayList<Student> z= new ArrayList<>();//размер разности может быть максимально размером первого множества
        while (i < n && j < m) {
            int comparison = x.get(i).compareTo(y.get(j));
            if (comparison < 0) {
                z.add(x.get(i));
                i++;
            } else if (comparison > 0) {
                j++;
            } else {
                i++;
                j++;
            }
        }
        while (i < x.size()) {
            z.add(x.get(i));
            i++;
        }
        return z;
    }
    static ArrayList<Student> Sliyanie ( ArrayList<Student> x, ArrayList<Student> y)//объединение
    {
        Collections.sort(x);
        Collections.sort(y);

        ArrayList<Student> z = new ArrayList<>();
        int i=0, j=0;
        int n= x.size();
        int m= y.size();
        while (i < n && j < m) {
            int comparison = x.get(i).compareTo(y.get(j));
            if (comparison < 0) {
                z.add(x.get(i));
                i++;
            } else if (comparison > 0) {
                z.add(y.get(i));
                j++;
            } else {
                z.add(x.get(i));
                i++;
                j++;
            }
        }
        while (i < x.size()) {
            z.add(x.get(i));
            i++;
        }

        while (j < y.size()) {
            z.add(y.get(j));
            j++;
        }
        return z;
    }
    static ArrayList<Student> Peresechenie (ArrayList<Student> x, ArrayList<Student> y)//пересечение
    {
        Collections.sort(x);
        Collections.sort(y);

        ArrayList<Student> z = new ArrayList<>();
        int i=0, j=0;
        int n= x.size();
        int m= y.size();
        while (i < n && j < m) {
            int comparison = x.get(i).compareTo(y.get(j));
            if (comparison < 0) {
                i++;
            } else if (comparison > 0) {
                j++;
            } else {
                z.add(y.get(i));
                i++;
            }
        }
        return z;
    }
    public static void main(String[] args)
    { try{Scanner sc = new Scanner(new File("input.txt"));
        sc.useLocale(Locale.US);
        PrintWriter out = new PrintWriter("output.txt");
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            long num = sc.nextLong();
            String name = sc.next();
            int group = sc.nextInt();
            double grade = sc.nextDouble();
            list1.add(new Student(num, name, group, grade));
        }

        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            long num = sc.nextLong();
            String name = sc.next();
            int group = sc.nextInt();
            double grade = sc.nextDouble();
            list2.add(new Student(num, name, group, grade));
        }

        ArrayList<Student> st_sliyanie = Sliyanie(list1, list2);
        ArrayList<Student> st_peresech = Peresechenie(list1, list2);
        ArrayList<Student> st_sub = sub(list1, list2);

        out.println("Объединение:");
        for (Student s : st_sliyanie) out.println(s);

        out.println("\nПересечение:");
        for (Student s : st_peresech) out.println(s);

        out.println("\nРазность (X - Y):");
        for (Student s : st_sub) out.println(s);

        sc.close();
        out.close();
    }
    catch (FileNotFoundException e) {
        System.out.println("Файл не найден!");
    }
}}
