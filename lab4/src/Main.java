import java.io.*;
import java.util.*;

class Zachetka {
    private String fio;
    private int kurs;
    private String group;
    private List<Session> sessions = new ArrayList<>();

    public Zachetka(String fio, int kurs, String group)
    {
        this.fio = fio;
        this.kurs = kurs;
        this.group = group;
    }
        public Zachetka() {}

    public void readFromString(String line) {
        String[] parts = line.split(";", 4);
        if (parts.length < 4) {
            System.out.println("Неправильный формат строки: " + line);
            return;
        }
        this.fio = parts[0];
        this.kurs = Integer.parseInt(parts[1]);
        this.group = parts[2];
        String[] sessionsParts = parts[3].split(";");
        for (int i = 0; i < sessionsParts.length; i++) {
        String[] sesSplit = sessionsParts[i].split(":", 2);
        int sesNum = Integer.parseInt(sesSplit[0]);
        this.addSession(sesNum);
        Session session = this.sessions.get(this.sessions.size() - 1);
        String[] results = sesSplit[1].split(",");
        for (int j = 0; j < results.length; j++) {
        String[] resSplit = results[j].split("-");
        if (resSplit[0].equalsIgnoreCase("exam")) {
        session.add_exam(resSplit[1], Integer.parseInt(resSplit[2]));
        } else if (resSplit[0].equalsIgnoreCase("zachet")) {
       session.add_zachet(resSplit[1], Boolean.parseBoolean(resSplit[2]));
                    }
                }
            }
        }
    public void print(PrintWriter pw) {
        for (int i = 0; i < sessions.size(); i++) {
        Session ses = sessions.get(i);
        for (int j = 0; j < ses.getExams().size(); j++) {
            Session.Exam ex = ses.getExams().get(j);
            pw.println(this.getFio() + "; " +
                            this.getKurs() + " курс; " +
                            this.getGroup() + " группа; " +
                            "Сессия " + ses.getNumber() + "; " +
                            ex.getSubject() + "; " +
                            ex.getGrade());
        }}}
    public void addSession(int number)
    {
        sessions.add(new Session(number));
    }
    public List<Session> getSessions()
    {
        return sessions;
    }
    public String getFio()
    {
        return fio;
    }
    public int getKurs()
    {
        return kurs;
    }
    public String getGroup()
    {
        return group;
    }
    public boolean isOtlichnik()
    {
        for (int i=0; i< sessions.size(); i++) {
            Session s= sessions.get(i);
            if (!s.is_all_good()) {
                return false;
            }
        }
        return true;
    }
    class Session {
        private int number; // номер сессии
        private List<Exam> exams = new ArrayList<>();
        private List<Zachet> zachety = new ArrayList<>();
        public Session(int number)
        {
            this.number = number;
        }
        public void add_exam(String subject, int grade)
        {
            exams.add(new Exam(subject, grade));
        }
        public void add_zachet(String subject, boolean passed)
        {
            zachety.add(new Zachet(subject, passed));
        }
        public int getNumber()
        {
            return number;
        }
        public List<Exam> getExams()
        {
            return exams;
        }
        public boolean is_all_good() {
            for (int i = 0; i < exams.size(); i++) {
                Exam e = exams.get(i);
                if (e.getGrade() < 9) return false;
            }
            for (int j=0; j< zachety.size(); j++) {
                Zachet z= zachety.get(j);
                if (!z.isPassed()) return false;
            }
            return true;
        }
        class Exam {
            private String subj;
            private int grade;

            public Exam(String subject, int grade)
            {
                this.subj = subject;
                this.grade = grade;
            }

            public String getSubject()
            {
                return subj;
            }

            public int getGrade()
            {
                return grade;
            }
        }

        class Zachet {
            private String subj;
            private boolean passed;

            public Zachet(String subject, boolean passed)
            {
                this.subj = subject;
                this.passed = passed;
            }

            public boolean isPassed()
            {
                return passed;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Zachetka> students = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File("input.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Zachetka st = new Zachetka();   // создаём пустого студента
                st.readFromString(line);
                students.add(st);
            }
            sc.close();

            PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).isOtlichnik()) {
                    students.get(i).print(pw);
                }
            }
            pw.close();

            System.out.println("Конец!!! Результаты в output.txt");

        } catch (IOException e) {
            System.out.println("Ошибочка при работе с файлом: " + e.getMessage());
        }
    }
}


