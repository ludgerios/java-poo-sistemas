package br.com.ludgerios.javapoo.estoque;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProdutoTest {
    @Test
    void cadastraProdutoENormalizaONome() {
        Produto produto = new Produto(123, " Caderno ", 10);

        assertAll(
                () -> assertEquals(123, produto.getCodigoProduto()),
                () -> assertEquals("Caderno", produto.getNomeProduto()),
                () -> assertEquals(10, produto.getQuantidade()));
    }

    @Test
    void rejeitaCadastroInvalido() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Produto(0, "Caderno", 10)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Produto(-1, "Caderno", 10)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Produto(1, null, 10)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Produto(1, " ", 10)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Produto(1, "Caderno", -1)));
    }

    @Test
    void permiteEstoqueInicialVazio() {
        assertEquals(0, new Produto(1, "Caderno", 0).getQuantidade());
    }

    @Test
    void vendeERepoeSemAlterarIdentidade() {
        Produto produto = new Produto(123, "Caderno", 10);

        produto.vender(3);
        assertEquals(7, produto.getQuantidade());
        produto.adicionarEstoque(5);

        assertAll(
                () -> assertEquals(12, produto.getQuantidade()),
                () -> assertEquals(123, produto.getCodigoProduto()),
                () -> assertEquals("Caderno", produto.getNomeProduto()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void rejeitaQuantidadesNaoPositivasSemAlterarEstoque(int quantidade) {
        Produto produto = new Produto(1, "Caderno", 10);

        assertThrows(IllegalArgumentException.class, () -> produto.vender(quantidade));
        assertEquals(10, produto.getQuantidade());
        assertThrows(IllegalArgumentException.class, () -> produto.adicionarEstoque(quantidade));
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    void rejeitaVendaAcimaDoEstoque() {
        Produto produto = new Produto(1, "Caderno", 10);

        assertThrows(IllegalArgumentException.class, () -> produto.vender(11));
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    void permiteVenderTodoOEstoque() {
        Produto produto = new Produto(1, "Caderno", 10);

        produto.vender(10);

        assertEquals(0, produto.getQuantidade());
    }

    @Test
    void permiteLimiteInteiroERejeitaOverflowNaReposicao() {
        Produto produto = new Produto(1, "Caderno", Integer.MAX_VALUE - 1);

        produto.adicionarEstoque(1);
        assertEquals(Integer.MAX_VALUE, produto.getQuantidade());
        assertThrows(IllegalArgumentException.class, () -> produto.adicionarEstoque(1));
        assertEquals(Integer.MAX_VALUE, produto.getQuantidade());
        produto.vender(Integer.MAX_VALUE);
        assertEquals(0, produto.getQuantidade());
    }
}
