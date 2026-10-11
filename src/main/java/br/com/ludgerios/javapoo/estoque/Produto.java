package br.com.ludgerios.javapoo.estoque;

/** Produto com identidade fixa e quantidade controlada por venda e reposição. */
public final class Produto {
    private final int codigoProduto;
    private final String nomeProduto;
    private int quantidade;

    public Produto(int codigoProduto, String nomeProduto, int quantidade) {
        if (codigoProduto <= 0) {
            throw new IllegalArgumentException("Código do produto deve ser positivo.");
        }
        if (nomeProduto == null || nomeProduto.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser nulo ou vazio.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade inicial não pode ser negativa.");
        }

        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto.strip();
        this.quantidade = quantidade;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void vender(int quantidadeVenda) {
        if (quantidadeVenda <= 0) {
            throw new IllegalArgumentException("Quantidade da venda deve ser maior que zero.");
        }
        if (quantidadeVenda > quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para venda.");
        }
        quantidade -= quantidadeVenda;
    }

    public void adicionarEstoque(int quantidadeReposicao) {
        if (quantidadeReposicao <= 0) {
            throw new IllegalArgumentException("Quantidade da reposição deve ser maior que zero.");
        }
        if (quantidadeReposicao > Integer.MAX_VALUE - quantidade) {
            throw new IllegalArgumentException("Reposição excede o limite de estoque.");
        }
        quantidade += quantidadeReposicao;
    }
}
