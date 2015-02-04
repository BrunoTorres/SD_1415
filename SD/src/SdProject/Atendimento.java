
package SdProject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Atendimento extends Thread {

    private GRT grt;
    private Socket s;
    private ObjectInputStream in;
    private PrintWriter out;

    public Atendimento(Socket so, GRT g) {
        this.grt = g;
        this.s = so;
        this.in = null;
        this.out = null;

    }

    @Override
    public void run() {

        try {
            this.in = new ObjectInputStream(s.getInputStream());
            this.out = new PrintWriter(s.getOutputStream());
            ArrayList<String> input;

            while (true) {                
                input = (ArrayList<String>) in.readObject();

                if (input.get(0).equals("Registar")) {

                    String username = input.get(1);
                    String pass = input.get(2);

                    boolean flag = grt.registaUtilizador(username, pass);

                    if (flag) {
                        out.println("Reg");
                        out.flush();
                    } else {
                        out.println("Erro");
                        out.flush();
                    }

                }

                if (input.get(0).equals("Login")) {

                    String username = input.get(1);
                    String pass = input.get(2);

                    boolean user = this.grt.getUtilizadores().containsKey(username);
                    boolean login = false;
                    if (user) {
                        login = this.grt.getUtilizadores().get(username).getPassword().equals(pass);
                        if (login) {
                            out.println("Entrou");
                            out.flush();
                        } else {
                            out.println("Pass errada");
                            out.flush();

                        }

                    } else {
                        out.println("Username errado");
                        out.flush();

                    }

                }
                if (input.get(0).equals("Abastecer")) {
                    String nome = input.get(1);
                    int quantidade = Integer.parseInt(input.get(2));
                    this.grt.supply(nome, quantidade);

                    out.println("Abastecido");
                    out.flush();
                }

                if (input.get(0).equals("Criar")) {
                    String nomeTarefa = input.get(1);
                    String nomef;
                    int quantidade;
                    boolean b = true;
                    HashMap<String, Integer> fNec = new HashMap<>();
                    b = this.grt.naoExisteTarefa(nomeTarefa);

                    for (int i = 2; i < input.size() && b; i += 2) {
                        nomef = input.get(i);
                        quantidade = Integer.parseInt(input.get(i + 1));

                        b = this.grt.armazem.containsKey(nomef); 
                        fNec.put(nomef, quantidade);

                    }

                    if (b) {
                        Tarefa t = new Tarefa(nomeTarefa, this.grt.armazem, fNec);
                        this.grt.inserirTarefa(t);

                        out.println("Criada");
                        out.flush();
                    } else {
                        out.println("Erro");
                        out.flush();

                    }

                }
                if (input.get(0).equals("Realizar")) {
                    String nomeTarefa = input.get(1);

                    boolean b = this.grt.naoExisteTarefa(nomeTarefa);
                    if (b) {
                        out.println("Erro");
                        out.flush();
                    } else {

                        int idTarefa = this.grt.realizaTarefa(nomeTarefa);
                        out.println(String.valueOf(idTarefa));
                        out.flush();

                    }
                }
                if (input.get(0).equals("Concluir")) {
                    int idTarefa = Integer.parseInt(input.get(1));

                    boolean b = this.grt.existeIdTarefa(idTarefa);
                    if (!b) {
                        out.println("Erro");
                        out.flush();
                    } else {
                        this.grt.concluirTarefa(idTarefa);
                        out.println("Concluida");
                        out.flush();

                    }
                }
                if (input.get(0).equals("Notificar")) {
                    int idTarefa;

                    for (int i = 1; i < input.size(); i++) {
                        idTarefa = Integer.parseInt(input.get(i));
                        if (this.grt.getTarefasEmCurso().containsKey(idTarefa)) {

                            this.grt.notificarTarefa(idTarefa);
                        }

                    }

                    out.println("Notifica");
                    out.flush();
                }

                if (input.get(0).equals("Lista")) {
                    ArrayList<String> mensagem;
                    mensagem = new ArrayList<>();
                    String nova;
                    int c = 0;

                    for (String t : this.grt.getTarefas().keySet()) {

                        nova = "Tipo : ".concat(t);

                        mensagem.add(nova);
                        for (int i : this.grt.getTarefasEmCurso().keySet()) {
                            if (this.grt.getTarefasEmCurso().get(i).getNome().equals(t)) {
                                mensagem.add("id -> ".concat(String.valueOf(i)));
                            }
                        }

                    }

                    ObjectOutputStream o;
                    o = new ObjectOutputStream(s.getOutputStream());
                    o.writeObject(mensagem);
                    o.flush();
                }

            }

        } catch (Exception e) {
            

        }
    }
        

}
