package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.api.retrofit.client.TestWebClient;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class ListaLeilaoTelaTest {

    @Rule
    public ActivityTestRule activity =
            new ActivityTestRule(ListaLeilaoActivity.class, true, false);
    private final TestWebClient webClient = new TestWebClient();
    private final FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();

    @Before
    public void setup() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        activity.launchActivity(new Intent());

        onView(allOf(ViewMatchers.withText("Carro"),
                withId(R.id.item_leilao_descricao)))
                .check(matches(ViewMatchers.isDisplayed()));

        String formatoEsperado = formatadorDeMoeda.formata(0.0);

        onView(allOf(ViewMatchers.withText(formatoEsperado),
                withId(R.id.item_leilao_maior_lance)))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activity.launchActivity(new Intent());


//        Espresso.onView(allOf(ViewMatchers.withText("Carro"),
//                withId(R.id.item_leilao_descricao)))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//        String formatoEsperadoCarro = formatadorDeMoeda.formata(0.0);
//
//        Espresso.onView(allOf(ViewMatchers.withText(formatoEsperadoCarro),
//                withId(R.id.item_leilao_maior_lance)))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//
//        Espresso.onView(allOf(ViewMatchers.withText("Computador"),
//                withId(R.id.item_leilao_descricao)))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//        String formatoEsperadoComputador = formatadorDeMoeda.formata(0.0);
//
//        Espresso.onView(allOf(ViewMatchers.withText(formatoEsperadoComputador),
//                withId(R.id.item_leilao_maior_lance)))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilao(0, "Carro", 0.00)));
    }

    private BoundedMatcher<View, RecyclerView> apareceLeilao(final int posicao,
                                                             final String descricaoEsperada,
                                                             final double maiorLanceEsperado) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                View viewDoViewHolder = item.findViewHolderForAdapterPosition(posicao).itemView;
                TextView textViewDescricao = viewDoViewHolder.findViewById(R.id.item_leilao_descricao);
                boolean temDescricaoEsperada = textViewDescricao.getText().toString().equals(descricaoEsperada);
                TextView textViewMaiorLance = viewDoViewHolder.findViewById(R.id.item_leilao_maior_lance);
                FormatadorDeMoeda formatador = new FormatadorDeMoeda();
                boolean temMaiorLanceEsperado = textViewMaiorLance.getText().toString().equals(formatador.formata(maiorLanceEsperado));
                return temDescricaoEsperada && temMaiorLanceEsperado;
            }
        };
    }

    @After
    public void tearDown() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    private void tentaSalvarLeilaoNaApi(Leilao ... leiloes) throws IOException {
        for (Leilao leilao: leiloes){

            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null){
                Assert.fail("Leilão não foi salvo" + leilao.getDescricao());
            }
        }
    }

    private void limpaBancoDeDadosDaApi() throws IOException {
        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();

        if (bancoDeDadosNaoFoiLimpo){
            Assert.fail("Banco de dados não foi limpo");
        }
    }
}