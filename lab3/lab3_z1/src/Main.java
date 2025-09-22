/*11.	В тексте найти и напечатать символы, встречающиеся наибо¬лее часто.*/
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

            Vector<Character> ch = new Vector<>();//символы
            Vector<Integer> count = new Vector<>();//частота символов

            for (int i = 0; i < text.length(); i++)
            {
                char c = text.charAt(i);
                if (Character.isWhitespace(c)) continue;//не учитываем пробелы
                int index = ch.indexOf(c);
                if (index == -1) {
                    ch.add(c);
                    count.add(1);
                } else {
                    int n= count.get(index)+1;
                    count.set(index, n);
                }
            }
            int max=0;
            for (int i = 0; i < count.size(); i++) {
                int p = count.get(i);
                if (p > max) {
                    max = p;
                }
            }
            PrintWriter pw = new PrintWriter(new File("output.txt"));
            pw.println("Самые частые символы:");
            for (int i = 0; i < ch.size(); i++) {
                if (count.get(i) == max) {
                    pw.println("'" + ch.get(i) + "' -> " + count.get(i) + " раз(а)");
                }
            }
            pw.close();

            System.out.println("Результат в файле output.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }

    }
}




