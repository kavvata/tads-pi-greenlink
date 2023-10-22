package ifpr.pgua.eic.greenlink.models.entities;

import java.time.LocalDate;

public class Tarefa {
    private int id;
    private String nome;
    private String descricao;
    private Planta planta;
    private LocalDate prazo;
    private boolean feito;

    public Tarefa(int id, String nome, String descricao, Planta planta, LocalDate prazo, boolean feito) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.planta = planta;
        this.prazo = prazo;
        this.feito = feito;
    }

    public Tarefa(String nome, String descricao, Planta planta, LocalDate prazo) {
        this.nome = nome;
        this.descricao = descricao;
        this.planta = planta;
        this.prazo = prazo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public boolean isFeito() {
        return feito;
    }

    public void setFeito(boolean feito) {
        this.feito = feito;
    }

    public String toString() {
        return nome;
    }
}
