package com.example.polish_zap_calc;

import java.util.Stack;

public class PolishCalc {

    public static String infToPost(String expression) {
        Stack<Character> znaki = new Stack<>();
        StringBuilder output = new StringBuilder();
        int chetSkO = 0;
        int chetSkZ = 0;
        int kolCh = 0;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) { // символ — цифра
                output.append(ch);
                kolCh++;
            } else if (ch == '(') {
                znaki.push(ch);
                chetSkO++;
            } else if (ch == ')') { // выталкиваем операции из стека до '('
                while (znaki.peek() != '(') {
                    output.append(znaki.pop());
                }
                znaki.pop(); // удаляем '('
                chetSkZ++;
            } else if (ch == '+' || ch == '-') {
                while (!znaki.isEmpty() && znaki.peek() != '(') {
                    output.append(znaki.pop());
                }
                znaki.push(ch);
            } else if (ch == '*' || ch == '/') {
                while (!znaki.isEmpty() && znaki.peek() != '(') {
                    char topOp = znaki.peek();
                    if (topOp == '*' || topOp == '/') {
                        output.append(znaki.pop());
                    } else {
                        break;
                    }
                }
                znaki.push(ch);
            }
        }

        if (kolCh == 0) {
            throw new RuntimeException("А цифры где?🕊");
        }

        for (int i = 0; i < expression.length() - 1; i++) {
            char ch1 = expression.charAt(i);
            char ch2 = expression.charAt(i + 1);
            if (Character.isDigit(ch1) && ch2 == '(') {
                throw new RuntimeException("Между цифрой и открывающей скобкой нет знака!");
            }
            if (Character.isDigit(ch2) && ch1 == ')') {
                throw new RuntimeException("Между закрывающей скобкой и цифрой нет знака!");
            }
            if (ch1 == ')' && ch2 == '(') {
                throw new RuntimeException("Между скобками нет знака!");
            }
            if (!Character.isDigit(ch1) && ch1 != ')' && ch1 != '(' && ch1 != '*' && ch1 != '/' && ch1 != '+' && ch1 != '-') {
                throw new RuntimeException("Введен неправильный символ!!!");
            }
        }

        while (!znaki.isEmpty()) {
            output.append(znaki.pop());
        }

        if ((chetSkZ - chetSkO) < 0) {
            throw new RuntimeException("Не хватает открывающих скобок!😭");
        }
        if ((chetSkZ - chetSkO) > 0) {
            throw new RuntimeException("Не хватает закрывающих скобок!😭");
        }

        return output.toString();
    }

    public static int countRes(String resPost) {
        Stack<Integer> zifrs = new Stack<>();

        for (int i = 0; i < resPost.length(); i++) {
            char ch = resPost.charAt(i);

            if (Character.isDigit(ch)) { // символ — цифра
                zifrs.push(ch - '0');
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                int c1 = zifrs.pop();
                int c2 = zifrs.pop();

                if (ch == '+') {
                    zifrs.push(c1 + c2);
                }
                if (ch == '-') {
                    zifrs.push(c2 - c1);
                }
                if (ch == '*') {
                    zifrs.push(c1 * c2);
                }
                if (ch == '/') {
                    if (c1 != 0) {
                        zifrs.push(c2 / c1);
                    } else {
                        throw new RuntimeException("Деление на ноль!!!😰");
                    }
                }
            }
        }

        int res = zifrs.pop();
        return res;
    }
}
