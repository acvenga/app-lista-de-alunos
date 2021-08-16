package com.example.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.model.Aluno;
import com.example.agenda.ui.dao.AlunoDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

public class ListaAlunosActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Lista de Alunos";

    private final AlunoDAO dao = new AlunoDAO();
    private ArrayAdapter<Aluno> adapter; //transformar o adapter num atributo de classe para que todos os métodos o acessem

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //este método deve ser sempre chamado
        setContentView(R.layout.activity_lista_alunos);
        setTitle(TITULO_APPBAR);
        configuraFabNovoAluno();
        configuraLista(); //deve ser feito apenas uma vez para funcionamento ótimo
        dao.salva(new Aluno("Alex", "48399283", "Alex@gmail.com"));
        dao.salva(new Aluno("Ana", "48399283", "Ana@gmail.com"));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.activity_lista_alunos_menu, menu);
        //inflar: pegar o arquivo do menu e vincular à activity transformando esse arquvio em objeto de menu (inflando)
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_lista_alunos_menu_remover){
            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); //entender isso aqui
            Aluno alunoEscolhido = adapter.getItem(menuInfo.position);
            remove(alunoEscolhido);
        }
        return super.onContextItemSelected(item);
    }//qqr tipo de menu de contexto selecionado chama este método

    private void configuraFabNovoAluno() {
        FloatingActionButton botaoNovoAluno = findViewById(R.id.activity_lista_alunos_fab_novo_aluno);
        botaoNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreFormularioModoInsereAluno();
            }

            private void abreFormularioModoInsereAluno() {
                startActivity(new Intent(ListaAlunosActivity.this,
                        FormularioAlunoActivity.class)); //dessa maneira se inicia uma ativity a partir de outra
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaAlunos();

    }

    private void atualizaAlunos() {
        adapter.clear(); //on resume, os dados estrarao desatualizados, portando deve-se limpá-los
        adapter.addAll(dao.todos()); //adicionar todos os elemnentos do dao, que virão de forma atualizada
    }

    private void configuraLista() {
        ListView listaDeAlunos = findViewById(R.id.activity_lista_alunos_listview);
        final List<Aluno> alunos = dao.todos(); //cria uma variavel em array para salvar a lista de alunos devolvida pelo dao
        configuraAdapter(listaDeAlunos);
        configuraListenerDeCliquePorItem(listaDeAlunos);
        //configuraListenerDeCliqueLongoPorItem(listaDeAlunos);
        registerForContextMenu(listaDeAlunos); //espera que envie como argumento uma view dentro do layout
    }

/*    private void configuraListenerDeCliqueLongoPorItem(ListView listaDeAlunos) {
        listaDeAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) { //clique longo para remover
                Aluno alunoEscolhido = (Aluno) adapterView.getItemAtPosition(position); //acessa a variável criada para salvar a lista do dao e busca sua posição na array.
                remove(alunoEscolhido);
                return false; // com true, indica que vai consumir todo este evento e nao passar pra frente, com false passa para frente
            }
        });
    }*/

    private void remove(Aluno aluno) {
        dao.remove(aluno);
        adapter.remove(aluno); //realiza a atualização apenas das informaçoes contidas no adapter
    }

    private void configuraListenerDeCliquePorItem(ListView listaDeAlunos) {
        listaDeAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Aluno alunoEscolhido = (Aluno) adapterView.getItemAtPosition(position); //acessa a variável criada para salvar a lista do dao e busca sua posição na array.
                abreFormularioModoEditaAluno(alunoEscolhido);      //salva em um novo aluno que devolve o atributo nome deste aluno escolhido com o clique.
            }
        });
    }

    private void abreFormularioModoEditaAluno(Aluno aluno) {
        Intent vaiParaFormularioActivity = new Intent(ListaAlunosActivity.this,
                FormularioAlunoActivity.class);
        vaiParaFormularioActivity.putExtra(CHAVE_ALUNO, aluno); //dados que estaremos enviando via extra para a outra activity
        startActivity(vaiParaFormularioActivity); //faz com que ao clicar um item, abra-se uma nova activity, neste caso o formulário
    }

    private void configuraAdapter(ListView listaDeAlunos) {
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1);
        listaDeAlunos.setAdapter(adapter); //preciso entender melhor este método
    }

    ;

};