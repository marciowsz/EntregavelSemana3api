package com.escola.api.service;

import com.escola.api.dto.AlunoRequest;
import com.escola.api.dto.AlunoResponse;
import com.escola.api.entity.Aluno;
import com.escola.api.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    // Injeção de dependência via construtor
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    // CREATE: AlunoRequest -> Entity -> salva -> AlunoResponse
    public AlunoResponse create(AlunoRequest request) {
        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setRa(request.getRa());
        aluno.setEmail(request.getEmail());

        Aluno salvo = alunoRepository.save(aluno);
        return new AlunoResponse(salvo);
    }

    // READ (por id): retorna o DTO, ou null se o id não existir
    public AlunoResponse getById(Long id) {
        Optional<Aluno> optional = alunoRepository.findById(id);

        if (optional.isPresent()) {
            Aluno aluno = optional.get();
            return new AlunoResponse(aluno);
        } else {
            return null;
        }
    }

    // READ (todos): converte cada Entity em DTO com um for tradicional
    public List<AlunoResponse> getAll() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoResponse> respostas = new ArrayList<>();

        for (Aluno aluno : alunos) {
            respostas.add(new AlunoResponse(aluno));
        }
        return respostas;
    }

    // UPDATE: retorna true se atualizou, false se o id não existir
    public boolean update(Long id, AlunoRequest request) {
        Optional<Aluno> optional = alunoRepository.findById(id);

        if (optional.isPresent()) {
            Aluno aluno = optional.get();
            aluno.setNome(request.getNome());
            aluno.setRa(request.getRa());
            aluno.setEmail(request.getEmail());
            alunoRepository.save(aluno);
            return true;
        } else {
            return false;
        }
    }

    // DELETE: verifica se existe antes de remover
    public boolean delete(Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
