AuroraLink – REST API em Java Puro

Este projeto faz parte da disciplina semestral e tem como objetivo desenvolver uma REST API em Java, sem usar frameworks, implementando CRUD completo para duas entidades em relacionamento 1..N:
Empresa (1) → N Funcionários.

A API foi construída utilizando apenas recursos nativos do Java, com sockets HTTP básicos, SQLite como banco embarcado, e uma arquitetura modular para demonstrar Abstração, Encapsulamento, Herança e Polimorfismo.

🔹 Tecnologias Utilizadas

Java 22 (JDK 22)

SQLite (banco de dados embarcado)

Maven (gerenciamento de dependências)

HTTPServer nativo (com.sun.net.httpserver.HttpServer)

Gson (para JSON ↔ objetos Java)

🔹 Entidades do Projeto
Empresa

id

nome

cidade

Funcionário

id

nome

empresaId (FK)

tipo (CLT ou PJ)

salário calculado via polimorfismo

Herança
FuncionarioBase (abstrata)
    ↑
    ├── FuncionarioCLT
    └── FuncionarioPJ


Cada tipo implementa seu próprio calcularSalario().

🌐 Como funciona a API HTTP

O Java possui uma classe chamada:

com.sun.net.httpserver.HttpServer


Ela permite criar um servidor HTTP sem usar frameworks.
No projeto, ela está sendo usada desta forma:

Server.java cria o servidor

Router.java define as rotas, como:

GET /empresas

POST /empresas

GET /funcionarios

Cada rota chama o respectivo Controller

O Controller chama o Service

O Service chama o Repository

O Repository acessa o banco via SQLite (JDBC)

Ou seja:

HTTP → Controller → Service → Repository → SQLite

📁 Estrutura do Projeto
src/main/java/com
│
├── Main.java
├── Server.java
├── Router.java
│
├── controllers/
│   ├── EmpresaController.java
│   └── FuncionarioController.java
│
├── service/
│   ├── EmpresaService.java
│   └── FuncionarioService.java
│
├── repository/
│   ├── EmpresaRepository.java
│   └── FuncionarioRepository.java
│
├── database/
│   └── Database.java
│
├── model/
│   ├── Empresa.java
│   ├── Funcionario.java
│   ├── FuncionarioBase.java
│   ├── FuncionarioCLT.java
│   └── FuncionarioPJ.java
│
└── util/
    ├── JsonUtil.java
    └── Database.java

🗂️ Banco de Dados

O arquivo auroralink.db é criado automaticamente na raiz do projeto.

As tabelas são criadas no primeiro uso pelo arquivo:

src/main/java/com/database/Database.java

▶️ Como Rodar o Projeto
1 — Clonar o repositório
git clone https://github.com/GuilhermebJunqueira/AuroraLink.git
cd AuroraLink

2 — Build via Maven
mvn clean package

3 — Executar
java -jar target/AuroraLink.jar


O servidor inicia em:

http://localhost:8080

📌 Rotas da API
EMPRESAS
✔ GET /empresas

Lista todas as empresas.

✔ GET /empresas/{id}

Busca empresa pelo ID.

✔ POST /empresas
{
  "nome": "TechCorp",
  "cidade": "São Paulo"
}

✔ PUT /empresas/{id}
✔ DELETE /empresas/{id}
FUNCIONÁRIOS
✔ GET /funcionarios

Lista todos os funcionários.

✔ POST /funcionarios
{
  "nome": "Carlos Silva",
  "empresaId": 1,
  "tipo": "CLT"
}


O salário é calculado automaticamente via polimorfismo.

🎓 Conceitos Demonstrados
Abstração

Interface clara entre camadas (Controller → Service → Repository)

Encapsulamento

Classes com atributos privados + getters/setters

Herança

FuncionarioBase → FuncionarioCLT / FuncionarioPJ

Polimorfismo
calcularSalario()


é diferente em cada tipo de funcionário.

Baixo acoplamento

Camadas independentes

Classes pequenas e focadas
