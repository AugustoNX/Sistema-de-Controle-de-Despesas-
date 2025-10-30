package persistence;

import model.Usuario;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UsuarioManager {
    private final File file;
    private final Map<Long, Usuario> mapa = new LinkedHashMap<>();
    private long nextId = 1;

    public UsuarioManager(String path) {
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
                if (p.length < 4) continue;
                long id = Long.parseLong(p[0]);
                String nome = p[1];
                String login = p[2];
                String hash = p[3];
                Usuario u = new Usuario(id, nome, login, hash);
                mapa.put(id, u);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Usuario u : mapa.values()) pw.println(u.serialize());
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public Usuario criar(String nome, String login, String senhaPlain) {
        String hash = Utils.sha256(senhaPlain);
        Usuario u = new Usuario(nextId++, nome, login, hash);
        mapa.put(u.getId(), u);
        save();
        return u;
    }

    public boolean editar(long id, String nome, String login, String senhaPlain) {
        Usuario u = mapa.get(id);
        if (u == null) return false;
        u.setNome(nome);
        u.setLogin(login);
        if (senhaPlain != null && !senhaPlain.isEmpty()) u.setSenhaHash(Utils.sha256(senhaPlain));
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

    public List<Usuario> listar() {
        return new ArrayList<>(mapa.values());
    }

    public Usuario buscarPorLogin(String login) {
        for (Usuario u : mapa.values()) if (u.getLogin().equalsIgnoreCase(login)) return u;
        return null;
    }

    public boolean autenticar(String login, String senhaPlain) {
        Usuario u = buscarPorLogin(login);
        if (u == null) return false;
        String hash = Utils.sha256(senhaPlain);
        return u.getSenhaHash().equalsIgnoreCase(hash);
    }
}
