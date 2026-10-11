package prova;

import java.util.Scanner;

public class Estoque {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Produto produto = null;
        int opcao;

        do {
            System.out.println("\n===== MENU DE ESTOQUE =====");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Mostrar estoque");
            System.out.println("3 - Vender produto");
            System.out.println("4 - Adicionar ao estoque");
            System.out.println("5 - Sair");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Código: ");
                    int cod = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Quantidade inicial: ");
                    int qtd = sc.nextInt();
                    produto = new Produto(cod, nome, qtd);
                    System.out.println("\nProduto cadastrado com sucesso!");
                    System.out.println(produto); // já mostra ao cadastrar
                    break;
                case 2:
                    if (produto != null) System.out.println(produto);
                    else System.out.println("Nenhum produto cadastrado!");
                    break;
                case 3:
                    if (produto != null) {
                        System.out.print("Qtd a vender: ");
                        int v = sc.nextInt();
                        if (produto.vender(v)) System.out.println("Venda feita!");
                        else System.out.println("Estoque insuficiente!");
                    } else System.out.println("Nenhum produto cadastrado!");
                    break;
                case 4:
                    if (produto != null) {
                        System.out.print("Qtd a adicionar: ");
                        produto.adicionarEstoque(sc.nextInt());
                        System.out.println("Estoque atualizado!");
                    } else System.out.println("Nenhum produto cadastrado!");
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 5);

        sc.close();
        System.out.println("\nPressione ENTER para finalizar...");
        sc.nextLine();

    }
}