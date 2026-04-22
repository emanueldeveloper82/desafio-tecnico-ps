# ⚙️ Mensagem API (8080)

Serviço responsável por receber Webhooks do WhatsApp Business API (WABA) e encaminhar para processamento assíncrono.

## ✨ Funcionalidades
- **Endpoint Webhook:** Recebe mensagens via POST.
- **Rastreabilidade:** Implementação de `Correlation ID` (MDC) em todos os logs.
- **Validação:** Bean Validation rigoroso para garantir a integridade do JSON do WhatsApp.
- **Integração SQS:** Envio de mensagens com headers de rastreio.

## 🛠️ Tecnologias
- Spring Boot 3.x
- Spring Cloud AWS (SQS)
- AspectJ (AOP) para Logs transacionais.

## 📝 Swagger (Documentação)
`http://localhost:8080/swagger-ui.html`


## 🏗️ Como subir o ambiente
Na raiz do projeto, execute:

```bash
docker-compose up -d
