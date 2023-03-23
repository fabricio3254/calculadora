import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class calculadora implements ActionListener {

    // Variáveis de instância
    JFrame frame;
    JTextField textField;
    JButton[] buttons;
    String[] buttonLabels = {
        "7", "8", "9", "+",
        "4", "5", "6", "-",
        "1", "2", "3", "*",
        ".", "0", "=", "/"
    };
    JPanel panel;

    // Construtor
    public calculadora() {
        // Cria o frame
        frame = new JFrame("Calculadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        // Cria o painel
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        // Cria o campo de texto
        textField = new JTextField();
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);

        // Cria os botões
        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        // Adiciona os componentes ao frame
        frame.add(textField, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        // Exibe o frame
        frame.setVisible(true);
    }

    // Método actionPerformed
    public void actionPerformed(ActionEvent e)
    {

     String buttonText;
    if (buttonText.equals("=")) {
        // Realiza o cálculo quando o botão "=" é pressionado
        String expression = textField.getText();
        double result = eval(expression);
        textField.setText(Double.toString(result));
    } else {
        // Acrescenta o texto do botão ao campo de texto
        textField.setText(textField.getText() + buttonText);
    }
}

// Método para avaliar a expressão matemática
public static double eval(final String expression) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < expression.length()) throw new RuntimeException("Caractere inesperado: " + (char)ch);
            return x;
        }

        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // adição
                else if (eat('-')) x -= parseTerm(); // subtração
                else return x;
            }
        }

        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); // multiplicação
                else if (eat('/')) x /= parseFactor(); // divisão
                else return x;
            }
        }

        double parseFactor() {
            if (eat('+')) return parseFactor(); // operador unário mais
            if (eat('-')) return -parseFactor(); // operador unário menos

            double x;
            int startPos = this.pos;
            if (eat('(')) { // parênteses
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // números
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z') { // funções matemáticas
                while (ch >= 'a' && ch <= 'z') nextChar();
                String func = expression.substring(startPos, this.pos);
                x = parseFactor();
                if (func.equals("sqrt")) x = Math.sqrt(x);
                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                else throw new RuntimeException("Função desconhecida: " + func);
            } else {
                throw new RuntimeException("Caractere inesperado: " + (char)ch);
            }

            if (eat('^')) x = Math.pow(x, parseFactor()); // potência

            return x;
        }
    }.parse();
}

// Método main
public static void main(String[] args) {
    new calculadora();
}
}