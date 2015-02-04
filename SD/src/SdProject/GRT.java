package SdProject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GRT {

    private HashMap<String, Utilizador> utilizadores;
    protected HashMap<String, Ferramenta> armazem;
    private HashMap<String, Condition> condFerramentas;
    protected HashMap<Integer, Condition> condTarefas;
    private HashMap<String, Tarefa> tarefas;
    private HashMap<Integer, Tarefa> tarefasEmCurso;
    private int contador;

    private Lock l = new ReentrantLock();
    //private Condition cond = l.newCondition();

    public GRT() {
        this.utilizadores = new HashMap<>();
        this.armazem = new HashMap<>();
        this.condFerramentas = new HashMap<>();
        this.tarefas = new HashMap<>();
        this.tarefasEmCurso = new HashMap<>();
        this.condTarefas = new HashMap<>();
        this.contador = 0;

    }

    public Map<String, Ferramenta> getArmazem() {
        return this.armazem;
    }

    public Map<Integer, Condition> getCondTarefas() {
       
        return this.condTarefas;
       
    }

    public Map<String, Tarefa> getTarefas() {
        return this.tarefas;
    }

    public Map<Integer, Tarefa> getTarefasEmCurso() {
        return this.tarefasEmCurso;
    }

    public int realizaTarefa(String nomeTarefa) throws InterruptedException {
        l.lock();
        try {
            Condition c = l.newCondition();           
            this.consume(nomeTarefa);
            this.contador++;
            Tarefa t = this.tarefas.get(nomeTarefa);
            this.tarefasEmCurso.put(contador, t);
            this.condTarefas.put(contador, c);
        } finally {
            l.unlock();
        }
        return this.contador;
    }

    public HashMap<String, Utilizador> getUtilizadores() {
        return this.utilizadores;
    }

    public void setUtilizadores(HashMap<String, Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public boolean registaUtilizador(String username, String pass) {
        boolean res = false;
        l.lock();
        try {
            if (!this.utilizadores.containsKey(username)) {
                Utilizador u = new Utilizador(username, pass);
                this.utilizadores.put(username, u);
                res = true;
            }
        } finally {
            l.unlock();
        }
        return res;
    }

    public void addTarefa(Tarefa t) {
        this.tarefas.put(t.getNome(), t);
    }

    public void supply(String nome, int quantidade) {
        l.lock();
        try {
            if (!armazem.containsKey(nome)) {
                Ferramenta f = new Ferramenta(nome, quantidade);
                armazem.put(nome, f);
                Condition c = l.newCondition();
                this.condFerramentas.put(nome, c);

            } else {
                Ferramenta f = this.armazem.get(nome);
                f.addQuantidade(quantidade);
                this.armazem.put(nome, f);

            }
        } finally {
            this.condFerramentas.get(nome).signalAll();
            l.unlock();
        }

    }

    public void consume(String nomeT) throws InterruptedException { 
        l.lock();
        try {
            boolean flag = true;

            Tarefa t = this.tarefas.get(nomeT);
            while (flag) {
                flag = false;
                for (String s : t.getFerramentas().keySet()) {
                    int i = this.armazem.get(s).getQuantidade();
                    if (i < t.getFerramentas().get(s)) {
                        this.condFerramentas.get(s).await();
                        flag = true;
                    }

                }
            }
            for (String s : t.getFerramentas().keySet()) {
                this.armazem.get(s).remQuantidade(t.getFerramentas().get(s));

            }
        } finally {
            l.unlock();
        }
    }

    boolean naoExisteTarefa(String nomeTarefa) {
        return !this.tarefas.containsKey(nomeTarefa);
    }

    boolean existeIdTarefa(int idTarefa) {
        return this.tarefasEmCurso.containsKey(idTarefa);
    }

    void concluirTarefa(int idTarefa) {
        l.lock();
        try {

            Tarefa t = this.tarefasEmCurso.remove(idTarefa);
            this.condTarefas.get(idTarefa).signalAll();
            this.condTarefas.remove(idTarefa);
            

            for (String f : t.getFerramentas().keySet()) {
                int quantidade = t.getFerramentas().get(f);
                this.supply(f, quantidade);
            }
        } finally {
            l.unlock();
        }
    }

    void notificarTarefa(int idTarefa) throws InterruptedException {
        l.lock();
        try {
            this.condTarefas.get(idTarefa).await();
        } finally {
            l.unlock();
        }
    }

    void inserirTarefa(Tarefa t) {
        this.tarefas.put(t.getNome(), t);
    }

}
