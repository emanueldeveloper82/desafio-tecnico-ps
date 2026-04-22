# 🚀 WABA Messaging Infrastructure

Este projeto contém a infraestrutura necessária para rodar o ecossistema de mensagens, utilizando Docker, LocalStack (SQS) e H2.

## 🛠️ Pré-requisitos
- Docker & Docker Compose
- Java 17+

## 📥 Configuração de Filas (LocalStack)
O arquivo `init-sqs.sh` é responsável por criar as filas no ambiente local. Para garantir que o Docker consiga executá-lo, é necessário dar permissão de execução:

```bash
chmod +x init-sqs.sh
