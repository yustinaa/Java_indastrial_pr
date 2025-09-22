/*7.	Из текста удалить все символы, кроме пробелов, не являющиеся буквами.
 Между последовательностями подряд идущих букв оставить хотя бы один пробел.*/
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Vector;
public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("input.txt"));
            String text = "";

            while (sc.hasNextLine()) {
                text += sc.nextLine() + "\n";
            }
            sc.close();

            String onlyLetters = text.toString().replaceAll("[^a-zA-Zа-яА-Я ]", " ");
            PrintWriter pw = new PrintWriter(new File("output.txt"));
            pw.println("Текст только с буквами и пробелами:");
            pw.println(onlyLetters);
            pw.close();

            System.out.println("Результат в файле output.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }

    }
}




