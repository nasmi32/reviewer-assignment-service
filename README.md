# reviewer-assignment-service

Сервис для назначения ревьюверов на Pull Request в рамках команды.

## Функциональность

- создание команд
- назначение ревьюверов на PR (до 2-х)
- переназначение ревьюверов
- статистика по авторам и ревьюверам
- создание пользователя
- добавление пользователя в команду
- просмотр всех команд, пользователей

## Переменные окружения

В проекте лежит файл примера .env.example:

```
DB_URL=db
DB_PORT=5432
DB_NAME=reviewer_db

DB_USERNAME=postgres
DB_PASSWORD=postgres
```

## Работа

Запуск
- `make up`

Остановка:
- `make down`

После запуска:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html