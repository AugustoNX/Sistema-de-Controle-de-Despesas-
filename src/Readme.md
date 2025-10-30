# Sistema de Controle de Despesas

## Visão geral
Sistema em Java para controle de despesas e pagamentos, armazenando dados em arquivos de texto (`data/`). Projetado para fins didáticos e aplicação prática de POO, herança, interfaces e persistência simples.

## Estrutura
- `src/` — código-fonte Java
  - `model/` — classes de domínio (Despesa, Usuario, TipoDespesa, etc.)
  - `persistence/` — gerenciadores que lidam com leitura/escrita em arquivos
  - `util/` — utilitários (hash SHA-256, leitura de linhas)
- `data/` — arquivos de dados (despesas.txt, tipos_despesa.txt, usuarios.txt)
- `README.md` — este arquivo

## Requisitos
- Java 8+ (recomendo Java 11)
- Ambiente para compilar/rodar Java (javac/java)

## Como compilar e rodar
1. Estruture pastas conforme `src/` e `data/`.
2. Compile:
```bash
cd SistemaControleDespesas
javac -d out src/**/*.java