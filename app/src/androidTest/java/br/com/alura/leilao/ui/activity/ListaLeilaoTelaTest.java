package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.TestWebClient;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoTelaTest {

    @Rule
    public ActivityTestRule activity =
            new ActivityTestRule(ListaLeilaoActivity.class, true, false);

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {

        TestWebClient webClient = new TestWebClient();

        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();

        if (bancoDeDadosNaoFoiLimpo){
            Assert.fail("Banco de dados não foi limpo");
        }

        Leilao carroSalvo = webClient.salva(new Leilao("Carro"));
        if (carroSalvo == null){
            Assert.fail("Leilão não foi salvo");
        }

        activity.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withText("Carro"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesNaApi() throws IOException {

        TestWebClient webClient = new TestWebClient();

        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();

        if (bancoDeDadosNaoFoiLimpo){
            Assert.fail("Banco de dados não foi limpo");
        }

        Leilao carroSalvo = webClient.salva(new Leilao("Carro"));
        Leilao computadorSalvo = webClient.salva(new Leilao("Computador"));
        if (carroSalvo == null || computadorSalvo == null){
            Assert.fail("Leilão não foi salvo");
        }

        activity.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withText("Carro"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withText("Computador"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


}