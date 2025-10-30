package model;

public class TipoDespesa {
    private long id;
    private String nome;

    public TipoDespesa(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String serialize() {
        return String.format("%d|%s", id, nome.replace("|"," "));
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
