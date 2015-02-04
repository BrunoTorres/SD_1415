

package SdProject;

import java.util.HashMap;

public class Tarefa {
    private String nome;
    private HashMap<String, Ferramenta> armazem;
    private HashMap<String, Integer> ferramentas;

    public Tarefa(String nome, HashMap<String, Ferramenta> armazem, HashMap<String, Integer> ferramentas) {
        this.nome = nome;
        this.armazem = armazem;
        this.ferramentas = ferramentas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    

    public HashMap<String, Integer> getFerramentas() {
        return ferramentas;
    }

  
    
    
    
    
    

}
