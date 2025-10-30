package persistence;

import model.Despesa;
import model.DespesaSimples;
import model.DespesaAlimentacao;
import util.Utils;

import java.io.*;
import java.util.*;

public class DespesaManager {
    private final File file;
    private final Map<Long, Despesa> mapa = new LinkedHashMap<>();
    private long nextId = 1;

    public DespesaManager(String path) {
        this.file = new File(path);
        load();
    }

    private void load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            List<String> lines = Utils.readAllLines(file);
            for (String l : lines) {
                if (l.trim().isEmpty()) continue;
                // id|descricao|valor|dataVencimento|categoria|paga|dataPagamento|valorPago
                String[] parts = l.split("\\|", -1);
                if (parts.length < 8) continue;
                long id = Long.parseLong(parts[0]);
                String descricao = parts[1];
                double valor = Double.parseDouble(parts[2]);
                String dataVenc = parts[3];
                String categoria = parts[4];
                boolean paga = Boolean.parseBoolean(parts[5]);
                String dataPag = parts[6];
                double valorPago = Double.parseDouble(parts[7]);
                Despesa d;
                if ("Alimentação".equalsIgnoreCase(categoria)) {
                    d = new DespesaAlimentacao(id, descricao, valor, dataVenc, paga, dataPag, valorPago);
                } else {
                    d = new DespesaSimples(id, descricao, valor, dataVenc, categoria, paga, dataPag, valorPago);
                }
                mapa.put(id, d);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar despesas: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (Despesa d : mapa.values()) pw.println(d.serialize());
        } catch (IOException e) {
            System.err.println("Erro ao salvar despesas: " + e.getMessage());
        }
    }

    public Despesa criarDespesa(String descricao, double valor, String dataVencimento, String categoria) {
        Despesa d = new DespesaSimples(nextId++, descricao, valor, dataVencimento, categoria, false, "", 0.0);
        mapa.put(d.getId(), d);
        save();
        return d;
    }

    public boolean editarDespesa(long id, String descricao, double valor, String dataVencimento, String categoria) {
        Despesa d = mapa.get(id);
        if (d == null) return false;
        d.setDescricao(descricao);
        d.setValor(valor);
        d.setDataVencimento(dataVencimento);
        d.setCategoria(categoria);
        save();
        return true;
    }

    public boolean excluirDespesa(long id) {
        if (mapa.remove(id) != null) {
            save();
            return true;
        }
        return false;
    }

    public boolean registrarPagamento(long id, String dataPagamento, double valorPago) {
        Despesa d = mapa.get(id);
        if (d == null) return false;
        d.registrarPagamento(dataPagamento, valorPago);
        save();
        return true;
    }

    public List<Despesa> listarTodas() {
        return new ArrayList<>(mapa.values());
    }

    public List<Despesa> listarPorStatus(boolean paga) {
        List<Despesa> out = new ArrayList<>();
        for (Despesa d : mapa.values()) if (d.isPaga() == paga) out.add(d);
        return out;
    }

    public List<Despesa> listarPorCategoria(String categoria) {
        List<Despesa> out = new ArrayList<>();
        for (Despesa d : mapa.values()) if (d.getCategoria().equalsIgnoreCase(categoria)) out.add(d);
        return out;
    }

    public Despesa buscarPorId(long id) {
        return mapa.get(id);
    }
}
