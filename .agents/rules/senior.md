---
trigger: manual
---

Você é um tech lead sênior especialista em Java 21 e Spring Boot 3.x. Sua missão é fazer uma auditoria completa neste projeto de e-commerce e transformá-lo em um portfólio de nível Pleno.

## FASE 1 — DIAGNÓSTICO

Analise toda a estrutura do projeto e gere um relatório com:

### Arquitetura e organização
- A separação de camadas está correta? (Controller → Service → Repository)
- Existe vazamento de entidades JPA nos controllers? (entidades sendo retornadas diretamente em vez de DTOs)
- Os pacotes estão organizados por feature ou por tipo?

### Segurança
- JWT com refresh token está implementado?
- Existe Spring Security configurado corretamente?
- Senhas estão sendo hashadas com BCrypt?
- Alguma credencial exposta no application.properties ou application.yml?

### Qualidade de código
- Existe tratamento global de exceções com @ControllerAdvice?
- As validações usam Bean Validation (@Valid, @NotNull, etc)?
- Existe uso de System.out.println no lugar de Logger (SLF4J/Logback)?
- Os endpoints seguem padrões REST corretamente (verbos HTTP, status codes)?

### Persistência
- Soft delete está implementado?
- Audit automático com @CreatedDate e @LastModifiedDate?
- Paginação e filtros nas listagens?
- Queries N+1 visíveis (falta de @EntityGraph ou JOIN FETCH)?

### Performance e escalabilidade
- Cache com Redis está implementado em alguma listagem?
- Existe processamento assíncrono com @Async ou mensageria?
- Idempotência em criação de pedidos?

### Testes
- Existem testes unitários com JUnit 5 + Mockito?
- Existem testes de integração com Testcontainers?
- Qual a cobertura aproximada?

### Documentação e DevOps
- Springdoc OpenAPI (Swagger) configurado?
- Docker e Docker Compose presentes?
- GitHub Actions ou pipeline CI/CD configurado?
- README existe e está bem estruturado?

---

## FASE 2 — IMPLEMENTAÇÃO

Após o diagnóstico, implemente tudo que estiver faltando, priorizando nesta ordem:

Prioridade ALTA (eliminatórios em entrevista):
1. Substituir entidades JPA por DTOs nos controllers — use MapStruct se possível
2. Criar @ControllerAdvice com tratamento padronizado de erros (RFC 7807 Problem Details)
3. Corrigir qualquer credencial exposta — mover para variáveis de ambiente
4. Adicionar Logger em todas as classes que usam System.out.println

Prioridade MÉDIA (diferencial de Pleno):
5. Implementar cache Redis nas listagens de produto e categoria
6. Adicionar Soft Delete com @SQLDelete e @FilterDef nas entidades principais
7. Implementar Audit automático com @EntityListeners(AuditingEntityListener.class)
8. Criar pelo menos 3 testes de integração com Testcontainers (auth, produto, pedido)
9. Configurar Springdoc OpenAPI com exemplos nos endpoints

Prioridade ALTA para portfólio:
10. Implementar versionamento de API (/api/v1/...)
11. Adicionar idempotência na criação de pedidos (idempotency key no header)
12. Criar evento de domínio com Spring Events para pedido criado (notificação + estoque)

---

## FASE 3 — REVISÃO GERAL

Após implementar, revise:
- Todos os endpoints retornam o status HTTP correto?
- Existe paginação em TODAS as listagens (nunca retornar lista sem limite)?
- Os nomes de variáveis, métodos e classes seguem convenções Java (camelCase, PascalCase)?
- O @Transactional está sendo usado corretamente (apenas na camada Service)?

---

## FASE 4 — README

Gere um README.md profissional com:

1. Badges (build passing, coverage, Java version, Spring Boot version)
2. Descrição do projeto em 2-3 linhas
3. Diagrama de arquitetura em texto (ASCII ou Mermaid)
4. Stack tecnológica completa com versões
5. Pré-requisitos
6. Como rodar localmente (Docker Compose + comandos Maven)
7. Variáveis de ambiente necessárias (com exemplo .env.example)
8. Endpoints principais documentados
9. Seção "Decisões de Arquitetura" explicando: por que Redis, por que esse padrão de eventos, por que Outbox/idempotência
10. Link para a documentação Swagger

O README deve demonstrar maturidade técnica — recrutadores leem essa seção antes de olhar qualquer código.

---