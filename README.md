# Sistema de Gestão de Biblioteca Municipal

Trabalho de Campo da disciplina **Introdução a Algoritmos e Programação**
Universidade Aberta ISCED (UnISCED) · Faculdade de Engenharia e Agricultura · Licenciatura em Engenharia Informática
**Discente:** Daniel Afonso Macuacua · Maputo, 2026

---

## 1. Descrição

Programa em **Java, executado em consola**, que ajuda os bibliotecários da Biblioteca Municipal a registar obras, consultar o catálogo, gerir utilizadores e tratar de empréstimos e devoluções de forma automática e fiável.

Os dados são guardados numa **base de dados simulada em memória**, usando **vetores (arrays)** e uma **matriz**, como pede o enunciado. Não há base de dados, nem ficheiros, nem bibliotecas externas: quando o programa termina, os dados perdem-se.

## 2. Funcionalidades

| Menu | Funcionalidade |
|------|----------------|
| **1. Registo de Livros** | Insere um livro com ID único (automático), título, autor, ano de publicação e quantidade disponível. Recusa livros repetidos (mesmo título e autor). |
| **2. Consulta de Catálogo** | Lista todos os livros; lista apenas os que têm exemplares disponíveis; pesquisa por título; pesquisa por autor (não distingue maiúsculas de minúsculas e aceita só uma parte do texto). |
| **3. Gestão de Utilizadores** | Regista utilizadores (ID automático e nome) e lista-os. |
| **4. Gestão de Empréstimos** | Efectua empréstimo a um utilizador registado (diminui a quantidade disponível), regista a devolução (repõe a quantidade) e lista os empréstimos activos. |
| **5. Estatísticas** | Mostra o total de livros requisitados e o livro mais emprestado (trata empates). |
| **0. Sair** | Termina o programa. |

## 3. Requisitos e dependências

| Requisito | Detalhe |
|-----------|---------|
| **Java JDK 8 ou superior** | Precisa do **JDK** (inclui o compilador `javac`), não apenas do JRE. Testado com OpenJDK 21. |
| Sistema operativo | Windows, Linux ou macOS |
| Dependências externas | **Nenhuma.** Usa só a biblioteca padrão do Java (`java.util.Scanner`, `java.util.NoSuchElementException`, `java.time.Year`). |
| Ferramentas | Um terminal (Prompt de Comandos, PowerShell, Terminal do Linux/macOS). Git é opcional, só para clonar o repositório. |

### Verificar a instalação do Java

```bash
java -version
javac -version
```

Os dois comandos devem mostrar uma versão (8 ou superior). Se o `javac` não for reconhecido, instale um JDK:

- **Windows / macOS / Linux:** descarregue o instalador do *Eclipse Temurin* em <https://adoptium.net>
- **Ubuntu/Debian:** `sudo apt install default-jdk`
- **macOS (Homebrew):** `brew install openjdk`

## 4. Como obter o código

```bash
git clone https://github.com/SEU-UTILIZADOR/biblioteca-municipal.git
cd biblioteca-municipal
```

(Alternativa: no GitHub, botão **Code → Download ZIP**, e extrair a pasta.)

## 5. Como compilar e executar

Na pasta onde está o ficheiro `BibliotecaMunicipal.java`:

```bash
javac BibliotecaMunicipal.java     # compila (gera os ficheiros .class)
java BibliotecaMunicipal           # executa o programa
```

Alternativa com JDK 11 ou superior (compila e executa num só passo):

```bash
java BibliotecaMunicipal.java
```

### Acentos e caracteres especiais

Os textos do programa têm acentos (ç, ã, é...). Se aparecerem símbolos estranhos:

- **Windows (Prompt de Comandos):** execute `chcp 65001` antes de correr o programa.
- **Em qualquer sistema**, force o UTF-8:
  ```bash
  javac -encoding UTF-8 BibliotecaMunicipal.java
  java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 BibliotecaMunicipal
  ```

### Usar um IDE (NetBeans, IntelliJ IDEA, Eclipse, VS Code)

Crie um projecto Java vazio, copie `BibliotecaMunicipal.java` para a pasta de código-fonte (sem `package`) e execute a classe `BibliotecaMunicipal`, que contém o método `main`.

## 6. Demonstração rápida

A pasta `demo/` inclui `entrada_demo.txt`, com uma sequência de opções já preparada (regista 4 livros e 3 utilizadores, faz empréstimos e devoluções, provoca erros de propósito e mostra as estatísticas). É a mesma sessão usada nas figuras da documentação técnica.

```bash
# Linux / macOS / Prompt de Comandos do Windows
java BibliotecaMunicipal < demo/entrada_demo.txt

# PowerShell
Get-Content demo\entrada_demo.txt | java BibliotecaMunicipal
```

Ao usar um ficheiro de entrada, o texto "digitado" não aparece no ecrã (só as respostas do programa); numa execução normal, aparece o que se escreve.

## 7. Como usar

Ao arrancar, o programa mostra o menu principal:

