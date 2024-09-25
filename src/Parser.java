import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

enum Estado {
    Q0,  // Estado inicial
    Q1,  // tipo
    Q2,  // Identificador
    Q3,  // =
    Q4,  // Número / Identificador
    Q5,  // Operador aritmético
    Q6,  // Número / Identificador
    Q7,  // While
    Q8,  // (
    Q9,  // Identificador no while
    Q10, // Número no while
    Q11, // Operador de comparação no while
    Q12, // Identificador no while
    Q13, // Número no while
    Q14, // )
    Q15, // {
    Q16, // Instruções
    Q17, // }
    Q18, // if
    Q19, // (
    Q20, // Variável no if
    Q21, // Número no if
    Q22, // Operador de comparação no if
    Q23, // Identificador no if
    Q24, // Número no if
    Q25, // )
    Q26, // {
    Q27, // Instruções no if
    Q28, // }
    Q29, // else
    Q30, // {
    Q31, // Instruções no else
    Q32, // }
    Q33, // print
    Q34, // Variável no print
    Q35, // Comentário
    Q36  // Texto no comentário
}

public class Parser {

    private Stack<Estado> pilhaBlocos = new Stack<>();  // Pilha para rastrear blocos abertos

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Parser <nome-do-arquivo>");
            return;
        }

        String nomeArquivo = args[0];
        System.out.println(nomeArquivo);
        String entrada = "";

        Parser p = new Parser();

        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null)
                entrada += linha + "\n";

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        boolean resultado = p.processar(entrada);
        System.out.println("Entrada " + (resultado ? "válida" : "inválida"));
    }

    private List<String> tokenizar(String entrada) {
        System.out.println(entrada);
        List<String> tokens = new ArrayList<>();
        int i = 0;

        while (i < entrada.length()) {

            char charAtual = entrada.charAt(i);

            if (Character.isLetter(charAtual)) {
                StringBuilder identificador = new StringBuilder();
                while (i < entrada.length() && Character.isLetter(entrada.charAt(i))) {
                    identificador.append(entrada.charAt(i));
                    i++;
                }
                tokens.add(identificador.toString());
            } else if (Character.isDigit(charAtual)) {
                StringBuilder numero = new StringBuilder();
                while (i < entrada.length() && Character.isDigit(entrada.charAt(i))) {
                    numero.append(entrada.charAt(i));
                    i++;
                }
                tokens.add(numero.toString());
            }// Tratamento para operadores de comparação
            else if (charAtual == '=' && i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                tokens.add("==");
                i += 2;
            } else if (charAtual == '!' && i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                tokens.add("!=");
                i += 2;
            } else if (charAtual == '<' && i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                tokens.add("<=");
                i += 2;
            } else if (charAtual == '>' && i + 1 < entrada.length() && entrada.charAt(i + 1) == '=') {
                tokens.add(">=");
                i += 2;
            }else if (charAtual == '>' || charAtual == '<'){
                tokens.add(String.valueOf(charAtual));
                i++;
            }else if (charAtual == '=' || charAtual == ';' || charAtual == '{' || charAtual == '}') {
                tokens.add(String.valueOf(charAtual));
                i++;
            } else if (charAtual == '(' || charAtual == ')') {
                tokens.add(String.valueOf(charAtual));
                i++;
            } else if (i + 5 <= entrada.length() && entrada.substring(i, i + 5).equals("print")) {
                tokens.add("print");
                i += 5;
            } else if(charAtual == '+' || charAtual == '-'||charAtual == '*' || charAtual == '/'){
                tokens.add(String.valueOf(charAtual));
                i++;
            }else if (charAtual == '#') {
                tokens.add("#");
                i++;
            } else if (charAtual == '\n'){
                tokens.add("\n");
                i++;
            } else if (charAtual == ','){
                tokens.add(";"); // Convertendo para ";" para não haver colisões com a "," da própria lista
                i++;
            } else {
                i++;
            }
        }

        return tokens;
    }

    private Estado transitar(Estado estado, String token) {


        switch (estado) {
            case Q0:
                if (token.equals("int") || token.equals("float")) return Estado.Q1; // Declaração de variável
                if (token.equals("while")) return Estado.Q7; // Loop
                if (token.equals("if")) return Estado.Q18; // Condicional
                if (token.equals("print")) return Estado.Q33; // Output padrão
                if (token.equals("#")) return Estado.Q35; // COmentário de uma linha


                if (token.equals("\n")) return Estado.Q0; // Linha vazia
                if (Character.isLetter(token.charAt(0))) return Estado.Q2; // Caso comece com uma variável (Atribuição)
                break;
            case Q1:
                if (Character.isLetter(token.charAt(0))) return Estado.Q2; // Identificador
                break;
            case Q2:
                if (token.equals("=")) return Estado.Q3; // Atribuição
                if (token.equals(";")) return Estado.Q1; // Mais de uma variável. Conversão para ";" foi definido na função Tokenizar para não colidir com a vírgula da lista gerada.
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                break;
            case Q3:
                if (Character.isLetter(token.charAt(0)) || Character.isDigit(token.charAt(0))) return Estado.Q4; // Pode ser um caracter(variavel) ou um número na atribuição
                break;
            case Q4:
                if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) return Estado.Q5; // Operadores aceitos
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                break;
            case Q5:
                if (Character.isLetter(token.charAt(0)) || Character.isDigit(token.charAt(0))) return Estado.Q6; // Número ou caracter(variavel)
                break;
            case Q6:
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                break;
            case Q7:
                if (token.equals("(")) return Estado.Q8; // Início da condicional do loop WHILE
                break;
            case Q8:
                if (Character.isLetter(token.charAt(0))) return Estado.Q9; // Pode ser uma variável
                if (Character.isDigit(token.charAt(0))) return Estado.Q10; // Pode ser um número (Talvez juntar esses dois em uma única condicional como no Q5.)
                break;
            case Q9, Q10:
                if (token.matches(">|<|>=|<=|==|!=")) return Estado.Q11; // Operadores de comparação aceitos.
                break;
            case Q11:
                if (Character.isLetter(token.charAt(0))) return Estado.Q12; // O Comparando pode ser número ou variavel.
                if (Character.isDigit(token.charAt(0))) return Estado.Q13;
                break;
            case Q12, Q13:
                if (token.equals(")")) return Estado.Q14; // Espera-se o fechamento da condicional do loop.
                break;
            case Q14:
                if (token.equals("{")) { // Abertura do escopo.
                    pilhaBlocos.push(Estado.Q15); // Push na pilha para controle de escopos.
                    return Estado.Q15;
                }
                break;
            case Q15:
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                if (token.equals("}")) {
                    return pilhaBlocos.isEmpty() ? null : pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior ou retorna nulo se não houver o que fechar.
                }
                if (token.equals("print")) return Estado.Q33; // Pode haver print dentro
                if (token.equals("if")) return Estado.Q18; // Pode haver if dentro
                if (token.equals("while")) return Estado.Q7; // Pode haver while dentro
                if (token.equals("int") || token.equals("float")) return Estado.Q1; // Pode haver declaração de variáveis
                if (token.equals("#")) return Estado.Q35;
                if (Character.isLetter(token.charAt(0))) return Estado.Q2; // Caso comece com uma variável (Atribuição)

                return Estado.Q16;
            case Q16:
                if (token.equals("}")) return Estado.Q17; // Espera-se o fechamento do escopo ao fim de tudo.
                break;
            case Q17:
                return Estado.Q0; // Quebra de linha que finaliza a instrução.
            case Q18:
                if (token.equals("(")) return Estado.Q19; // Abertura da condicional do IF.
                break;
            case Q19:
                if (Character.isLetter(token.charAt(0))) return Estado.Q20; // Mesma situação do Q8.
                if (Character.isDigit(token.charAt(0))) return Estado.Q21;
                break;
            case Q20, Q21:
                if (token.matches(">|<|>=|<=|==|!=")) return Estado.Q22; // Operadores Lógicos.
                break;
            case Q22:
                if (Character.isLetter(token.charAt(0))) return Estado.Q23;
                if (Character.isDigit(token.charAt(0))) return Estado.Q24;
                break;
            case Q23, Q24:
                if (token.equals(")")) return Estado.Q25; // Fechamento da condição.
                break;
            case Q25:
                if (token.equals("{")) {
                    pilhaBlocos.push(Estado.Q26); // Push na pilha para controle de escopo
                    return Estado.Q26;
                }
                break;
            case Q26:
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                if (token.equals("else")) return Estado.Q29;
                if (token.equals("}")) {

                    return pilhaBlocos.isEmpty() ? null : pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior
                }
                if (token.equals("print")) return Estado.Q33; // Pode haver print dentro do else
                if (token.equals("if")) return Estado.Q18; // Pode haver if dentro do else
                if (token.equals("while")) return Estado.Q7; // Pode haver while dentro do else
                if (token.equals("int") || token.equals("float")) return Estado.Q1; // Pode haver declaração de variáveis
                if (token.equals("#")) return Estado.Q35;
                if (Character.isLetter(token.charAt(0))) return Estado.Q2; // Caso comece com uma variável (Atribuição)
                break;
            case Q27:
                if (token.equals("}")) {
                    return pilhaBlocos.isEmpty() ? null : pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior
                }
                break;
            case Q28:
                if (token.equals("else")) return Estado.Q29;
                break;
            case Q29:
                if (token.equals("{")) {
                    pilhaBlocos.push(Estado.Q30);
                    return Estado.Q30;
                }
                break;
            case Q30:
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek(); // Retorna o primeiro escopo da pilha sem retira-lo da mesma.
                }
                if (token.equals("}")) {
                    return pilhaBlocos.isEmpty() ? null : pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior
                }
                if (token.equals("print")) return Estado.Q33; // Pode haver print dentro do else
                if (token.equals("if")) return Estado.Q18; // Pode haver if dentro do else
                if (token.equals("while")) return Estado.Q7; // Pode haver while dentro do else
                if (token.equals("int") || token.equals("float")) return Estado.Q1; // Pode haver declaração de variáveis
                if (token.equals("#")) return Estado.Q35;
                if (Character.isLetter(token.charAt(0))) return Estado.Q2; // Caso comece com uma variável (Atribuição)

                return Estado.Q31;
            case Q31:
                if (token.equals("}")) {
                    return pilhaBlocos.isEmpty() ? null: pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior
                }
                break;
            case Q33:
                if (Character.isLetter(token.charAt(0))) { // Nesse caso precisa printar uma variavel
                    return Estado.Q34;
                }
                break;
            case Q34:
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek();
                }

                if (token.equals("}")) {
                    return pilhaBlocos.isEmpty() ? null: pilhaBlocos.pop(); // Fecha o bloco e retorna ao estado anterior
                }
                break;
            case Q35:
                return Estado.Q36; // Aqui pode ser qualquer coisa pois é um comentário
            case Q36:
                if(!token.equals("\n")) return Estado.Q36; // Caso tenha mais de uma palavra no comentário
                if (token.equals("\n")){
                    return pilhaBlocos.isEmpty() ? Estado.Q0: pilhaBlocos.peek();
                }
                break;
            default:
                return null;
        }
        return null;
    }

    private boolean processar(String entrada) {
        Estado estadoAtual = Estado.Q0;
        List<String> tokens = tokenizar(entrada);
        System.out.println(tokens);
        for (String token : tokens) {

            estadoAtual = transitar(estadoAtual, token);

            if (estadoAtual == null) {
                return false;
            }

            if(tokens.indexOf(token) == (tokens.size()-1)){
                estadoAtual = Estado.Q0;
            }

        }


        return estadoAtual == Estado.Q0;
    }
}
