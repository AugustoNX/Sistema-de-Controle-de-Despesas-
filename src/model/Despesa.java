package model;

public abstract class Despesa implements Pagavel {
    protected long id;
    protected String descricao;
    protected double valor;
    protected String dataVencimento; // formato simples: YYYY-MM-DD
    protected String categoria;
    protected boolean paga;
    protected String dataPagamento; // pode ser vazio
    protected double valorPago;

    public Despesa(long id, String descricao, double valor, String dataVencimento, String categoria,
                   boolean paga, String dataPagamento, double valorPago) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.categoria = categoria;
        this.paga = paga;
        this.dataPagamento = dataPagamento == null ? "" : dataPagamento;
        this.valorPago = valorPago;
    }

    public long getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public String getDataVencimento() { return dataVencimento; }
    public String getCategoria() { return categoria; }
    public boolean isPaga() { return paga; }
    public String getDataPagamento() { return dataPagamento; }
    public double getValorPago() { return valorPago; }

    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(double valor) { this.valor = valor; }
    public void setDataVencimento(String dataVencimento) { this.dataVencimento = dataVencimento; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public void registrarPagamento(String dataPagamento, double valorPago) {
        this.paga = true;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
    }

    public abstract String serialize(); // gravação em arquivo
    public static String csvHeader() { return "id|descricao|valor|dataVencimento|categoria|paga|dataPagamento|valorPago"; }

    @Override
    public String toString() {
        return String.format("ID:%d | %s | R$%.2f | Venc: %s | Cat: %s | %s",
                id, descricao, valor, dataVencimento, categoria, paga ? "PAGA" : "PENDENTE");
    }
}
