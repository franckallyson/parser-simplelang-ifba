# parser-simplelang-ifba
This is a basic parser of a imaginary programming language named SimpleLang.
___
# Exercício

---
## 1. Descrição da Linguagem:
  * SimpleLang é uma linguagem simples voltada para operações matemáticas básicas e manipulação de variáveis. Ela suporta declarações de variáveis, atribuições e operações aritméticas, além de estruturas condicionais e de repetição simples.

---
## 2. Elementos da Linguagem:
### a. Palavras-chave:
  *    int : define uma ou um conjunto de variáveis inteiras.
  *    float: define uma ou um conjunto de variáveis reais.
  *    print : imprime o valor de uma variável ou expressão.
  *    if: inicia um controle.
  *    else: inicia o bloco alternativo de um controle.
  *    while: inicia um laço.

### b. Operadores:
* Aritméticos: +, -, *, /
* Comparação: ==, !=, <, >, <=, >=
* Atribuição: =

### c. Delimitadores:
* { } : delimitam blocos de código, usados em estruturas condicionais e laços.

### d. Comentários:
* Comentários de linha única iniciam com #.

### e. Estrutura de Controle:
* if (condição) { ... } else { ... }

### f. Estrutura de Repetição
* while (condição) { ... }

---
## 3. Sintaxe Básica:
### a) Declaração de Variáveis:
*     int xa1,ya,za

### b) Atribuição de Valor:
*     x = 5

### c) Operações Aritméticas:
*     x = 5 + 3
      y = x * 2
  
### d) Controle:
*     if (x > y) {
          print x
      } else {
          print y
      }

### e) Laço:
*     while (x <= y) {
          x = x + 1
      }

### f) Comentários:
*     # Este é um comentário

___
## 4. Exemplos de Códigos em SimpleLang:
### a) Exemplo 1:
*     int aa, b
      aa = 10
      b = aa + 5
      print b

### b) Exemplo 2:
*     # meu programa
      int x, y
      x = 8
      y = 3
      # vai começar o if
      if (x > y) {
          print x
      } else {
          print y
      }

### c) Exemplo 3:
*     int x, y
      x = 8
      y = 3
      # vai começar o laço
      while (x <= y) {
          if (x > y) {
              print x
          } else {
              print y
          }
          x = x + 1
      }

___
## 5. Regras Léxicas e Sintáticas:
### a) Tokens Léxicos:
* Números Inteiros: Sequências de dígitos (ex: 123)
* dentificadores: Sequências alfanuméricas iniciando com letra (ex: x1, total)
* Operadores: +, -, *, /, =, ==, !=, <, >, <=, >=
* Palavras-chave: int, float, print, if, else, while
* Delimitadores: {, }
* Comentários: Linha única iniciada por #

