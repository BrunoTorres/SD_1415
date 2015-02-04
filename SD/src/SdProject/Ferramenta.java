package SdProject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Ferramenta {
    private String nome;
    private int quantidade;
    private Lock l = new ReentrantLock();

    public Ferramenta(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        l.lock();
        try {
            this.nome = nome;
        } finally {
            l.unlock();
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        l.lock();
        try {
            this.quantidade = quantidade;
        } finally {
            l.unlock();
        }
    }

    void addQuantidade(int quantidade) {
        l.lock();
        try {
            this.quantidade += quantidade;
        } finally {
            l.unlock();
        }
       
    }

    void remQuantidade(int quantidade) {
         l.lock();
        try {
            this.quantidade -= quantidade;
        } finally {
            l.unlock();
        }
    }
    
    

}
