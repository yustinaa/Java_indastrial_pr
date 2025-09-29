/*3.	В тексте после буквы Р, если она не последняя в слове, ошибочно напечатана буква А вместо О.
 Внести исправления в текст.*/
import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("input.txt"));
            String text = "";

            while (sc.hasNextLine()) {
                text += sc.nextLine() + "\n";
            }
            sc.close();
            String correctedT = "";
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String[] words = line.split(" ");
                for (int m = 0; m < words.length; m++) {
                    String word = words[m];
                    String newWord = "";
                    int len = word.length();
                    for (int j = 0; j < len; j++) {
                        char current = word.charAt(j);
                        if ((current == 'Р') && j < len - 1) {
                            char next = word.charAt(j + 1);
                            newWord += current;
                            if (next == 'А') newWord += 'О';
                            else newWord += next;
                            j++;
                        } else {
                            newWord += current;
                        }
                    }
                    words[m] = newWord;
                }
                correctedT += String.join(" ", words) + "\n";
            }

            PrintWriter pw = new PrintWriter(new File("output.txt"));
            pw.println("Текст с РО вместо РА:");
            pw.println(correctedT);
            pw.close();

            System.out.println("Результат в файле output.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }

    }
}




