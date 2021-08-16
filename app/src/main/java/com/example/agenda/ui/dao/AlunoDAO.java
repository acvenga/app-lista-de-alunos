package com.example.agenda.ui.dao;

import com.example.agenda.model.Aluno;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private final static List<Aluno> alunos = new ArrayList<>();
    private static int contadorDeIds = 1;

    public List<Aluno> todos() {
        return new ArrayList<>(alunos);
    }

    public void salva(Aluno aluno) {
        aluno.setId(contadorDeIds);
        alunos.add(aluno);
        atualizaIds();
    }

    private void atualizaIds() {
        contadorDeIds++;
    }

    public void edita(Aluno aluno) {
        Aluno alunoEncontrado = buscaAlunoPeloId(aluno);
        if (alunoEncontrado != null) { //se o aluno foi encontrado, ele deixa de ser null e precisamos substituir esse aluno a partir da posição dele
            int posicaoDoAluno = alunos.indexOf(alunoEncontrado); //o primeiro passo é encontrar a posição
            alunos.set(posicaoDoAluno, aluno); //fazer a substituição colocando o aluno que veio de fora (parâmetro)
        }
    }

    @Nullable
    private Aluno buscaAlunoPeloId(Aluno aluno) {
        for (Aluno a :                      //passar por cada aluno contido na lista
                alunos) {
            if (a.getId() == aluno.getId()) { //verificar se o id do a é o mesmo que o id enviado pelo parâmetro
                return a; //se for igual, ele vira o aluno encontrado
            }
        }
        return null;
    }

    public void remove(Aluno aluno) {
        Aluno alunoDevolvido = buscaAlunoPeloId(aluno);
        if (alunoDevolvido != null){
            alunos.remove(alunoDevolvido);
        }
    }

}
