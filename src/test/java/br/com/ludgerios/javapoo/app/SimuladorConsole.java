package br.com.ludgerios.javapoo.app;

import java.io.StringReader;
import java.io.StringWriter;

final class SimuladorConsole {
    private SimuladorConsole() {
    }

    static String executar(String entrada) {
        StringWriter saida = new StringWriter();
        new Aplicacao(new StringReader(entrada), saida).executar();
        return saida.toString().replace('\u00a0', ' ');
    }

    static long ocorrencias(String saida, String texto) {
        return saida.lines().filter(linha -> linha.contains(texto)).count();
    }
}
