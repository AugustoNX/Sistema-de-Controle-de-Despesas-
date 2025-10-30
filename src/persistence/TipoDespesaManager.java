package persistence;

import model.TipoDespesa;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TipoDespesaManager {
    private final File file;
    private final Map<Long, TipoDespesa> mapa = new LinkedHashMap<>();
    private long nextId = 1;

    public TipoDespesaManager(String path) {
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
                String[] p = l.split("\\|", -1);
                if (p.length < 2) continue;
                long id = Long.parseLong(p[0]);
                String nome = p[1];
                TipoDespesa t = new TipoDespesa(id, nome);
                mapa.put(id, t);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar tipos: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (TipoDespesa t : mapa.values()) pw.println(t.serialize());
        } catch (IOException e) {
            System.err.println("Erro ao salvar tipos: " + e.getMessage());
        }
    }

    public TipoDespesa criar(String nome) {
        TipoDespesa t = new TipoDespesa(nextId++, nome);
        mapa.put(t.getId(), t);
        save();
        return t;
    }

    public boolean editar(long id, String novoNome) {
        TipoDespesa t = mapa.get(id);
        if (t == null) return false;
        t.setNome(novoNome);
        save();
        return true;
    }

    public boolean excluir(long id) {
        if (mapa.remove(id) != null) {
            save();
            return true;
        }
        return false;
    }

    public List<TipoDespesa> listar() {
        return new ArrayList<>(mapa.values());
    }

    public TipoDespesa buscarPorNome(String nome) {
        for (TipoDespesa t : mapa.values()) if (t.getNome().equalsIgnoreCase(nome)) return t;
        return null;
    }
}
