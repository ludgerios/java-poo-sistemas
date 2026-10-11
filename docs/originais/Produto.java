package prova;

public class Produto {
    private int codigoProduto;
    private String nomeProduto;
    private int quantidade;

    public Produto(int codigoProduto, String nomeProduto, int quantidade) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
    }

    public void adicionarEstoque(int qtd) {
        quantidade += qtd;
    }

    public boolean vender(int qtd) {
        if (qtd <= quantidade) {
            quantidade -= qtd;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Código: " + codigoProduto +
               "\nProduto: " + nomeProduto +
               "\nQuantidade: " + quantidade;
    }
}