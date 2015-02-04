package SdProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {

    private static final String ip = "localhost";
    private static final int port = 55000;
    private static Scanner in = new Scanner(System.in);
    private static String username;

    private static Socket s = null;
    private static ObjectOutputStream out;
    private static ArrayList<String> mensagem;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        s = new Socket(ip, port);
        out = new ObjectOutputStream(s.getOutputStream());
        menuPrincipal();

    }

    public static void menuPrincipal() throws IOException, ClassNotFoundException {
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
                mensagem = new ArrayList<>();
                mensagem.add("Registar");
                mensagem.add(user);
                mensagem.add(pass);

                out.writeObject(mensagem);
                out.flush();
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String result = socketInput.readLine();

                if (result.equals("Reg")) {
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

                    mensagem = new ArrayList<>();
                    mensagem.add("Login");
                    mensagem.add(user);
                    mensagem.add(pass);

                    out.writeObject(mensagem);
                    out.flush();

                    BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    String result = null;

                    result = socketInput.readLine();

                    if (result.equals("Entrou")) {
                        System.out.println(result);
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
                        s.shutdownInput();
                        s.shutdownOutput();
                        s.close();
                        System.exit(0);                        
                    } else {
                        System.out.println("Opção inválida!");
                        menuInicial();
                    }
                }
            }

        }
    }

    public static String menuInicial() {
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

    public static void menuUtilizador() throws IOException, ClassNotFoundException {
        
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

    private static void menuAbastecer() throws IOException, ClassNotFoundException {
        in.nextLine();
        System.out.println("Introduza nome da ferramenta");
        String nome = in.nextLine();
        System.out.println("Introduza quantidade");
        String quantidade = in.nextLine();

        mensagem = new ArrayList<>();
        mensagem.add("Abastecer");
        mensagem.add(nome);
        mensagem.add(quantidade);

        out.writeObject(mensagem);
        out.flush();

        BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String result = socketInput.readLine();
        System.out.println(result);
        menuUtilizador();

    }

    private static void menuCriaTareda() throws IOException, ClassNotFoundException {
        mensagem = new ArrayList<>();
        mensagem.add("Criar");
        in.nextLine();
        System.out.println("Introduza nome da tarefa");
        String nome = in.nextLine();
        mensagem.add(nome);
        System.out.println("Introduza nome da ferramenta");
        String ferramenta = in.nextLine();
        mensagem.add(ferramenta);
        System.out.println("Introduza a quantidade");
        String quantidade = in.nextLine();
        mensagem.add(quantidade);
        System.out.println("Deseja introduzir nova ferramenta? y/n");
        while (in.nextLine().equals("y")) {
            System.out.println("Introduza nome da ferramenta");
            ferramenta = in.nextLine();
            mensagem.add(ferramenta);
            System.out.println("Introduza a quantidade");
            quantidade = in.nextLine();
            mensagem.add(quantidade);
            System.out.println("Deseja introduzir nova ferramenta? y/n");

        }

        out.writeObject(mensagem);
        out.flush();

        BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String result = socketInput.readLine();
        if (result.equals("Erro")) {
            System.err.println("Impossivel adicionar tarefa");
        }
        else{
            System.out.println(result);
        }

        menuUtilizador();

    }

    private static void menuRealizarTarefa() throws IOException, ClassNotFoundException {
        mensagem = new ArrayList<>();
        mensagem.add("Realizar");
        in.nextLine();
        System.out.println("Introduza o nome da tarefa");
        String nome = in.nextLine();
        
        mensagem.add(nome);
        out.writeObject(mensagem);
        out.flush();
        
        BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String result = socketInput.readLine();
        
        if(result.equals("Erro")){
            System.err.println("Tarefa inexistente");
            menuUtilizador();
            
        }
        else{
            System.out.println("Tarefa realizada com o id: "+result);
            menuUtilizador();
            
        }
    }

    private static void menuConcluir() throws IOException, ClassNotFoundException {
        mensagem = new ArrayList<>();
        mensagem.add("Concluir");
        in.nextLine();
        System.out.println("Introduza o id da Tarefa");
        String id = in.nextLine();
        mensagem.add(id);
        
        out.writeObject(mensagem);
        out.flush();
        
        BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String result = socketInput.readLine();
        
        if(result.equals("Erro")){
            System.err.println("Id da Tarefa incorrecto");
            out.flush();
            menuUtilizador();
            
        }
        else{            
            System.out.println("Tarefa concluida");
            out.flush();
            menuUtilizador();
            
        }
        
        
    }

    private static void menuSerNotificado() throws IOException, ClassNotFoundException {
        mensagem = new ArrayList<>();
        mensagem.add("Notificar");
        in.nextLine();
        System.out.println("Introduza id da tarefa");
        String id = in.nextLine();
        mensagem.add(id);
        System.out.println("Deseja introduzir nova tarefa? y/n");
        while (in.nextLine().equals("y")) {
            System.out.println("Introduza id da tarefa");
            id = in.nextLine();
            mensagem.add(id);
            System.out.println("Deseja introduzir nova tarefa? y/n");

        }

        out.writeObject(mensagem);
        out.flush();

        BufferedReader socketInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String result = socketInput.readLine();
        
       
            System.out.println("Todas as tarefas concluidas");

        menuUtilizador();
        
        

        
        
    }

    private static void menuListaTarefas() throws IOException, ClassNotFoundException {
        mensagem = new ArrayList<>();
        mensagem.add("Lista");
        in.nextLine();
        out.writeObject(mensagem);
        out.flush();
        
        ObjectInputStream inn=new ObjectInputStream(s.getInputStream());
        
        ArrayList<String> input = (ArrayList<String>) inn.readObject();
        
        for (String input1 : input) {
            System.out.println(input1);
        }
        menuUtilizador();
        
        
        
        
    }

}
