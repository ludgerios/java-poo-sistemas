package br.com.ludgerios.javapoo.app;

import br.com.ludgerios.javapoo.console.LeitorConsole;
import br.com.ludgerios.javapoo.estoque.Produto;
import java.util.Objects;

public final class MenuEstoque {
    private final LeitorConsole console;
    private Produto produto;

    public MenuEstoque(LeitorConsole console) {
        this.console = Objects.requireNonNull(console, "Console não pode ser nulo.");
    }

    public void executar() {
        while (true) {
            console.escreverLinha("\n===== MENU DE ESTOQUE =====");
            console.escreverLinha("1 - Cadastrar produto");
            console.escreverLinha("2 - Consultar estoque");
            console.escreverLinha("3 - Vender produto");
            console.escreverLinha("4 - Adicionar ao estoque");
            console.escreverLinha("5 - Retornar ao menu principal");
            int opcao = console.lerOpcao("Escolha uma opção: ", 5);
            try {
                switch (opcao) {
                    case 1 -> cadastrarProduto();
                    case 2 -> mostrarProduto();
                    case 3 -> vender();
                    case 4 -> repor();
                    case 5 -> {
                        console.escreverLinha("Retornando ao menu principal.");
                        return;
                    }
                }
            } catch (IllegalArgumentException erro) {
                console.escreverLinha("Erro: " + erro.getMessage());
            }
        }
    }

    private void cadastrarProduto() {
        if (produto != null) {
            console.escreverLinha("Já existe um produto cadastrado. O cadastro atual foi preservado.");
            return;
        }
        int codigo = console.lerInteiro("Código do produto: ", 1, Integer.MAX_VALUE);
        String nome = console.lerTexto("Nome do produto: ");
        int quantidade = console.lerInteiro("Quantidade inicial: ", 0, Integer.MAX_VALUE);
        produto = new Produto(codigo, nome, quantidade);
        console.escreverLinha("Produto cadastrado com sucesso!");
        mostrarProduto();
    }

    private void mostrarProduto() {
        if (temProduto()) {
            console.escreverLinha("Código: " + produto.getCodigoProduto());
            console.escreverLinha("Produto: " + produto.getNomeProduto());
            console.escreverLinha("Quantidade: " + produto.getQuantidade());
        }
    }

    private void vender() {
        if (temProduto()) {
            produto.vender(console.lerInteiro("Quantidade a vender: "));
            console.escreverLinha("Venda realizada com sucesso!");
        }
    }

    private void repor() {
        if (temProduto()) {
            produto.adicionarEstoque(console.lerInteiro("Quantidade a adicionar: "));
            console.escreverLinha("Estoque atualizado com sucesso!");
        }
    }

    private boolean temProduto() {
        if (produto == null) {
            console.escreverLinha("Nenhum produto cadastrado.");
            return false;
        }
        return true;
    }
}
