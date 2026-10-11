package br.com.ludgerios.javapoo.estoque;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProdutoTest {
    @Test
    void cadastraProdutoComDadosValidos() {
        Produto produto = new Produto(123, "Caderno", 10);

        assertAll(
                () -> assertEquals(123, produto.getCodigoProduto()),
                () -> assertEquals("Caderno", produto.getNomeProduto()),
                () -> assertEquals(10, produto.getQuantidade()));
    }

    @Test
    void removeEspacosExternosSemAlterarNomeDoProduto() {
        Produto produto = new Produto(1, "\t Caderno  azul \u2003", 0);

        assertEquals("Caderno  azul", produto.getNomeProduto());
    }

    @Test
    void aceitaMaiorCodigoDeProdutoInteiro() {
        Produto produto = new Produto(Integer.MAX_VALUE, "Caderno", 0);

        assertEquals(Integer.MAX_VALUE, produto.getCodigoProduto());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    void rejeitaCodigoNaoPositivo(int codigo) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Produto(codigo, "Caderno", 10));

        assertEquals("Código do produto deve ser positivo.", erro.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void rejeitaNomeNuloOuEmBranco(String nome) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1, nome, 10));

        assertEquals("Nome do produto não pode ser nulo ou vazio.", erro.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void rejeitaQuantidadeInicialNegativa(int quantidade) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Produto(1, "Caderno", quantidade));

        assertEquals("Quantidade inicial não pode ser negativa.", erro.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, Integer.MAX_VALUE})
    void aceitaLimitesDaQuantidadeInicial(int quantidade) {
        Produto produto = new Produto(1, "Caderno", quantidade);

        assertEquals(quantidade, produto.getQuantidade());
    }

    @Test
    void vendaValidaReduzEstoqueEPreservaIdentidade() {
        Produto produto = new Produto(123, "Caderno", 10);

        produto.vender(3);

        assertAll(
                () -> assertEquals(7, produto.getQuantidade()),
                () -> assertEquals(123, produto.getCodigoProduto()),
                () -> assertEquals("Caderno", produto.getNomeProduto()));
    }

    @Test
    void permiteVenderTodoEstoque() {
        Produto produto = new Produto(1, "Caderno", 10);

        produto.vender(10);

        assertEquals(0, produto.getQuantidade());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    void rejeitaVendaNaoPositivaSemAlterarEstoque(int quantidade) {
        Produto produto = new Produto(1, "Caderno", 10);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.vender(quantidade));

        assertAll(
                () -> assertEquals("Quantidade da venda deve ser maior que zero.", erro.getMessage()),
                () -> assertEquals(10, produto.getQuantidade()));
    }

    @ParameterizedTest
    @ValueSource(ints = {11, Integer.MAX_VALUE})
    void rejeitaVendaAcimaDoEstoqueSemAlterarQuantidade(int quantidade) {
        Produto produto = new Produto(1, "Caderno", 10);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.vender(quantidade));

        assertAll(
                () -> assertEquals("Estoque insuficiente para venda.", erro.getMessage()),
                () -> assertEquals(10, produto.getQuantidade()));
    }

    @Test
    void rejeitaVendaQuandoEstoqueEstaVazio() {
        Produto produto = new Produto(1, "Caderno", 0);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.vender(1));

        assertAll(
                () -> assertEquals("Estoque insuficiente para venda.", erro.getMessage()),
                () -> assertEquals(0, produto.getQuantidade()));
    }

    @Test
    void reposicaoValidaAumentaEstoque() {
        Produto produto = new Produto(1, "Caderno", 10);

        produto.adicionarEstoque(5);

        assertEquals(15, produto.getQuantidade());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    void rejeitaReposicaoNaoPositivaSemAlterarEstoque(int quantidade) {
        Produto produto = new Produto(1, "Caderno", 10);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.adicionarEstoque(quantidade));

        assertAll(
                () -> assertEquals("Quantidade da reposição deve ser maior que zero.", erro.getMessage()),
                () -> assertEquals(10, produto.getQuantidade()));
    }

    @Test
    void permiteReposicaoAteLimiteInteiro() {
        Produto produto = new Produto(1, "Caderno", Integer.MAX_VALUE - 1);

        produto.adicionarEstoque(1);

        assertEquals(Integer.MAX_VALUE, produto.getQuantidade());
    }

    @Test
    void permiteReporMaiorQuantidadeInteiraNoEstoqueVazio() {
        Produto produto = new Produto(1, "Caderno", 0);

        produto.adicionarEstoque(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, produto.getQuantidade());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MAX_VALUE})
    void rejeitaOverflowNaReposicaoSemAlterarEstoque(int quantidade) {
        Produto produto = new Produto(1, "Caderno", Integer.MAX_VALUE);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.adicionarEstoque(quantidade));

        assertAll(
                () -> assertEquals("Reposição excede o limite de estoque.", erro.getMessage()),
                () -> assertEquals(Integer.MAX_VALUE, produto.getQuantidade()));
    }

    @Test
    void rejeitaReposicaoQueExcedeLimitePorUmaUnidade() {
        Produto produto = new Produto(1, "Caderno", 1);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> produto.adicionarEstoque(Integer.MAX_VALUE));

        assertAll(
                () -> assertEquals("Reposição excede o limite de estoque.", erro.getMessage()),
                () -> assertEquals(1, produto.getQuantidade()));
    }

    @Test
    void mantemQuantidadeIntegraAoAlternarOperacoesValidasERejeitadas() {
        Produto produto = new Produto(1, "Caderno", 10);

        produto.vender(4);
        assertThrows(IllegalArgumentException.class, () -> produto.vender(-3));
        assertThrows(IllegalArgumentException.class, () -> produto.adicionarEstoque(-2));
        assertThrows(IllegalArgumentException.class, () -> produto.vender(7));
        produto.adicionarEstoque(2);
        produto.vender(8);

        assertEquals(0, produto.getQuantidade());
    }
}
