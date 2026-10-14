package com.escola.api.dto;

/**
 * DTO de ENTRADA: dados que o cliente envia no corpo do POST e do PUT.
 * Não tem "id": quem gera o id é o banco, e não o cliente.
 */
public class AlunoRequest {

    private String nome;
    private String ra;
    private String email;

    public AlunoRequest() {
    }

    public AlunoRequest(String nome, String ra, String email) {
        this.nome = nome;
        this.ra = ra;
        this.email = email;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
