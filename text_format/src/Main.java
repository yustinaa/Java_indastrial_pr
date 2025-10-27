import java.io.*;
import java.util.*;
import java.io.IOException;

class MyFile
{
    private String inputF;
    private String outputF;

    public MyFile(String inputF, String outputF)
    {
        this.inputF = inputF;
        this.outputF = outputF;
    }

    public String readText() throws IOException
    {
        String text = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(inputF), "UTF-8"));
        String line;
        while ((line = r.readLine()) != null) {
            text += line + " ";
        }
        r.close();
        return text.trim();
    }

    public void writeText(List<String> lines) throws IOException {
        PrintWriter out = new PrintWriter(outputF, "UTF-8");
        for (String line : lines) {
            out.println(line);
        }
        out.close();
    }

    public List<String> text_to_width(String text, int width) {
        String[] words = text.split("\\s+");
        List<String> result = new ArrayList<>();

        List<String> lineWords = new ArrayList<>();
        int currentLength = 0;

        for (String word : words) {
            if (currentLength + word.length() + lineWords.size() > width) {
                result.add(line_to_width(lineWords, width));
                lineWords.clear();
                currentLength = 0;
            }
            lineWords.add(word);
            currentLength += word.length();
        }

        if (!lineWords.isEmpty()) {
            result.add(String.join(" ", lineWords)); // последняя строка без выравнивания
        }

        return result;
    }

    private String line_to_width(List<String> words, int width) {
        if (words.size() == 1)
            return words.get(0);

        int totalChars = 0;
        for (String w : words)
            totalChars += w.length();

        int spaces = width - totalChars;
        int gaps = words.size() - 1;
        int evenSpace = spaces / gaps;
        int extraSpace = spaces % gaps;

        String line = "";
        for (int i = 0; i < words.size(); i++) {
            line += words.get(i);
            if (i < gaps) {
                int count = evenSpace + (i < extraSpace ? 1 : 0);
                for (int j = 0; j < count; j++) line += " ";
            }
        }
        return line;
    }
}

public class Main {
    public static void main(String[] args) {
        try { Scanner sc = new Scanner(System.in);
        System.out.print("Введите ширину строки: ");
        int width = sc.nextInt();

        MyFile file = new MyFile("input.txt", "output.txt");
        String text = file.readText();
        List<String> res = file.text_to_width(text, width);
        file.writeText(res);
        System.out.println("Текст выровнен и записан в output.txt!");
        }
        catch (IOException e) {
            System.out.println("Ошибка работы с файлом: " + e.getMessage());
        }
    }
}
