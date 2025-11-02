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
        boolean newParagraph = false;

        while ((line = r.readLine()) != null) {
            if (line.startsWith(" ") || line.startsWith("\t")) {
                text += "\n\n" + line.trim(); //новый абзац
                newParagraph = true;
            } else {
                if (!newParagraph && !text.isEmpty())
                    text += " ";
                text += line.trim();
                newParagraph = false;
            }
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
        // Разбиваем весь текст на абзацы (по двойным переводам строк)
        String[] paragraphs = text.split("\\n\\n+");
        List<String> result = new ArrayList<>();

        for (String paragraph : paragraphs) {
            paragraph = paragraph.trim();
            String[] words = paragraph.split("\\s+");
            List<String> lineWords = new ArrayList<>();
            int currentLength = 0;
            boolean firstLine = true;

            for (String word : words) {
                int availableWidth = width;
                if (firstLine) {
                    availableWidth -= 4;
                }
                if (currentLength + word.length() + lineWords.size() > availableWidth) {
                    String line = line_to_width(lineWords, availableWidth);

                    if (firstLine) {
                        result.add("    " + line); // добавляем красную строку
                        firstLine = false;
                    } else {
                        result.add(line);
                    }

                    lineWords.clear();
                    currentLength = 0;
                }

                lineWords.add(word);
                currentLength += word.length();
            }
            // добавляем последнюю строку абзаца
            if (!lineWords.isEmpty()) {
                String lastLine = String.join(" ", lineWords);
                if (firstLine) {
                    result.add("    " + lastLine);

                } else {
                    result.add(lastLine);
                }
            }
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
                int count = evenSpace;
                if (i < extraSpace) {
                    count += 1;
                }
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
