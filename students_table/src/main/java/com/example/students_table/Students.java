package com.example.students_table;

import java.util.Objects;
import java.io.*;
import java.util.*;


public class Students implements Comparable<Students> {
    private long num;
    private String name;
    private int group;
    private double grade;

    public Students() {}
    public Students(long num, String name, int gr, double grade) {
        this.num = num;
        this.name = name;
        this.group = gr;
        this.grade = grade;
    }
    public Students(Students a) {
        this.num = a.num;
        this.name = a.name;
        this.group = a.group;
        this.grade = a.grade;
    }

    public long getNum() { return num; }
    public String getName() { return name; }
    public int getGroup() { return group; }
    public double getGrade() { return grade; }


    @Override
    public int compareTo(Students other) {
        return Long.compare(this.num, other.num);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Students s = (Students) obj;
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

    public static class IdComparator implements Comparator<Students>
    {
        @Override
        public int compare(Students s1, Students s2)
        {
            return Long.compare(s1.getNum(), s2.getNum());
        }
    }


    public static class NameComparator implements Comparator<Students>
    {
        @Override
        public int compare(Students s1, Students s2)
        {
            return s1.getName().compareTo(s2.getName());
        }
    }


    public static class GroupNameComparator implements Comparator<Students> {
        @Override
        public int compare(Students s1, Students s2)
        {
            int groupCompare = Integer.compare(s1.getGroup(), s2.getGroup());
            if (groupCompare != 0) return groupCompare;
            return s1.getName().compareTo(s2.getName());
        }
    }


    public static class GroupGradeComparator implements Comparator<Students> {
        @Override
        public int compare(Students s1, Students s2)
        {
            int groupCompare = Integer.compare(s1.getGroup(), s2.getGroup());
            if (groupCompare != 0) return groupCompare;
            // внутри группы по убыванию балла
            return Double.compare(s2.getGrade(), s1.getGrade());
        }
    }

    public static class GradeAscComparator implements Comparator<Students>
    {
        @Override
        public int compare(Students s1, Students s2)
        {
            return Double.compare(s1.getGrade(), s2.getGrade());
        }
    }

    public static class GradeDecComparator implements Comparator<Students>
    {
        @Override
        public int compare(Students s1, Students s2)
        {
            return Double.compare(s2.getGrade(), s1.getGrade());
        }
    }

    public static List<Students> loadF(String filename) {
        List<Students> list = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(filename), "UTF-8")) {
            sc.useLocale(Locale.US);
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                long num = sc.nextLong();
                String name = sc.next();
                int group = sc.nextInt();
                double grade = sc.nextDouble();
                list.add(new Students(num, name, group, grade));
            }
        } catch (Exception e) {
            //System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }

        return list;
    }


}


