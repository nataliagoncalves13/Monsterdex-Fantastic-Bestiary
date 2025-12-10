# 🐉 MonsterDex – Bestiário de Criaturas Fantásticas

O **MonsterDex** é uma aplicação web criada para organizar, catalogar e documentar criaturas fantásticas em um ambiente totalmente customizável pelos próprios usuários. Inspirado em bestiários clássicos e enciclopédias de monstros, o sistema permite que qualquer pessoa — jogadores de RPG, escritores, worldbuilders, professores, criadores de conteúdo ou entusiastas de fantasia — registre suas próprias criaturas, incluindo informações como tipo, habitat, poderes e imagens obtidas automaticamente via API externa.

Além do catálogo de criaturas, o MonsterDex oferece também um módulo de Entradas de Diário, onde o usuário pode registrar encontros, eventos, aventuras ou observações relacionadas a cada criatura. Esses registros incluem clima do local (obtido automaticamente por API externa), data, descrição e outros detalhes narrativos ou investigativos.

Com autenticação segura, arquitetura profissional em camadas (MVC), banco de dados relacional e documentação REST completa, o sistema pode ser utilizado tanto como uma ferramenta pessoal quanto como apoio em campanhas de RPG, projetos literários, atividades educacionais, worldbuilding colaborativo ou até protótipos de jogos.

---

## 🚀 Tecnologias Utilizadas

### **Backend**

* Java 17
* Spring Boot
* Spring MVC
* Spring Security
* Spring Data JPA
* Swagger / OpenAPI
* RestTemplate

### **Banco de Dados**

* PostgreSQL (produção)

### **Frontend**

* Thymeleaf (monolito)

---

## 📌 Funcionalidades

* Autenticação e autorização de usuários
* CRUD de **Criaturas**
* CRUD de **Entradas de Diário**
* Associação entre criatura e entrada de diário
* Integração com APIs externas:

  * Imagens (Unsplash)
* Documentação dos endpoints (Swagger)

---

## ▶️ Como Executar

### **Pré-requisitos**

* Java 17+
* Maven
* PostgreSQL

### **Clonar o repositório**

```bash
git clone https://github.com/<grupo>/monsterdex.git
cd monsterdex
```

### **Executar a aplicação**

```bash
mvn spring-boot:run
```

### **Acessos**

* Aplicação: **[http://localhost:8080](http://localhost:8080)**
* Swagger: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 🗄️ Entidades Principais

### **Usuário**

Responsável por autenticação e permissões.

### **Criatura**

Possui:

* nome
* tipo
* habitat
* poderes
* imagem vinda da API externa

### **Entrada de Diário**

Inclui:

* descrição
* data
* localização

---

## 🗓️ Cronograma da Disciplina

| Entrega                          | Data           |
| -------------------------------- | -------------- |
| Repositório e membros            | **18/10/2025** |
| Scaffold do projeto              | **25/10/2025** |
| Rotas e funcionalidades iniciais | **01/11/2025** |
| Banco + Autenticação             | **08/11/2025** |
| Entrega final                    | **09/12/2025** |

---

## 👥 Integrantes do Grupo

* Natalia dos Santos Gonçalves
* Leonardo de Paula Trindade
* Isabela de Paula Azevedo

