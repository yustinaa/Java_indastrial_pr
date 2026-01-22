package org.example;

import java.util.*;

class Employee {
    String name;
    String department;
    double salary;
    String hireDate;

    public Employee(String name, String department, double salary, String hireDate)
    {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    @Override
    public String toString()
    {
        return name + ", " + department +", "+salary + ", "+ hireDate;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Jane", "IT", 2000, "2015-05-10"));
        employees.add(new Employee("Andrew", "Finance", 3200, "2010-04-01"));
        employees.add(new Employee("Mark", "IT", 5000, "2018-03-20"));
        employees.add(new Employee("Ann", "HR", 2500, "2020-03-20"));
        employees.add(new Employee("Ann", "IT", 3000, "2021-03-20"));
        employees.add(new Employee("Tom", "HR", 800, "2018-06-20"));
        employees.add(new Employee("Paul", "Finance", 1200, "2018-03-20"));
        employees.add(new Employee("Tom", "Finance", 3200, "2018-03-20"));
        employees.add(new Employee("Elis", "IT", 3000, "2017-03-27"));
        employees.add(new Employee("Mathew", "HR", 2000, "2018-09-28"));
        employees.add(new Employee("Sam", "HR", 1000, "2011-03-25"));


        System.out.println("Сотрудник с макс. зарплатой");
        System.out.println(findMaxSalary(employees));

        System.out.println("Сотрудники с зарплатой выше средней");
        for (Employee e : getAboveAverage(employees)) {
            System.out.println(e);
        }

        System.out.println("3 самых старых сотрудника");
        for (Employee e : getOldestEmployees(employees, 3)) {
            System.out.println(e);
        }

        System.out.println("Суммарная зарплата по отделу IT");
        System.out.println(getTotalSalaryFromD(employees, "IT"));
        System.out.println("Суммарная зарплата по отделу HR");
        System.out.println(getTotalSalaryFromD(employees, "HR"));
        System.out.println("Суммарная зарплата по отделу Finance");
        System.out.println(getTotalSalaryFromD(employees, "Finance"));



        Map<String, List<Employee>> departmentsMap = groupByDepartment(employees);
        System.out.println("Сотрудники по отделам:");
        for (String dept : departmentsMap.keySet()) {
            System.out.println("Отдел: " + dept);

            List<Employee> deptList = departmentsMap.get(dept); //список людей из нужного отдела
            for (Employee e : deptList) {
                System.out.println(e);
            }
        }
    }


    public static Employee findMaxSalary(List<Employee> list)
    {
        if (list.isEmpty())
        {return null;}
        Employee top = list.get(0);
        for (Employee e : list)
        {
            if (e.salary > top.salary) {
                top = e;
            }
        }
        return top;
    }

    public static List<Employee> getAboveAverage(List<Employee> list)
    {
        double sum = 0;
        for (Employee e : list)
        {sum += e.salary;}
        double avg = sum / list.size();
        List<Employee> result = new ArrayList<>();
        for (Employee e : list)
        {
            if (e.salary > avg)
            {result.add(e);}
        }
        return result;
    }

    public static double getTotalSalaryFromD(List<Employee> list, String d) {
        double total = 0;
        for (Employee e : list)
        {
            if (e.department.equalsIgnoreCase(d))
            {
                total += e.salary;
            }
        }
        return total;
    }

    public static List<Employee> getOldestEmployees(List<Employee> list, int count) {
        List<Employee> sortedList = new ArrayList<>(list);

        sortedList.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return e1.hireDate.compareTo(e2.hireDate);
            }
        });

        List<Employee> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, sortedList.size()); i++) {
            result.add(sortedList.get(i));
        }
        return result;
    }

    public static Map<String, List<Employee>> groupByDepartment(List<Employee> list) {
        Map<String, List<Employee>> result = new HashMap<>();

        for (Employee e : list)
        {
            String dept = e.department;

            if (!result.containsKey(dept)) //если отдела раньше не было
            {
                result.put(dept, new ArrayList<>());
            }

            result.get(dept).add(e);
        }
        return result;
    }
}