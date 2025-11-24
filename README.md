✅ Explicação simples: Como o HTTP é usado no meu projeto

No meu projeto AuroraLink, eu implementei a API sem usar frameworks, então eu mesmo precisei montar a parte do HTTP.
Para isso, eu usei a classe HttpServer do próprio Java, que já vem no JDK.

👉 O que essa parte faz?

Ela é responsável por:

abrir uma porta (geralmente a 8080)

receber requisições HTTP (GET, POST, PUT, DELETE)

mandar essas requisições pro lugar certo (os controllers)

devolver uma resposta HTTP para o cliente

🧩 Como funciona dentro do meu projeto
1. Server.java

É aqui que tudo começa.

Eu inicio o HttpServer nessa classe.

Falo qual porta ele vai usar.

Registro os endpoints, tipo /empresas e /funcionarios.

Depois ele fica “escutando” as requisições.

É como ligar uma máquina e deixá-la esperando alguém chamar.

2. Router.java

Aqui eu faço uma lógica para “direcionar” cada requisição.

Exemplo:

Se chega /empresas → mando para o EmpresaController

Se chega /funcionarios → mando para o FuncionarioController

Ele olha:

qual é o caminho

qual é o método HTTP (GET/POST/PUT/DELETE)

e envia para o controller correto

É literalmente um roteador, como o Wi-Fi, mas de URLs.

3. Controllers

Eles são os responsáveis por tratar a requisição HTTP de verdade.

Ex.:

GET /empresas → chama o método listar()

POST /empresas → chama salvar()

DELETE /empresas/3 → chama deletar(3)

Os controllers pegam os dados da requisição,
chamam o service/repository,
e devolvem uma resposta HTTP formatada.

4. Resposta HTTP

Toda vez que o controller termina, ele devolve:

Código HTTP
(200, 201, 400, 404, etc.)

Corpo da resposta (JSON)

Isso aparece no Postman ou no navegador
e é como qualquer API profissional funciona.