___
# Execução do Projeto
## 1. BNF Gerado
> Para a execução do projeto, geramos o seguinte código DNF -> Cole o código em [BNFPlayground](https://bnfplayground.pauliankline.com/)


```bnf 
<programa> ::= <declaracoes>

<declaracoes> ::= <declaracao> ("\n" (" ")* <declaracao>)*

<declaracao> ::= <declaracao_variavel> | <atribuicao> | <operacao_aritmetica> | <print> | <controle> | <laco> | <comentario>

<laco> ::= <palavra_chave_laco> (" ")* "(" <condicao> ")" (" ")* <delimitador_abertura> <declaracoes> <delimitador_fechamento>

<controle> ::= <palavra_chave_controle> (" ")* "(" <condicao> ")" (" ")* <delimitador_abertura> <declaracoes> <delimitador_fechamento> (<palavra_chave_alternativo_controle> (" ")* <delimitador_abertura> <declaracoes> <delimitador_fechamento>)*

<condicao> ::= (<variavel> | <digito>) (" ")* <operador_comparacao> (" ")* (<variavel> | <digito>)

<print> ::= <palavra_chave_impressao> " " (<variavel> | <texto>)

<atribuicao> ::= <variavel> (" ")* <operador_atribuicao> (" ")* (<numero> | <operacao_aritmetica>)

<operacao_aritmetica> ::= (<variavel> | <digito>) (" ")* <operador_aritmetico> (" ")* (<variavel> | <digito>)

<declaracao_variavel> ::= <palavra_chave_tipo> " " <lista_variaveis>

<lista_variaveis> ::= <variavel> ((", " | ",") <variavel>)*

<variavel> ::= <letra> (<letra> | <digito>)*

<comentario> ::= "#" (" ")* <texto>

<delimitador_fechamento> ::= (" " | "\n")* "}" (" " | "\n")*

<delimitador_abertura> ::= "{" (" " | "\n")*

<operador_atribuicao> ::= "="

<operador_comparacao> ::= "==" | "!=" | "<" | ">" | "<=" | ">="

<operador_aritmetico> ::= "+" | "-" | "*" | "/"

<palavra_chave_laco> ::= "while"

<palavra_chave_alternativo_controle> ::= "else"

<palavra_chave_controle> ::= "if"

<palavra_chave_impressao> ::= "print"

<palavra_chave_tipo> ::= "int" | "float"

<texto> ::= (<numero> | <letra>) (<numero> | <letra> | " ")*

<numero> ::= <digito> <digito>*

<digito> ::= [0-9]

<letra> ::= [a-z] | [A-Z]
```

## 2. Autômato Finito Determinístico (DFA) Gerado
> Para a geração do DFA o seguinte código foi criado -> Cole o código em [Edutor.net](https://edotor.net/)

<img src="images\DFA - Grafo.png"/>

```dfa
digraph BNF {
    rankdir=LR;
    node [shape=circle];

    // Definição dos estados
    start [shape=doublecircle, label="q0 (inicial)"];
    
    q1 [label="q1"];  // Tipo
    q2 [label="q2"];  // Identificador
    q3 [label="q3"];  // =
    q4 [label="q4"];  // Número / Identificador
    q5 [label="q5"];  // Operador Aritmético
    q6 [label="q6"];  // Número / Identificador
    q7 [label="q7"];  // while
    q8 [label="q8"];  // (
    q9 [label="q9"];  // Identificador no while
    q10 [label="q10"]; // Número no while
    q11 [label="q11"]; // Operador de comparação
    q12 [label="q12"]; // Identificador no while
    q13 [label="q13"]; // Número no while
    q14 [label="q14"]; // )
    q15 [label="q15"]; // {
    q16 [label="q16"]; // Instruções
    q17 [label="q17"]; // }

    q18 [label="q18"]; // if
    q19 [label="q19"]; // (
    q20 [label="q20"]; // Variável no if
    q21 [label="q21"]; // Número no if
    q22 [label="q22"]; // Operador de comparação no if
    q23 [label="q23"]; // Identificador no if
    q24 [label="q24"]; // Número no if
    q25 [label="q25"]; // )
    q26 [label="q26"]; // {
    q27 [label="q27"]; // Instruções no if
    q28 [label="q28"]; // }
    q29 [label="q29"]; // else
    q30 [label="q30"]; // {
    q31 [label="q31"]; // Instruções no else
    q32 [label="q32"]; // }

    q33 [label="q33"]; // print
    q34 [label="q34"]; // Variável no print
    q35 [label="q35"]; // Comentário
    q36 [label="q36"]; // Texto no comentário

    // Transições
    start -> q1 [label="tipo"];
    start -> q2 [label="identificador"]
    start -> q7 [label="while"];
    start -> q18 [label="if"];
    start -> q33 [label="print"];
    start -> q35 [label="#"];

    q1 -> q2 [label="identificador"];
    q2 -> q1 [label=","];
    
    q2 -> q3 [label="="];
    q3 -> q4 [label="Número/Identificador"];
    q4 -> q5 [label="+, -, *, /"];
    q5 -> q6 [label="Número/Identificador"];

    q7 -> q8 [label="("];
    q8 -> q9 [label="identificador"];
    q8 -> q10 [label="número"];
    q9 -> q11 [label=">, <, <=, >=, =="];
    q10 -> q11 [label=">, <, <=, >=, =="];
    q11 -> q12 [label="identificador"];
    q11 -> q13 [label="número"];
    q12 -> q14 [label=")"];
    q13 -> q14 [label=")"];
    q14 -> q15 [label="{"];
    q15 -> q16 [label="Instruções"];
    q16 -> q17 [label="}"];

    q18 -> q19 [label="("];
    q19 -> q20 [label="identificador"];
    q19 -> q21 [label="número"];
    q20 -> q22 [label=">, <, <=, >=, =="];
    q21 -> q22 [label=">, <, <=, >=, =="];
    q22 -> q23 [label="identificador"];
    q22 -> q24 [label="número"];
    q23 -> q25 [label=")"];
    q24 -> q25 [label=")"];
    q25 -> q26 [label="{"];
    q26 -> q27 [label="Instruções"];
    q27 -> q28 [label="}"];
    q28 -> q29 [label="else"];
    q29 -> q30 [label="{"];
    q30 -> q31 [label="Instruções"];
    q31 -> q32 [label="}"];

    q33 -> q34 [label="identificador"];

    q35 -> q36 [label="caracteres"];

    // Transições de retorno ao estado inicial
    q4 -> start;
    q6 -> start;
    q17 -> start;
    q28 -> start;
    q32 -> start;
    q34 -> start;
    q36 -> start;
    

    q2 -> start;
}
```

## 3. Implementação e testes
> Com base no que foi descrito e implementado previamente, foi criado o software em linguagem JAVA, se encontra na pasta SRC deste projeto.
> 
> __Obs: Algumas adaptações no código foram feitas principalmente nos casos de IF-Else e While, bem como abertura e fechamento de escopo e podem não condizer exatamente com o DFA.__
> 
> Exemplo: no Q27 instruções, poderá ser qualquer instrução anteriormente declarada como aceita. 
> Exemplo2: ao passar pelo q26, é validado tanto se é um fechamento de escopo quanto se é um Else, se for else, vai pular diretamente para o Q29. 
> 
> __Essas alterações básicas estão comentadas e constam na função transitar__
### Teste Exemplo 1:

<img src="images\teste-exemplo1.png"/>

### Teste Exemplo 2:

<img src="images\teste-exemplo2.png"/>

### Teste Exemplo 3:

<img src="images\teste-exemplo3.png"/>

### Teste Nível Máximo
```txt
# Comentário

int a, b, c

if (a>b) {
    int d, e, f
    float g,h
    int i
    d = i + 1
    g = e + f
    h = 2 + 2
    while(h>=c){
        # comentário teste
        print d
        print teste
        if(c==h){
            print aqui
            while(1<2){
                print teste
            }
            print mais
            i = 1+1
        }
    }
    int b = a/c
}
```

<img src="images\teste-final.png"/>

___
### Obrigado por ler até aqui! Esperamos ter contribuido para seu aprendizado tanto quanto o nosso!
       
       
       
       
      
