
package SdProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class AtendimentoServidor extends Thread {

    private final GRT grt;
    private static final Scanner in = new Scanner(System.in);
    private static String username;

    public AtendimentoServidor(GRT g) {
        this.grt = g;
    }

    public void run() {
        try {
            menuPrincipal();
        } catch (IOException ex) {
            Logger.getLogger(AtendimentoServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AtendimentoServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void menuPrincipal() throws IOException, ClassNotFoundException {
        String opt;
        opt = menuInicial();
        while (true) {
            if (opt.equals("1")) {
                System.out.println("#################### Registar Utilizador #####################");
                System.out.println("                                                        ");
                in.nextLine();
                System.out.println("   Defina um username                                   ");
                String user = in.nextLine();
                System.out.println("  Defina uma password                                  ");
                String pass = in.nextLine();
                boolean res = this.grt.registaUtilizador(user, pass);
                if (res) {
                    System.out.println("Registo com sucesso");
                    opt = menuInicial();

                } else {
                    System.err.println("                   Falhou registo                         ");
                    System.out.println("                                                       ");
                    System.out.println("##########################################################");
                    System.out.println("                                                       ");
                    menuPrincipal();
                }

            } else {
                if (opt.equals("2")) {
                    System.out.println("######################## Login ##########################");

                    in.nextLine();
                    System.out.println("   Introduza um username                                ");
                    String user = in.nextLine();
                    System.out.println("   Introduza a password                                 ");
                    String pass = in.nextLine();
                    boolean u = this.grt.getUtilizadores().containsKey(user);
                    boolean login = false;
                    if (u) {
                        login = this.grt.getUtilizadores().get(user).getPassword().equals(pass);
                        if (login) {
                            username = user;
                            menuUtilizador();

                        } else {
                            System.err.println("                   Falhou login                         ");
                            System.out.println("                                                       ");
                            System.out.println("##########################################################");
                            System.out.println("                                                       ");
                            menuPrincipal();

                        }
                    } else {
                        if (opt.equals("3")) {
                            System.exit(0);
                        } else {
                            System.out.println("Opção inválida!");
                            menuInicial();
                        }
                    }
                }

            }
        }
    }

    public String menuInicial() {
        System.out.println("###################### Gestor de Requisições/Tarefas ######################");
        System.out.println("#                                                                         #");
        System.out.println("#   1 - Registar                                                          #");
        System.out.println("#   2 - Entrar                                                            #");
        System.out.println("#   3 - Sair da aplicação                                                 #");
        System.out.println("#                                                                         #");
        System.out.println("#   Escolha uma opção:                                                    #");
        System.out.println("##########################################################################");
        String opt = in.next();
        if (!(opt.equals("1") || opt.equals("2") || opt.equals("3"))) {
            opt = menuInicial();
        }
        return opt;
    }

    public void menuUtilizador() throws IOException, ClassNotFoundException {
        
        System.out.println("#################### Menu de Utilizador ######################");
        System.out.println("#                                                        #");
        System.out.println("#   Bem Vindo " + username);
        System.out.println("#                                                        #");
        System.out.println("#   1 - Criar tarefa                                     #");
        System.out.println("#   2 - Realizar tarefa                                  #");
        System.out.println("#   3 - Abastecer armazém                                #");
        System.out.println("#   4 - Concluir tarefa                                  #");
        System.out.println("#   5 - Lista tipos de tarefas                           #");
        System.out.println("#   6 - Ser notificado                                   #");
        System.out.println("#   0 - Logout	                                         #");
        System.out.println("#                                                        #");
        System.out.println("#   Escolha uma opção                                    #");
        System.out.println("##########################################################");
        String opt = in.next();

        while (true) {
            switch (opt) {
                case "1":
                    menuCriaTareda();
                    break;
                case "2":
                    menuRealizarTarefa();
                    break;
                case "3":
                    menuAbastecer();
                    break;
                case "4":
                    menuConcluir();
                    break;
                case "5":
                    menuListaTarefas();
                    break;
                case "6":
                    menuSerNotificado();
                    break;
                case "0":
                    menuPrincipal();
                    break;

                default:
                    System.out.println("Opcão inválida!");
                    menuPrincipal();
                    break;
            }
        }
    }

    private void menuAbastecer() throws IOException, ClassNotFoundException {
        in.nextLine();
        System.out.println("Introduza nome da ferramenta");
        String nome = in.nextLine();
        System.out.println("Introduza quantidade");
        int quantidade = Integer.parseInt(in.nextLine());
        this.grt.supply(nome, quantidade);
        System.out.println("Abastecido");
        menuUtilizador();
    }

    private void menuCriaTareda() throws IOException, ClassNotFoundException {
        in.nextLine();
        System.out.println("Introduza nome da tarefa");
        String nome = in.nextLine();
        boolean b = this.grt.naoExisteTarefa(nome);
        if (b) {
            HashMap<String, Integer> fNec = new HashMap<>();
            System.out.println("Introduza nome da ferramenta");
            String ferramenta = in.nextLine();
            System.out.println("Introduza a quantidade");
            int quantidade = Integer.parseInt(in.nextLine());
            fNec.put(ferramenta, quantidade);
            System.out.println("Deseja introduzir nova ferramenta? y/n");
            while (in.nextLine().equals("y")) {
                System.out.println("Introduza nome da ferramenta");
                ferramenta = in.nextLine();
                System.out.println("Introduza a quantidade");
                quantidade = Integer.parseInt(in.nextLine());
                System.out.println("Deseja introduzir nova ferramenta? y/n");
                fNec.put(ferramenta, quantidade);
            }
            Iterator<String> it = fNec.keySet().iterator();
            while (it.hasNext() && b) {
                String nomef = it.next();
                quantidade = fNec.get(nomef);
                b = this.grt.armazem.containsKey(nomef);
            }
            if (b) {
                Tarefa t = new Tarefa(nome, this.grt.armazem, fNec);
                this.grt.inserirTarefa(t);
                System.out.println("Criada");
            } else {               
                System.err.println("Impossivel adicionar tarefa");
            }
        } else {
            System.err.println("Impossivel adicionar tarefa");
        }
        
        menuUtilizador();

    }

    private void menuRealizarTarefa() throws IOException, ClassNotFoundException {
        in.nextLine();
        System.out.println("Introduza o nome da tarefa");
        String nome = in.nextLine();
        boolean b = this.grt.naoExisteTarefa(nome);
        if (b) {
            System.err.println("Tarefa inexistente");
            menuUtilizador();

        } else {
            try {
                int idTarefa = this.grt.realizaTarefa(nome);
                System.out.println("Tarefa realizada com o id: " + idTarefa);
                menuUtilizador();
            } catch (InterruptedException ex) {
                Logger.getLogger(AtendimentoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void menuConcluir() throws IOException, ClassNotFoundException {
        in.nextLine();
        System.out.println("Introduza o id da Tarefa");
        int id = Integer.parseInt(in.nextLine());
        boolean b = this.grt.existeIdTarefa(id);
        if (!b) {
            System.err.println("Id da Tarefa incorrecto");
            in.reset();
            menuUtilizador();

        } else {
            this.grt.concluirTarefa(id);
            System.out.println("Tarefa concluida");
            menuUtilizador();

        }

    }

    private void menuSerNotificado() throws IOException, ClassNotFoundException {
        ArrayList<Integer> ids = new ArrayList<>();
        in.nextLine();
        System.out.println("Introduza id da tarefa");
        int id = Integer.parseInt(in.nextLine());
        ids.add(id);
        System.out.println("Deseja introduzir nova tarefa? y/n");
        while (in.nextLine().equals("y")) {
            System.out.println("Introduza id da tarefa");
            id = Integer.parseInt(in.nextLine());
            ids.add(id);
            System.out.println("Deseja introduzir nova tarefa? y/n");
        }
        for (int i = 0; i < ids.size(); i++) {
            id = ids.get(i);
            if (this.grt.getTarefasEmCurso().containsKey(id)) {
                try {
                    this.grt.notificarTarefa(id);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AtendimentoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        System.out.println("Todas as tarefas concluidas");
        menuUtilizador();

    }

    private void menuListaTarefas() throws IOException, ClassNotFoundException {
        in.nextLine();
        ArrayList<String> mensagem = new ArrayList<>();
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
        for (String s : mensagem) {
            System.out.println(s);
        }
        menuUtilizador();

    }
}
