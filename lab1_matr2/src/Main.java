import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        try {
            Scanner fileIn = new Scanner(new File("matrix.txt"));
            PrintWriter fileOut = new PrintWriter("output.txt");
            myMatr A = myMatr.readMatr(fileIn);
            fileOut.println("Исходная матрица:");
            A.writeMatr(fileOut);
            A.sort_Matr();
            fileOut.println("Отсортированная матрица:");
            A.writeMatr(fileOut);
            int sum;
            sum= A.sum_min(A);
            fileOut.println("Минимальная сумма модулей диагоналей:"+ sum);
            myMatr B=A.matr_ot(A);
            fileOut.println("Отображенная относительно побочной диагонали матрица:");
            B.writeMatr(fileOut);
            fileIn.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }
    }}
class myMatr{
    int n;
    int m;
    int [][] Matrix;
    myMatr() {
        Matrix = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Matrix[i][j] = 0;
            }}
    }
    myMatr(int n1, int m1) {
        n=n1;
        m=m1;
        Matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Matrix[i][j] = 0;
            }}
    }
    void sort_Matr()
    {
        int [] Mas_sum=new int [n];
        for(int i=0; i<n; i++)
        {for(int j=0; j<m; j++)
        {Mas_sum[i]+=Matrix[i][j];}}
        for (int i = 1; i < n; i++) { //сортировка вставками
            int cur_Sum = Mas_sum[i];
            int[] cur_Row = Matrix[i];
            int j = i - 1;
            while (j >= 0 && Mas_sum[j] > cur_Sum) {//сдвиг эл-в > текущ
                Mas_sum[j + 1] = Mas_sum[j];
                Matrix[j + 1] = Matrix[j];
                j--;
            }
            Mas_sum[j + 1] = cur_Sum;//текущий элемент на правильное место
            Matrix[j + 1] = cur_Row;
        }
    }
    static myMatr readMatr(Scanner in) {
        int n = in.nextInt();
        int m = in.nextInt();
        myMatr A = new myMatr(n, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A.Matrix[i][j] = in.nextInt();
            }
        }
        return A;
    }
    void writeMatr(PrintWriter out) {
        out.println(n + " " + m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.print(Matrix[i][j] + " ");
            }
            out.println();
        }
    }

    myMatr matr_ot_row (myMatr a)//матрица отображения номеров строк
    { myMatr row_Ot = new myMatr(a.n,a.m);
        for (int i = 0; i < row_Ot.n; i++) {
            for (int j = 0; j < row_Ot.m; j++) {
                // для элемента [i,j] симметр [m-1-j, n-1-i]
                int i1 = row_Ot.m - 1 - j;
                row_Ot.Matrix[i][j] = i1;
            }}
        return row_Ot;
    }
    myMatr matr_ot_col (myMatr a)//матрица отображения номеров столбцов
    { myMatr col_Ot = new myMatr(a.n,a.m);
        for (int i = 0; i < col_Ot.n; i++) {
            for (int j = 0; j < col_Ot.m; j++) {
                // для элемента [i,j] симметр [m-1-j, n-1-i]
                int j1 = col_Ot.n - 1 - i;
                col_Ot.Matrix[i][j] = j1;
            }}
        return col_Ot;
    }
    myMatr matr_ot (myMatr a)//возвращает матрицу, эл-ты к-рой отображены относ побочной диагонали
    {
        myMatr result = new myMatr(a.n, a.m);
        myMatr col_Ot = matr_ot_col(a);
        myMatr row_Ot = matr_ot_row(a);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                result.Matrix[row_Ot.Matrix[i][j]][col_Ot.Matrix[i][j]] = a.Matrix[i][j];
            }
        }
        return result;
    }
int sum_min (myMatr a)
{
    int sum_min=0;
    // побочная диагональ: i + j = n - 1
    for (int i = 0; i < a.n; i++) {
        int j = a.n - 1 - i;
        sum_min += Math.abs(a.Matrix[i][j]);
    }
    for (int d = 1; d < a.n; d++) {
        int sum = 0; int count=0;
        //диагональ выше побочной: i + j = n - 1 - d
        for (int i = 0; i < a.n; i++) {
            int j = a.n - 1 - d - i;
            if (j >= 0 && j < a.n) {
                sum += Math.abs(a.Matrix[i][j]);
                count++;
            }
        }
        if (count>=2&&(sum < sum_min)) {
            sum_min = sum;
        }
        sum=0; count=0;
        //диагональ ниже побочной: i + j = n - 1 + d
        for (int i = 0; i < a.n; i++) {
            int j = a.n - 1 + d - i;
            if (j >= 0 && j < a.n) {
                sum += Math.abs(a.Matrix[i][j]);
                count++;
            }
        }
        if (count>=2&&(sum < sum_min)) {
            sum_min = sum;
        }
    }
    return sum_min;
}
}