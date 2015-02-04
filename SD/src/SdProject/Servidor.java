package SdProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Servidor {

    private static GRT g = null;

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket ss = new ServerSocket(55000);

        g = new GRT();

        g.registaUtilizador("ze", "1");
        g.registaUtilizador("Manel", "2");
        g.supply("chave", 10);
        g.supply("prego", 10);
        g.supply("martelo", 100);
        HashMap<String, Integer> ferramentas = new HashMap<>();
        ferramentas.put("chave", 10);
        ferramentas.put("prego", 10);
        ferramentas.put("martelo", 10);

        Tarefa t2 = new Tarefa("Tarefaze", g.armazem, ferramentas);

        g.addTarefa(t2);
        AtendimentoServidor as;
        as = new AtendimentoServidor(g);
        as.start();

        while (true) {
            Socket s = ss.accept();
            Atendimento t = new Atendimento(s, g);
            t.start();
        }

    }

}
