# ⚙️ Mensagem Service (8081)

Consumidor SQS responsável pelo processamento de mensagens, persistência e histórico.

## ✨ Funcionalidades
- **Processamento Assíncrono:** Consome da fila `waba-messages-queue`.
- **Idempotência:** Garante que o mesmo `wamid` do WhatsApp não seja processado duas vezes.
- **Distributed Tracing:** Recupera o `correlationId` dos headers da fila para manter o rastro da API.
- **Histórico:** Endpoint interno para consulta de mensagens persistidas.

## 🛠️ Tecnologias
- Spring Data JPA + H2
- Spring Cloud AWS (SQS Listener)
- SpringDoc OpenAPI

## 📝 Swagger (Documentação)
`http://localhost:8081/swagger-ui.html`

### Endpoint de Histórico
- `GET /internal/v1/messages`: Retorna todas as mensagens processadas.