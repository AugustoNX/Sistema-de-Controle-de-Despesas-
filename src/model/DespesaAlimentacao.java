package model;

public class DespesaAlimentacao extends DespesaSimples {
    public DespesaAlimentacao(long id, String descricao, double valor, String dataVencimento,
                              boolean paga, String dataPagamento, double valorPago) {
        super(id, descricao, valor, dataVencimento, "Alimentação", paga, dataPagamento, valorPago);
    }
}
