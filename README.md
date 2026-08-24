# Task Tracker Scheduler

Scheduler-сервис проекта [Task Tracker](https://github.com/prplhd/task-tracker).

Сервис раз в сутки запускает формирование пользовательских отчётов по задачам. Он получает данные из backend, отправляет их в сервис суммаризации через Kafka RPC, а готовый отчет передает в Kafka для последующей отправки пользователю по email.

## Как работает

Каждый день в `00:00` по московскому времени сервис:

1. Запрашивает у backend данные по задачам за прошедшие сутки.
2. Отправляет данные пользователя в топик `SUMMARIZATION_REQUESTS`.
3. Получает сгенерированный отчёт через Kafka request-reply.
4. Отправляет готовое письмо в топик `EMAIL_SENDING_TASKS`.

## Стек

- Java 21
- Spring Boot 4
- Spring Scheduler
- Spring Kafka
- Kafka Request-Reply
- Spring RestClient
- Gradle
- Docker

## Локальный запуск

Для локальной работы необходимы:

- Backend — `localhost:8080`
- Kafka — `localhost:9092`

Kafka можно поднять из [основного репозитория](https://github.com/prplhd/task-tracker):

```bash
docker compose -f docker-compose.dev.yml up -d
```

После этого scheduler запускается из IDE с профилем:

```text
dev
```

## Docker

Сервис входит в общий production Docker Compose проекта:

https://github.com/prplhd/task-tracker
