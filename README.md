CRUD com Java + JDBC

---

No diretório raiz, execute:

1. Inicie container do banco de dados:
   docker compose up -d

2. Insira as tabelas no banco de dados:
   docker exec -i postgres-db psql -U postgres -d database_test < query.sql

3. Instale as dependências:
   mvn install

4. Confira os testes:
   mvn test
