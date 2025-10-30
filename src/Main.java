import java.util.List;
import java.util.Scanner;
import model.Despesa;
import model.TipoDespesa;
import persistence.DespesaManager;
import persistence.TipoDespesaManager;
import persistence.UsuarioManager;
import util.Utils;

public class Main {
    private static final String DATA_DIR = "data/";
    private static final String DESP_FILE = DATA_DIR + "despesas.txt";
    private static final String TIPOS_FILE = DATA_DIR + "tipos_despesa.txt";
    private static final String USU_FILE = DATA_DIR + "usuarios.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DespesaManager dm = new DespesaManager(DESP_FILE);
        TipoDespesaManager tm = new TipoDespesaManager(TIPOS_FILE);
        UsuarioManager um = new UsuarioManager(USU_FILE);

        // cria usuário admin inicial se nenhum existir
        if (um.listar().isEmpty()) {
            System.out.println("Nenhum usuário encontrado. Criando usuário padrão: admin/admin");
            um.criar("Administrador", "admin", "admin");
        }

        int opcao = 0;
        while (opcao != 7) {
            System.out.println("\n=== SISTEMA DE CONTROLE DE DESPESAS ===");
            System.out.println("1. Entrar Despesa");
            System.out.println("2. Anotar Pagamento");
            System.out.println("3. Listar Despesas em Aberto");
            System.out.println("4. Listar Despesas Pagas");
            System.out.println("5. Gerenciar Tipos de Despesa");
            System.out.println("6. Gerenciar Usuários");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            if (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Opção inválida");
                continue;
            }
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    entrarDespesa(sc, dm, tm);
                    break;
                case 2:
                    anotarPagamento(sc, dm);
                    break;
                case 3:
                    listarDespesas(sc, dm, false);
                    break;
                case 4:
                    listarDespesas(sc, dm, true);
                    break;
                case 5:
                    gerenciarTipos(sc, tm);
                    break;
                case 6:
                    gerenciarUsuarios(sc, um);
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
        sc.close();
    }

    private static void entrarDespesa(Scanner sc, DespesaManager dm, TipoDespesaManager tm) {
        System.out.println("\n--- Entrar Despesa ---");
        System.out.print("Descrição: ");
        String desc = Utils.readLineSkip(sc);
        System.out.print("Valor (ex: 123.45): ");
        double valor = Utils.readDoubleSafe(sc);
        sc.nextLine();
        System.out.print("Data de vencimento (YYYY-MM-DD): ");
        String dataVenc = sc.nextLine();

        System.out.println("Tipos disponíveis:");
        List<TipoDespesa> tipos = tm.listar();
        for (TipoDespesa t : tipos) System.out.println(t);
        System.out.print("Digite o nome da categoria (ou deixe vazio para 'Outros'): ");
        String cat = sc.nextLine();
        if (cat.trim().isEmpty()) cat = "Outros";

        dm.criarDespesa(desc, valor, dataVenc, cat);
        System.out.println("Despesa criada com sucesso.");
    }

    private static void anotarPagamento(Scanner sc, DespesaManager dm) {
        System.out.println("\n--- Anotar Pagamento ---");
        System.out.print("ID da despesa: ");
        long id = Utils.readLongSafe(sc);
        sc.nextLine();
        var desp = dm.buscarPorId(id);
        if (desp == null) {
            System.out.println("Despesa não encontrada.");
            return;
        }
        System.out.println("Despesa: " + desp);
        System.out.print("Data do pagamento (YYYY-MM-DD): ");
        String dataPag = sc.nextLine();
        System.out.print("Valor pago: ");
        double vPag = Utils.readDoubleSafe(sc);
        sc.nextLine();
        boolean ok = dm.registrarPagamento(id, dataPag, vPag);
        System.out.println(ok ? "Pagamento registrado." : "Falha ao registrar pagamento.");
    }

