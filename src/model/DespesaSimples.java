package model;

public class DespesaSimples extends Despesa {

    public DespesaSimples(long id, String descricao, double valor, String dataVencimento, String categoria,
                          boolean paga, String dataPagamento, double valorPago) {
        super(id, descricao, valor, dataVencimento, categoria, paga, dataPagamento, valorPago);
    }

    @Override
    public String serialize() {
        // id|descricao|valor|dataVencimento|categoria|paga|dataPagamento|valorPago
        return String.format("%d|%s|%.2f|%s|%s|%b|%s|%.2f",
                id, descricao.replace("|"," "), valor, dataVencimento, categoria.replace("|"," "),
                paga, dataPagamento, valorPago);
    }
}
