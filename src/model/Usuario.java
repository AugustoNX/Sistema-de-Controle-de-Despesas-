package model;

public class Usuario {
    private long id;
    private String nome;
    private String login;
    private String senhaHash; // SHA-256 hex

    public Usuario(long id, String nome, String login, String senhaHash) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senhaHash = senhaHash;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }

    public void setNome(String nome) { this.nome = nome; }
    public void setLogin(String login) { this.login = login; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String serialize() {
        return String.format("%d|%s|%s|%s", id, nome.replace("|"," "), login.replace("|"," "), senhaHash);
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + login + ")";
    }
}
