package ifpr.pgua.eic.greenlink.models.entities;

public class Planta {
    private int id;
    private String nome;
    private String descricao;
    private Jardim jardim;

    public Planta(int id, String nome, String descricao, Jardim jardim) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.jardim = jardim;
    }

    public Planta(String nome, String descricao, Jardim jardim) {
        this.nome = nome;
        this.descricao = descricao;
        this.jardim = jardim;
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

    public Jardim getJardim() {
        return jardim;
    }

    public void setJardim(Jardim jardim) {
        this.jardim = jardim;
    }

    public String toString() {
        return nome;
    }
}