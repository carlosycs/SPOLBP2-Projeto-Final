package com.ifsp.projeto3bim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tarefa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String data;    
    private String status; 

    public Tarefa() { } 

    public Tarefa(String nome, String data, String status) {
        this.nome = nome;
        this.data = data;
        this.status = status;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void toggleStatus() {
        if ("Completed".equalsIgnoreCase(this.status)) {
            this.status = "Pending";
        } else {
            this.status = "Completed";
        }
    }
}