# 📖 EBDSoft

**Plataforma web para digitalização e gestão da Escola Bíblica Dominical (EBD).**

O EBDSoft é uma solução full-stack desenvolvida para modernizar a administração de Escolas Bíblicas Dominicais, centralizando processos acadêmicos, administrativos e financeiros em uma única plataforma.

Projetado para atender múltiplas igrejas em uma única instalação, o sistema oferece gestão de alunos, professores, turmas, aulas, materiais didáticos, contribuições financeiras e relatórios gerenciais, substituindo controles manuais por um ambiente digital integrado.

---

## ✨ Principais Funcionalidades

### 👥 Gestão de Pessoas

* Cadastro de igrejas, escolas bíblicas, turmas, professores e alunos
* Matrícula de alunos em turmas
* Associação de professores às turmas e aulas
* Controle de acesso baseado em perfis

### 📚 Gestão Pedagógica

* Planejamento de aulas por trimestre
* Controle de frequência de alunos e professores
* Gestão de revistas e materiais didáticos
* Controle de entrega de materiais por turma

### 💰 Gestão Financeira

* Registro de contribuições e ofertas
* Controle de pedidos de revistas
* Geração e acompanhamento de parcelas
* Controle de pagamentos e inadimplência

### 📊 Relatórios e Indicadores

* Relatórios em PDF
* Dashboards analíticos
* Indicadores de frequência
* Indicadores financeiros
* Acompanhamento de pedidos e entregas

### 🔐 Segurança e Controle de Acesso

* Autenticação baseada em JWT
* Controle de permissões por perfil
* Verificação de usuários por e-mail
* Geração de QR Codes

---

## 👤 Perfis de Usuário

O sistema possui diferentes níveis de acesso para atender às necessidades operacionais da organização:

| Perfil       | Responsabilidade                              |
| ------------ | --------------------------------------------- |
| ADMIN        | Administração global da plataforma            |
| ADMIN_IGREJA | Administração de uma igreja específica        |
| COORDENADOR  | Coordenação de turmas e aulas                 |
| SECRETARIA   | Gestão de cadastros e relatórios              |
| PROFESSOR    | Gerenciamento das próprias turmas e aulas     |
| ALUNO        | Consulta de informações pessoais e acadêmicas |

---

## 🧱 Arquitetura

```text
Frontend (React + TypeScript)
          │
          ▼
 REST API (Spring Boot)
          │
 ┌────────┴────────┐
 ▼                 ▼
PostgreSQL      RabbitMQ
```

O sistema segue uma arquitetura baseada em API REST, separando frontend e backend para facilitar manutenção, escalabilidade e evolução da plataforma.

---

## 🛠️ Stack Tecnológica

### Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* RabbitMQ
* JWT
* ModelMapper
* ZXing
* Lombok
* Maven

### Frontend

* React 18
* TypeScript
* Vite
* Bootstrap
* TailwindCSS
* Axios
* React Router
* Chart.js
* jsPDF
* html2canvas

---

## 📂 Estrutura do Projeto

```text
ebdsoft/
├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   └── config/
│
└── frontend/
    ├── pages/
    ├── components/
    ├── routes/
    ├── services/
    └── styles/
```

---

## 🚀 Executando Localmente

### Pré-requisitos

* Java 17+
* Maven
* Node.js 18+
* PostgreSQL

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 📡 Principais Módulos

| Módulo       | Descrição                                 |
| ------------ | ----------------------------------------- |
| Autenticação | Login, registro e verificação de usuários |
| Usuários     | Gestão de usuários e perfis               |
| Igrejas      | Administração de igrejas                  |
| EBDs         | Gestão das Escolas Bíblicas               |
| Turmas       | Controle de classes e organização         |
| Aulas        | Planejamento e frequência                 |
| Alunos       | Matrículas e acompanhamento               |
| Professores  | Gestão de docentes                        |
| Revistas     | Controle de materiais didáticos           |
| Pedidos      | Solicitações de revistas                  |
| Pagamentos   | Controle financeiro                       |

---

## 🗺️ Evoluções Planejadas

* Testes automatizados
* Integração contínua (CI/CD)
* Containerização com Docker
* Documentação OpenAPI/Swagger
* Progressive Web App (PWA)
* Notificações em tempo real
* Exportação para Excel
* Aplicativo mobile

---

## 👨‍💻 Autor

**Pedro Paulo Silva**

Graduado em Sistemas de Informação pela Universidade Federal Rural da Amazônia (UFRA).

Desenvolvedor Full Stack com experiência em Java, Spring Boot, React, TypeScript e PostgreSQL.

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT.

