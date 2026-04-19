#!/bin/bash
echo "*********** Criando filas SQS no LocalStack ***********"

awslocal sqs create-queue --queue-name waba-messages-queue
awslocal sqs create-queue --queue-name messages-test-queue
awslocal sqs create-queue --queue-name messages-test-pessoa-queue

echo "*********** Filas criadas com sucesso! ***********"