```
==================================================
 SISTEMA DE GESTÃO - BIBLIOTECA MUNICIPAL
==================================================

----------------- MENU PRINCIPAL -----------------
1. Registo de Livros
2. Consulta de Catálogo
3. Gestão de Utilizadores
4. Gestão de Empréstimos
5. Estatísticas
0. Sair
---------------------------------------------------
Escolha uma opção:
```

Escreva o número da opção e prima **Enter**. Depois de cada operação, o menu volta a aparecer. Os submenus têm sempre a opção **0. Voltar**.

**Fluxo típico:** (1) registar livros → (3) registar utilizadores → (4) efectuar empréstimos e devoluções → (5) consultar as estatísticas. Para emprestar é preciso ter pelo menos um livro e um utilizador registados; os IDs a usar aparecem nas listagens.

### Exemplo de sessão (excerto)

```
Escolha uma opção: 4

--- Gestão de Empréstimos ---
1. Efectuar empréstimo
2. Registar devolução
3. Listar empréstimos activos
0. Voltar
Escolha uma opção: 1
ID do livro a emprestar: 1
ID do utilizador: 1
Empréstimo registado: "Os Lusíadas" -> Daniel Macuacua

Escolha uma opção: 5

--- Estatísticas ---
Total de livros requisitados (empréstimos efectuados): 4
Livro mais emprestado: "Os Lusíadas" (Luís de Camões) - 2 empréstimo(s).
```

## 8. Estruturas de dados

| Estrutura | Tipo | Capacidade | Função |
|-----------|------|-----------:|--------|
| `Livro[] livros` | Vetor de objectos | 100 | Catálogo (ID, título, autor, ano, quantidade disponível) |
| `Utilizador[] utilizadores` | Vetor de objectos | 100 | Leitores registados |
| `Emprestimo[] emprestimos` | Vetor de objectos | 1000 | Histórico de empréstimos (livro, utilizador, devolvido) |
| `int[][] matrizEmprestimos` | Matriz 100 × 100 | 10 000 | Linha = livro, coluna = utilizador; guarda quantas vezes cada utilizador levou cada livro |
| `totalLivros`, `totalUtilizadores`, `totalEmprestimos` | Contadores | — | Posições ocupadas em cada vetor |
| `proximoIdLivro`, `proximoIdUtilizador` | Geradores de ID | — | Garantem IDs únicos |

**Livro mais emprestado:** o programa soma cada linha da matriz (todos os empréstimos de um livro, de qualquer utilizador), guarda o maior total e mostra todos os livros que o atingem (o que trata empates).

## 9. Validação de dados e tratamento de erros

- Texto ou números inválidos onde se espera um inteiro (letras, decimais, números gigantes): mensagem e nova pergunta, sem o programa falhar.
- Campos de texto vazios (título, autor, nome, termos de pesquisa) não são aceites.
- Ano de publicação tem de estar entre 1 e o ano actual; a quantidade não pode ser negativa.
- Não regista o mesmo livro duas vezes (mesmo título e autor, ignorando maiúsculas).
- Não empresta livros inexistentes, a utilizadores inexistentes, nem sem exemplares disponíveis.
- Não aceita devolver um livro que não está emprestado, nem devolver duas vezes o mesmo empréstimo.
- Avisa quando os vetores atingem a capacidade máxima (100 livros, 100 utilizadores, 1000 empréstimos).
- Se a entrada de dados terminar de forma inesperada (Ctrl+D / Ctrl+Z), o programa encerra sem excepções.

## 10. Testes

Além da sessão de demonstração, foram testados (com sequências de entradas preparadas) os valores-limite e os limites de capacidade: anos 0, 1 e 3000; quantidades −2 e 0; 101.º livro, 101.º utilizador e 1001.º empréstimo; devolução repetida; empate nas estatísticas; número gigante e decimal no menu; fim inesperado da entrada. Em todos os casos o resultado foi o esperado. A lista completa está na secção 4.10 da documentação técnica.

## 11. Estrutura do repositório

```
Sistema-de-Gest-o-de-Biblioteca-Municipal/
├── BibliotecaMunicipal.java      # código-fonte completo (ficheiro único)
├── README.md                     # este ficheiro
├── .gitignore
├── demo/
│   └── entrada_demo.txt          # sessão de demonstração pronta a executar
└── docs/
    └── Documentacao_Tecnica_Biblioteca_Municipal.docx
```

## 12. Limitações e melhorias futuras

- Os dados só existem em memória e perdem-se ao fechar o programa (melhoria: guardar em ficheiro ou base de dados).
- A capacidade dos vetores é fixa (melhoria: listas dinâmicas).
- Não há prazos de devolução, multas nem limite de livros por utilizador.
- Interface apenas em consola (melhoria: interface gráfica).

## 13. Documentação técnica

A documentação completa (introdução, objectivos, referencial teórico, metodologia, desenvolvimento com as figuras da execução, conclusão e referências) está em [`docs/Documentacao_Tecnica_Biblioteca_Municipal.docx`](docs/Documentacao_Tecnica_Biblioteca_Municipal.docx).

## 14. Autor e licença

Daniel Afonso Macuacua, discente da Licenciatura em Engenharia Informática da UnISCED.
Trabalho académico desenvolvido para fins educativos.
