import java.time.Year;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Trabalho de Campo - Introdução a Algoritmos e Programação
 * UnISCED - Licenciatura em Engenharia Informática
 *
 * Sistema de gestão da Biblioteca Municipal (registo de livros, catálogo,
 * utilizadores, empréstimos/devoluções e estatísticas), com todos os dados
 * guardados em memória através de vetores e de uma matriz - sem base de
 * dados nem ficheiros externos.
 *
 * Como compilar:  javac BibliotecaMunicipal.java
 * Como executar:  java BibliotecaMunicipal
 */
public class BibliotecaMunicipal {

    // ------------------------------------------------------------------
    // Classes do "modelo" - cada uma guarda os dados de uma entidade
    // ------------------------------------------------------------------

    static class Livro {
        int id;
        String titulo;
        String autor;
        int anoPublicacao;
        int quantidadeDisponivel;

        Livro(int id, String titulo, String autor, int anoPublicacao, int quantidadeDisponivel) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
            this.anoPublicacao = anoPublicacao;
            this.quantidadeDisponivel = quantidadeDisponivel;
        }

        public String toString() {
            return "ID:" + id + " | " + titulo + " | " + autor + " | " + anoPublicacao
                    + " | Disponíveis: " + quantidadeDisponivel;
        }
    }

    static class Utilizador {
        int id;
        String nome;

        Utilizador(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public String toString() {
            return "ID:" + id + " | " + nome;
        }
    }

    // guarda um empréstimo feito; devolvido passa a true quando o livro volta
    static class Emprestimo {
        int idLivro;
        int idUtilizador;
        boolean devolvido;

        Emprestimo(int idLivro, int idUtilizador) {
            this.idLivro = idLivro;
            this.idUtilizador = idUtilizador;
            this.devolvido = false;
        }
    }

    // ------------------------------------------------------------------
    // Base de dados simulada em memória (vetores e matriz)
    // ------------------------------------------------------------------

    // capacidade fixa dos vetores (é uma simulação em memória, não há BD)
    static final int CAPACIDADE_MAXIMA = 100;
    static final int CAPACIDADE_EMPRESTIMOS = 1000;

    static Livro[] livros = new Livro[CAPACIDADE_MAXIMA];
    static int totalLivros = 0;

    static Utilizador[] utilizadores = new Utilizador[CAPACIDADE_MAXIMA];
    static int totalUtilizadores = 0;

    static Emprestimo[] emprestimos = new Emprestimo[CAPACIDADE_EMPRESTIMOS];
    static int totalEmprestimos = 0;

    // matriz de estatísticas: linha = livro, coluna = utilizador.
    // matrizEmprestimos[i][j] = quantas vezes o utilizador j já levou o livro i
    static int[][] matrizEmprestimos = new int[CAPACIDADE_MAXIMA][CAPACIDADE_MAXIMA];

    // geradores dos identificadores únicos (nunca se repetem)
    static int proximoIdLivro = 1;
    static int proximoIdUtilizador = 1;

    static Scanner scanner = new Scanner(System.in);

    // ------------------------------------------------------------------
    // Programa principal e menu
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" SISTEMA DE GESTÃO - BIBLIOTECA MUNICIPAL");
        System.out.println("==================================================");

        try {
            boolean continuar = true;
            while (continuar) {
                mostrarMenuPrincipal();
                int opcao = lerInteiro("Escolha uma opção: ");

                switch (opcao) {
                    case 1:
                        menuRegistoLivros();
                        break;
                    case 2:
                        menuConsultaCatalogo();
                        break;
                    case 3:
                        menuUtilizadores();
                        break;
                    case 4:
                        menuEmprestimos();
                        break;
                    case 5:
                        menuEstatisticas();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\nA encerrar o sistema. Até breve!");
                        break;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            }
        } catch (NoSuchElementException e) {
            // acontece se a entrada de dados terminar de forma inesperada (ex.: Ctrl+D)
            System.out.println("\nEntrada de dados terminada. A encerrar o sistema.");
        } finally {
            scanner.close();
        }
    }

    static void mostrarMenuPrincipal() {
        System.out.println("\n----------------- MENU PRINCIPAL -----------------");
        System.out.println("1. Registo de Livros");
        System.out.println("2. Consulta de Catálogo");
        System.out.println("3. Gestão de Utilizadores");
        System.out.println("4. Gestão de Empréstimos");
        System.out.println("5. Estatísticas");
        System.out.println("0. Sair");
        System.out.println("---------------------------------------------------");
    }

    // ------------------------------------------------------------------
    // Registo de Livros
    // ------------------------------------------------------------------

    static void menuRegistoLivros() {
        System.out.println("\n--- Registo de Livros ---");

        if (totalLivros >= CAPACIDADE_MAXIMA) {
            System.out.println("Não é possível registar mais livros: catálogo cheio.");
            return;
        }

        String titulo = lerTexto("Título: ");
        String autor = lerTexto("Autor: ");

        // evita registar duas vezes a mesma obra (mesmo título e mesmo autor)
        int indiceExistente = indiceDoLivroPorTituloEAutor(titulo, autor);
        if (indiceExistente != -1) {
            System.out.println("Este livro já está registado (ID " + livros[indiceExistente].id
                    + "). Registo cancelado.");
            return;
        }

        int ano = lerAnoPublicacao("Ano de publicação: ");
        int quantidade = lerInteiroNaoNegativo("Quantidade disponível: ");

        Livro novoLivro = new Livro(proximoIdLivro, titulo, autor, ano, quantidade);
        livros[totalLivros] = novoLivro;
        totalLivros++;
        proximoIdLivro++;

        System.out.println("Livro registado com sucesso! " + novoLivro);
    }

    // ------------------------------------------------------------------
    // Consulta de Catálogo
    // ------------------------------------------------------------------

    static void menuConsultaCatalogo() {
        System.out.println("\n--- Consulta de Catálogo ---");
        System.out.println("1. Listar todos os livros");
        System.out.println("2. Listar apenas livros disponíveis");
        System.out.println("3. Pesquisar por título");
        System.out.println("4. Pesquisar por autor");
        System.out.println("0. Voltar");

        int opcao = lerInteiro("Escolha uma opção: ");
        switch (opcao) {
            case 1:
                listarLivros();
                break;
            case 2:
                listarLivrosDisponiveis();
                break;
            case 3:
                pesquisarPorTitulo();
                break;
            case 4:
                pesquisarPorAutor();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    static void listarLivros() {
        if (totalLivros == 0) {
            System.out.println("Não existem livros registados.");
            return;
        }
        System.out.println("\nCatálogo completo (" + totalLivros + " livro(s)):");
        for (int i = 0; i < totalLivros; i++) {
            System.out.println(livros[i]);
        }
    }

    // mostra só os livros que ainda têm pelo menos um exemplar para emprestar
    static void listarLivrosDisponiveis() {
        boolean encontrou = false;
        System.out.println("\nLivros com exemplares disponíveis:");
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].quantidadeDisponivel > 0) {
                System.out.println(livros[i]);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Não há livros com exemplares disponíveis no momento.");
        }
    }

    // a pesquisa ignora maiúsculas/minúsculas e aceita apenas parte do texto
    static void pesquisarPorTitulo() {
        String termo = lerTexto("Título (ou parte) a pesquisar: ").toLowerCase();
        boolean encontrou = false;
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].titulo.toLowerCase().contains(termo)) {
                System.out.println(livros[i]);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum livro encontrado com esse título.");
        }
    }

    static void pesquisarPorAutor() {
        String termo = lerTexto("Autor (ou parte) a pesquisar: ").toLowerCase();
        boolean encontrou = false;
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].autor.toLowerCase().contains(termo)) {
                System.out.println(livros[i]);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum livro encontrado desse autor.");
        }
    }

    // ------------------------------------------------------------------
    // Utilizadores
    // ------------------------------------------------------------------

    static void menuUtilizadores() {
        System.out.println("\n--- Gestão de Utilizadores ---");
        System.out.println("1. Registar utilizador");
        System.out.println("2. Listar utilizadores");
        System.out.println("0. Voltar");

        int opcao = lerInteiro("Escolha uma opção: ");
        switch (opcao) {
            case 1:
                registarUtilizador();
                break;
            case 2:
                listarUtilizadores();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    static void registarUtilizador() {
        if (totalUtilizadores >= CAPACIDADE_MAXIMA) {
            System.out.println("Não é possível registar mais utilizadores: limite atingido.");
            return;
        }
        String nome = lerTexto("Nome do utilizador: ");
        Utilizador novo = new Utilizador(proximoIdUtilizador, nome);
        utilizadores[totalUtilizadores] = novo;
        totalUtilizadores++;
        proximoIdUtilizador++;
        System.out.println("Utilizador registado com sucesso! " + novo);
    }

    static void listarUtilizadores() {
        if (totalUtilizadores == 0) {
            System.out.println("Não existem utilizadores registados.");
            return;
        }
        System.out.println("\nUtilizadores registados (" + totalUtilizadores + "):");
        for (int i = 0; i < totalUtilizadores; i++) {
            System.out.println(utilizadores[i]);
        }
    }

    // ------------------------------------------------------------------
    // Empréstimos e devoluções
    // ------------------------------------------------------------------

    static void menuEmprestimos() {
        System.out.println("\n--- Gestão de Empréstimos ---");
        System.out.println("1. Efectuar empréstimo");
        System.out.println("2. Registar devolução");
        System.out.println("3. Listar empréstimos activos");
        System.out.println("0. Voltar");

        int opcao = lerInteiro("Escolha uma opção: ");
        switch (opcao) {
            case 1:
                efectuarEmprestimo();
                break;
            case 2:
                registarDevolucao();
                break;
            case 3:
                listarEmprestimosActivos();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    static void efectuarEmprestimo() {
        if (totalUtilizadores == 0) {
            System.out.println("Não existem utilizadores registados. Registe um utilizador primeiro.");
            return;
        }
        if (totalLivros == 0) {
            System.out.println("Não existem livros registados.");
            return;
        }

        int idLivro = lerInteiro("ID do livro a emprestar: ");
        int indiceLivro = indiceDoLivro(idLivro);
        if (indiceLivro == -1) {
            System.out.println("Livro não encontrado.");
            return;
        }

        int idUtilizador = lerInteiro("ID do utilizador: ");
        int indiceUtilizador = indiceDoUtilizador(idUtilizador);
        if (indiceUtilizador == -1) {
            System.out.println("Utilizador não encontrado.");
            return;
        }

        Livro livro = livros[indiceLivro];
        if (livro.quantidadeDisponivel <= 0) {
            System.out.println("Não há exemplares disponíveis deste livro no momento.");
            return;
        }

        if (totalEmprestimos >= CAPACIDADE_EMPRESTIMOS) {
            System.out.println("Limite de registos de empréstimos atingido.");
            return;
        }

        livro.quantidadeDisponivel--;
        emprestimos[totalEmprestimos] = new Emprestimo(idLivro, idUtilizador);
        totalEmprestimos++;
        matrizEmprestimos[indiceLivro][indiceUtilizador]++; // actualiza a matriz p/ as estatísticas

        System.out.println("Empréstimo registado: \"" + livro.titulo + "\" -> "
                + utilizadores[indiceUtilizador].nome);
    }

    static void registarDevolucao() {
        int idLivro = lerInteiro("ID do livro a devolver: ");
        int indiceLivro = indiceDoLivro(idLivro);
        if (indiceLivro == -1) {
            System.out.println("Livro não encontrado.");
            return;
        }

        int idUtilizador = lerInteiro("ID do utilizador que devolve: ");

        // procura de trás para a frente o empréstimo activo deste par livro/utilizador
        int indiceEmprestimo = -1;
        for (int i = totalEmprestimos - 1; i >= 0; i--) {
            Emprestimo emp = emprestimos[i];
            if (emp.idLivro == idLivro && emp.idUtilizador == idUtilizador && !emp.devolvido) {
                indiceEmprestimo = i;
                break;
            }
        }

        if (indiceEmprestimo == -1) {
            System.out.println("Não foi encontrado um empréstimo activo para este livro e utilizador.");
            return;
        }

        emprestimos[indiceEmprestimo].devolvido = true;
        livros[indiceLivro].quantidadeDisponivel++;

        System.out.println("Devolução registada com sucesso. \"" + livros[indiceLivro].titulo
                + "\" está novamente disponível.");
    }

    static void listarEmprestimosActivos() {
        boolean encontrou = false;
        System.out.println("\nEmpréstimos activos:");
        for (int i = 0; i < totalEmprestimos; i++) {
            Emprestimo emp = emprestimos[i];
            if (!emp.devolvido) {
                int indiceLivro = indiceDoLivro(emp.idLivro);
                int indiceUtilizador = indiceDoUtilizador(emp.idUtilizador);
                String tituloLivro = indiceLivro != -1 ? livros[indiceLivro].titulo : "(livro removido)";
                String nomeUtilizador = indiceUtilizador != -1 ? utilizadores[indiceUtilizador].nome : "(utilizador removido)";
                System.out.println("Livro ID:" + emp.idLivro + " \"" + tituloLivro + "\" | Utilizador ID:"
                        + emp.idUtilizador + " " + nomeUtilizador);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Não há empréstimos activos no momento.");
        }
    }

    // ------------------------------------------------------------------
    // Estatísticas
    // ------------------------------------------------------------------

    static void menuEstatisticas() {
        System.out.println("\n--- Estatísticas ---");

        if (totalEmprestimos == 0) {
            System.out.println("Ainda não foram efectuados empréstimos.");
            return;
        }

        // 1.ª passagem: soma cada linha da matriz (todas as colunas) para saber
        // quantas vezes cada livro foi emprestado, e guarda o maior total
        int[] totalPorLivro = new int[totalLivros];
        int maiorTotal = 0;

        for (int i = 0; i < totalLivros; i++) {
            int totalDoLivro = 0;
            for (int j = 0; j < totalUtilizadores; j++) {
                totalDoLivro += matrizEmprestimos[i][j];
            }
            totalPorLivro[i] = totalDoLivro;
            if (totalDoLivro > maiorTotal) {
                maiorTotal = totalDoLivro;
            }
        }

        // conta quantos livros estão empatados no primeiro lugar
        int empatados = 0;
        for (int i = 0; i < totalLivros; i++) {
            if (totalPorLivro[i] == maiorTotal) {
                empatados++;
            }
        }

        System.out.println("Total de livros requisitados (empréstimos efectuados): " + totalEmprestimos);

        if (empatados > 1) {
            System.out.println("Há um empate entre " + empatados + " livros:");
        }
        // 2.ª passagem: mostra o(s) livro(s) com o maior total
        for (int i = 0; i < totalLivros; i++) {
            if (totalPorLivro[i] == maiorTotal) {
                System.out.println("Livro mais emprestado: \"" + livros[i].titulo + "\" ("
                        + livros[i].autor + ") - " + maiorTotal + " empréstimo(s).");
            }
        }
    }

    // ------------------------------------------------------------------
    // Utilitários de pesquisa
    // ------------------------------------------------------------------

    // devolve a posição do livro no vetor, ou -1 se não existir
    static int indiceDoLivro(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].id == id) return i;
        }
        return -1;
    }

    // devolve a posição do utilizador no vetor, ou -1 se não existir
    static int indiceDoUtilizador(int id) {
        for (int i = 0; i < totalUtilizadores; i++) {
            if (utilizadores[i].id == id) return i;
        }
        return -1;
    }

    // devolve a posição de um livro com o mesmo título e autor, ou -1
    static int indiceDoLivroPorTituloEAutor(String titulo, String autor) {
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].titulo.equalsIgnoreCase(titulo) && livros[i].autor.equalsIgnoreCase(autor)) {
                return i;
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------
    // Utilitários de leitura (com validação da entrada do utilizador)
    // ------------------------------------------------------------------

    // lê uma linha de texto, recusando valores vazios
    static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        String linha = scanner.nextLine().trim();
        while (linha.isEmpty()) {
            System.out.print("O valor não pode ficar vazio. " + mensagem);
            linha = scanner.nextLine().trim();
        }
        return linha;
    }

    // lê um número inteiro; se o utilizador escrever outra coisa, pede de novo
    static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Introduza um número inteiro.");
            }
        }
    }

    static int lerInteiroNaoNegativo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= 0) return valor;
            System.out.println("O valor não pode ser negativo.");
        }
    }

    // o ano tem de estar entre 1 e o ano actual (não há livros "do futuro")
    static int lerAnoPublicacao(String mensagem) {
        int anoActual = Year.now().getValue();
        while (true) {
            int ano = lerInteiro(mensagem);
            if (ano >= 1 && ano <= anoActual) return ano;
            System.out.println("Ano inválido. Introduza um ano entre 1 e " + anoActual + ".");
        }
    }
}
