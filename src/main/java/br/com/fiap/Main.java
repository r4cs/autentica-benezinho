package br.com.fiap;

import br.com.fiap.authentication.model.Profile;
import br.com.fiap.authentication.model.Role;
import br.com.fiap.authentication.model.User;
import br.com.fiap.pessoa.model.PessoaFisica;
import br.com.fiap.pessoa.model.PessoaJuridica;
import br.com.fiap.pessoa.model.Sexo;
import br.com.fiap.sistema.model.Sistema;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracle");
        EntityManager manager = factory.createEntityManager();

        var bene = new PessoaFisica();

        bene.setCPF(geraCpf())
                .setSexo(Sexo.MASCULINO)
                .setNome("Benefrancis do Nascimento")
                .setNascimento(LocalDate.of(1977, 3, 8));

        var holding = new PessoaJuridica();
        holding.addSocio(bene)
                .setCNPJ(geraCNPJ())
                .setNome("Holding Benezinho")
                .setNascimento(LocalDate.now().minusYears(new Random().nextInt(99)));


        Sistema bank = new Sistema("Banco Benezinho", "BBANC");
        bank.addResponsavel(holding);

        Role abrirCaixaBanco = new Role();
        abrirCaixaBanco.setSistema(bank)
                .setNome("OPEN_CAIXA")
                .setDescricao("Abrir o caixa do Banco");

        Role fecharCaixaBanco = new Role();
        fecharCaixaBanco.setSistema(bank)
                .setNome("CLOSE_CAIXA")
                .setDescricao("Fechar o caixa do Banco");

        Profile gerenteBancario = new Profile();
        gerenteBancario.setNome("GERENTE_BANCARIO")
                .addRole(abrirCaixaBanco)
                .addRole(fecharCaixaBanco);


        Sistema mercado = new Sistema("Supermercados Benezinho", "BMARCK");
        mercado.addResponsavel(holding);

        Role abrirCaixaMercado = new Role();
        abrirCaixaMercado.setSistema(mercado)
                .setNome("OPEN_CAIXA")
                .setDescricao("Abrir o caixa do Mercado");

        Role fecharCaixaMercado = new Role();
        fecharCaixaMercado.setSistema(mercado)
                .setNome("CLOSE_CAIXA")
                .setDescricao("Fechar o caixa do Mercado");

        Profile gerenteDeMercado = new Profile();
        gerenteDeMercado.setNome("GERENTE_DE_MERCADO")
                .addRole(fecharCaixaMercado)
                .addRole(abrirCaixaMercado);

        User benefrancis = new User();
        benefrancis.setPessoa(bene)
                .setEmail("benefrancis@holding.com")
                .setPassword("root")
                .addPerfil(gerenteBancario)
                .addPerfil(gerenteDeMercado);


        try {
            manager.getTransaction().begin();
            manager.persist(benefrancis);
            manager.getTransaction().commit();


            //Métodos para consultar aqui:



        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    """
                            Erro na persistência!

                            Confira se todas as classes estão anotadas corretamente!

                            veja detalhes no console..."""

            );
            e.printStackTrace();
        } finally {
            manager.close();
            factory.close();
            System.out.println(benefrancis);
        }

    }

    private static String geraCpf() {
        var sorteio = new Random();
        var digito = sorteio.nextLong(99);
        var numero = sorteio.nextLong(999999999);
        var cpf = String.valueOf(numero) + "-" + String.valueOf(digito);
        return cpf;
    }

    private static String geraCNPJ() {
        var sorteio = new Random();
        var digito = sorteio.nextLong(99);
        var numero = sorteio.nextLong(999999999);
        var cpf = String.valueOf(numero) + "/0001-" + String.valueOf(digito);
        return cpf;
    }
}