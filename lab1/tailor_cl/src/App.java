import java.util.Scanner;
//import java.io.*;
import java.text.*;
class Tailor
{
    int k;//точность
    double x;//аргумент
    Tailor(int k1, double arg)
    {
        k=k1;
        x=arg;
    }
    Tailor Change(int k1, double x1)
    {
        k=k1;
        x=x1;
        return this;
    }
    void Sum_cos()
    {   double x_save=x;
        final double two_PI = 2 * Math.PI;
        //угол к диапазону [0, 2π]
        x= x % two_PI;
        // если угол отрицательный, добавляем 2π
        if (x < 0) {
            x += two_PI;
        }
        if (x > Math.PI) {
            x -= two_PI;
        }
        double sum=1, el=1;
        int n=0;
        do {
            n+=2;
            el*=(-1)*x*x/(n*(n-1));
            sum+=el;
        } while (Math.abs(el)>Math.pow(10, -k));
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(k);
        System.out.println("Значение косинуса:"+formatter.format(sum));
        System.out.println("Значение ряда Тейлора:"+formatter.format(Math.cos(x_save)));
    }
    void Sum_exp()
    {
        double eps = Math.pow(10, -k);
        double term = 1; // текущий член ряда
        double result = 1; // накопленная сумма
        int i = 1; // счетчик факториала
        // Для отрицательных x используем свойство: e^(-x) = 1/e^x
        boolean isNegative = x < 0;
        double absX = Math.abs(x);
        if (isNegative) {
            while (Math.abs(term) >= eps) {
                term = term * absX / i;
                result += term;
                i++;
            }
            result = 1.0 / result; // e^(-x) = 1/e^x
        } else {
            // для положительных x
            while (Math.abs(term) >= eps) {
                term = term * x / i;
                result += term;
                i++;
            }
        }

        NumberFormat formatter1 = NumberFormat.getNumberInstance();
        formatter1.setMaximumFractionDigits(k);
        System.out.println("Значение экспоненты: " + formatter1.format(Math.exp(x)));
        System.out.println("Значение ряда Тейлора: " + formatter1.format(result));
    }
}

    public class  App {
        public static void main(String[] args) {
            System.out.print("Введите k для ограничения косинуса:");
            Scanner objIn = new Scanner(System.in);
            int k_ = objIn.nextInt();
            System.out.print("Введите x (фргумент для косинуса):");
            double x_ = objIn.nextDouble();
            Tailor A = new Tailor(k_,x_);
            System.out.print("Введите k для ограничения экспоненты:");
            int k1_ = objIn.nextInt();
            System.out.print("Введите x (аргумент для экспоненты):");
            double x1_ = objIn.nextDouble();
            Tailor B = new Tailor(k1_,x1_);
            A.Sum_cos();
            B.Sum_exp();
            objIn.close();
        }
    }
