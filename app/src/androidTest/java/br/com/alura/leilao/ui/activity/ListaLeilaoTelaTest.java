package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.TestWebClient;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoTelaTest {

    @Rule
    public ActivityTestRule activity =
            new ActivityTestRule(ListaLeilaoActivity.class, true, false);
    private final TestWebClient webClient = new TestWebClient();

    @Before
    public void setup() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        activity.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withText("Carro"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activity.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withText("Carro"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withText("Computador"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
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