    private static void listarDespesas(Scanner sc, DespesaManager dm, boolean pagas) {
        System.out.println(pagas ? "\n--- Despesas Pagas ---" : "\n--- Despesas Pendentes ---");
        List<Despesa> list = dm.listarPorStatus(pagas);
        if (list.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }
        for (Despesa d : list) {
            System.out.println(d);
        }
        System.out.println("\nDeseja editar/excluir alguma despesa? (s/n)");
        String r = sc.nextLine();
        if (r.equalsIgnoreCase("s")) {
            System.out.print("Digite o ID: ");
            long id = Utils.readLongSafe(sc);
            sc.nextLine();
            Despesa d = dm.buscarPorId(id);
            if (d == null) { System.out.println("Despesa não encontrada."); return; }
            System.out.println("1-Editar | 2-Excluir | 3-Voltar");
            int o = sc.nextInt(); sc.nextLine();
            if (o == 1) {
                System.out.print("Nova descrição: ");
                String desc = Utils.readLineSkip(sc);
                System.out.print("Novo valor: ");
                double val = Utils.readDoubleSafe(sc); sc.nextLine();
                System.out.print("Nova data vencimento (YYYY-MM-DD): "); String dv = sc.nextLine();
                System.out.print("Nova categoria: "); String cat = sc.nextLine();
                boolean ok = dm.editarDespesa(id, desc, val, dv, cat);
                System.out.println(ok ? "Despesa editada." : "Falha ao editar.");
            } else if (o == 2) {
                boolean ok = dm.excluirDespesa(id);
                System.out.println(ok ? "Despesa excluída." : "Falha ao excluir.");
            }
        }
    }

    private static void gerenciarTipos(Scanner sc, TipoDespesaManager tm) {
        int op = 0;
        while (op != 5) {
            System.out.println("\n--- Gerenciar Tipos de Despesa ---");
            System.out.println("1. Listar");
            System.out.println("2. Criar");
            System.out.println("3. Editar");
            System.out.println("4. Excluir");
            System.out.println("5. Voltar");
            System.out.print("Opção: ");
            if (!sc.hasNextInt()) { sc.next(); System.out.println("Opção inválida"); continue; }
            op = sc.nextInt(); sc.nextLine();
            switch (op) {
                case 1:
                    var list = tm.listar();
                    if (list.isEmpty()) System.out.println("Nenhum tipo cadastrado.");
                    else list.forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Nome do tipo: ");
                    String nome = sc.nextLine();
                    tm.criar(nome);
                    System.out.println("Tipo criado.");
                    break;
                case 3:
                    System.out.print("ID a editar: ");
                    long id = Utils.readLongSafe(sc); sc.nextLine();
                    System.out.print("Novo nome: ");
                    String novo = sc.nextLine();
                    boolean ok = tm.editar(id, novo);
                    System.out.println(ok ? "Editado." : "Não encontrado.");
                    break;
                case 4:
                    System.out.print("ID a excluir: ");
                    long idex = Utils.readLongSafe(sc); sc.nextLine();
                    boolean ok2 = tm.excluir(idex);
                    System.out.println(ok2 ? "Excluído." : "Não encontrado.");
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private static void gerenciarUsuarios(Scanner sc, UsuarioManager um) {
        int op = 0;
        while (op != 5) {
            System.out.println("\n--- Gerenciar Usuários ---");
            System.out.println("1. Listar");
            System.out.println("2. Criar");
            System.out.println("3. Editar");
            System.out.println("4. Excluir");
            System.out.println("5. Voltar");
            System.out.print("Opção: ");
            if (!sc.hasNextInt()) { sc.next(); System.out.println("Opção inválida"); continue; }
            op = sc.nextInt(); sc.nextLine();
            switch (op) {
                case 1:
                    var list = um.listar();
                    if (list.isEmpty()) System.out.println("Nenhum usuário.");
                    else list.forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Nome: ");
                    String nome = Utils.readLineSkip(sc);
                    System.out.print("Login: "); String login = sc.nextLine();
                    System.out.print("Senha: "); String senha = sc.nextLine();
                    um.criar(nome, login, senha);
                    System.out.println("Usuário criado.");
                    break;
                case 3:
                    System.out.print("ID a editar: ");
                    long id = Utils.readLongSafe(sc); sc.nextLine();
                    System.out.print("Novo nome: "); String nNome = Utils.readLineSkip(sc);
                    System.out.print("Novo login: "); String nLogin = sc.nextLine();
                    System.out.print("Nova senha (deixe vazio para manter): "); String nSenha = sc.nextLine();
                    boolean ok = um.editar(id, nNome, nLogin, nSenha);
                    System.out.println(ok ? "Editado." : "Não encontrado.");
                    break;
                case 4:
                    System.out.print("ID a excluir: ");
                    long idex = Utils.readLongSafe(sc); sc.nextLine();
                    boolean ok2 = um.excluir(idex);
                    System.out.println(ok2 ? "Excluído." : "Não encontrado.");
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}
