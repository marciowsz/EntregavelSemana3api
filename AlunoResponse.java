package com.escola.api.dto;

import com.escola.api.entity.Aluno;

/**
 * DTO de SAÍDA: o que a API devolve ao cliente.
 * Desacopla a resposta da Entity: se a Entity ganhar campos internos
 * (senha, datas de auditoria...), eles não vazam para a API.
 */
public class AlunoResponse {

    private Long id;
    private String nome;
    private String ra;
    private String email;

    public AlunoResponse() {
    }

    // Converte a Entity em DTO de resposta
    public AlunoResponse(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.ra = aluno.getRa();
        this.email = aluno.getEmail();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
