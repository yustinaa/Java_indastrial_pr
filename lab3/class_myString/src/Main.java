import java.io.*;
import java.util.*;

class myString {
    String text;

    myString() {
        text = "";
    }

    myString(String t) {
        text = t;
    }

    myString(myString other) {
        this.text = other.text;
    }

    void readStr(Scanner in) {
        text = "";
        while (in.hasNextLine()) {
            text += in.nextLine() + "\n";
        }
    }

    void writeStr(PrintWriter out) {
        out.println(text);
    }

    void most_fr_ch(PrintWriter out) {
        Vector<Character> ch = new Vector<>();
        Vector<Integer> count = new Vector<>();

        char[] arr = text.toCharArray(); // превращаем строку в массив символов

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (Character.isWhitespace(c)) continue; // пропускаем пробелы

            int index = ch.indexOf(c);
            if (index == -1) {
                ch.add(c);
                count.add(1);
            } else {
                count.set(index, count.get(index) + 1);
            }
        }

        int max = 0;
        for (int i = 0; i < count.size(); i++) {
            if (count.get(i) > max) {
                max = count.get(i);
            }
        }

        out.println("Самые частые символы:");
        for (int i = 0; i < ch.size(); i++) {
            if (count.get(i) == max) {
                out.println("'" + ch.get(i) + "' -> " + count.get(i) + " раз(а)");
            }
        }
    }

    void leave_letters() {
        text = text.replaceAll("[^a-zA-Zа-яА-Я ]", " ");
        text = text.replaceAll("\\s+", " ");
    }

    void change_RA() {
        String correctedT = "";
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] words = line.split(" ");
            for (int m = 0; m < words.length; m++) {
                char[] chars = words[m].toCharArray(); // превращаем слово в массив символов
                for (int j = 0; j < chars.length - 1; j++) {
                    if (chars[j] == 'Р' && chars[j + 1] == 'А') {
                        chars[j + 1] = 'О'; // заменяем А на О
                    }
                }
                words[m] = new String(chars); // собираем обратно слово
            }
            correctedT += String.join(" ", words) + "\n";
        }
        text = correctedT;
    }
}
public class Main {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new File("input.txt"));
            PrintWriter out = new PrintWriter("output.txt");

            myString A = new myString();
            A.readStr(in);

            out.println("Исходный текст:");
            A.writeStr(out);
            A.most_fr_ch(out);
            myString B = new myString(A);
            B.leave_letters();
            out.println("Только буквы и пробелы:");
            B.writeStr(out);
            myString C = new myString(A);
            C.change_RA();
            out.println("Исправленный текст (РА -> РО):");
            C.writeStr(out);

            in.close();
            out.close();
            System.out.println("Результат в файле output.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        }
    }
}
