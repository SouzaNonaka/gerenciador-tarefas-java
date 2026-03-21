import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GerenciadorTarefas {
    private static final String ARQUIVO = "tarefas.txt";

    public static void main(String[] args) {
        ArrayList<Tarefa> tarefas = carregarTarefas();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 5) {
            mostrarMenu();
            opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1 -> {
                    adicionarTarefa(tarefas, scanner);
                    salvarTarefas(tarefas);
                }
                case 2 -> listarTarefas(tarefas);
                case 3 -> {
                    removerTarefa(tarefas, scanner);
                    salvarTarefas(tarefas);
                }
                case 4 -> {
                    concluirTarefa(tarefas, scanner);
                    salvarTarefas(tarefas);
                }
                case 5 -> {
                    salvarTarefas(tarefas);
                    System.out.println("Dados salvos. Saindo...");
                }
                default -> System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    // -------- MENU --------
    public static void mostrarMenu() {
        System.out.println("\n1. Adicionar tarefa");
        System.out.println("2. Listar tarefas");
        System.out.println("3. Remover tarefa");
        System.out.println("4. Concluir tarefa");
        System.out.println("5. Sair");
        System.out.print("Opção: ");
    }

    private static int lerOpcao(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            return opcao;
        } else {
            System.out.println("Digite um número válido!");
            scanner.nextLine();
            return -1;
        }
    }

    private static int lerIndice(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int indice = scanner.nextInt() - 1;
            scanner.nextLine();
            return indice;
        } else {
            System.out.println("Digite um número válido!");
            scanner.nextLine();
            return -1;
        }
    }

    // -------- FUNCIONALIDADES --------

    public static void adicionarTarefa(ArrayList<Tarefa> tarefas, Scanner scanner) {
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        tarefas.add(new Tarefa(descricao));
        System.out.println("Tarefa adicionada!");
    }

    public static void listarTarefas(ArrayList<Tarefa> tarefas) {
        System.out.println("\n--- Tarefas ---");

        if (tarefas.isEmpty()) {
            System.out.println("Lista vazia.");
        } else {
            for (int i = 0; i < tarefas.size(); i++) {
                Tarefa t = tarefas.get(i);
                String status = t.isConcluida() ? "[X]" : "[ ]";
                System.out.println((i + 1) + ". " + status + " " + t.getDescricao());
            }
        }
    }

    public static void removerTarefa(ArrayList<Tarefa> tarefas, Scanner scanner) {
        if (tarefas.isEmpty()) {
            System.out.println("Nada para remover.");
            return;
        }

        listarTarefas(tarefas);
        System.out.print("Número da tarefa: ");
        int indice = lerIndice(scanner);

        if (indice >= 0 && indice < tarefas.size()) {
            Tarefa removida = tarefas.remove(indice);
            System.out.println("Removida: " + removida.getDescricao());
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public static void concluirTarefa(ArrayList<Tarefa> tarefas, Scanner scanner) {
        if (tarefas.isEmpty()) {
            System.out.println("Não há tarefas.");
            return;
        }

        listarTarefas(tarefas);
        System.out.print("Número da tarefa: ");
        int indice = lerIndice(scanner);

        if (indice >= 0 && indice < tarefas.size()) {
            tarefas.get(indice).concluir();
            System.out.println("Tarefa concluída!");
        } else {
            System.out.println("Índice inválido.");
        }
    }

    // -------- ARQUIVO --------

    private static void salvarTarefas(ArrayList<Tarefa> tarefas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (Tarefa t : tarefas) {
                // 🔥 SUA MUDANÇA AQUI
                String descricao = t.getDescricao().replace(";", ",");
                writer.println(descricao + ";" + t.isConcluida());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private static ArrayList<Tarefa> carregarTarefas() {
        ArrayList<Tarefa> lista = new ArrayList<>();
        File file = new File(ARQUIVO);

        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");

                if (partes.length == 2) {
                    Tarefa t = new Tarefa(partes[0]);

                    if (Boolean.parseBoolean(partes[1])) {
                        t.concluir();
                    }

                    lista.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }

        return lista;
    }
}