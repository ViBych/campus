# Automation Services v2 — Документация доработок

## Обзор изменений

В систему добавлены два новых сервиса и доработан существующий для реализации сценария проверки платежей по 115-ФЗ

**Новые сервисы:**

| Сервис | Описание                                       |
|--------|------------------------------------------------|
| auth-api | Регистрация и аутентификация сотрудников (JWT) |
| compliance-api | Проверка платежей по правилам 115-ФЗ |

**Доработанные сервисы:**

| Сервис | Что изменилось |
|--------|----------------|
| payments-api | Новый статус `M`, поиск платежей по дате, опциональное поле `createdAt` |

---

## Статусы платежей

| Статус | Название | Описание |
|--------|----------|----------|
| N | Новый | Платёж создан |
| C | На подписании | Инициировано подписание |
| S | Успешно | Подписание подтверждено |
| **M** | **Заблокирован** | **Платёж заблокирован службой комплаенс** |
| F | Ошибка | Из любого состояния |

**Новый переход:** S → M (выполняется compliance-api при обнаружении нарушений).

---

## Доработки payments-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/payments-api`

### Новый эндпоинт: Поиск платежей

```
GET /api/payments/search?fromUserId={xPin}&date={YYYY-MM-DD}
```

Возвращает список платежей пользователя за указанную дату.

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| fromUserId | string | да | X-pin пользователя-отправителя |
| date | string (ISO date) | да | Дата в формате YYYY-MM-DD |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/payments-api/api/payments/search?fromUserId=XAAAAA&date=2026-02-27"
```

**Пример ответа:**

```json
[
    {
        "reference": "P152702202600000001",
        "from": {
            "userId": "XAAAAA",
            "organizationId": "UAAAAA",
            "accountNumber": "40702810540051266334"
        },
        "to": {
            "organizationId": "UAAAAB"
        },
        "amount": 700000.00,
        "status": "S",
        "createdAt": "2026-02-27T19:04:38.899473",
        "updatedAt": "2026-02-27T19:05:44.102517"
    }
]
```

### Опциональное поле createdAt при создании платежа

При создании платежа теперь можно передать поле `createdAt` для указания произвольной даты создания. Если поле не передано — используется текущее время (поведение по умолчанию).

```bash
curl -X POST https://alfa-campus-qa.ru/automation/payments-api/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "from": {
        "userId": "XAAAAA",
        "organizationId": "UAAAAA",
        "accountNumber": "40702810540051266334"
    },
    "to": {
        "organizationId": "UAAAAB"
    },
    "amount": 100000.00,
    "createdAt": "2026-02-25T10:00:00"
  }'
```

### Обновлённая таблица переходов статусов

| Из статуса | В статус | Описание |
|------------|----------|----------|
| N | C | Инициализация подписания |
| C | S | Подтверждение подписания |
| S | M | Блокировка комплаенс-службой |
| любой | F | Ошибка |

---

## auth-api (новый сервис)

**Базовый путь:** `https://alfa-campus-qa.ru/automation/auth-api`

Сервис регистрации и аутентификации сотрудников. Выдаёт JWT-токены для доступа к compliance-api.

### Справочник API

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/auth/register` | Регистрация сотрудника |
| POST | `/api/auth/login` | Аутентификация и получение JWT |

### Предварительно разрешённые логины

Регистрация доступна только для допущенных логинов.
Логин - название вашего неймспейса в gitlab.
Если не нашли или не работает - написать Козелкову Сергею.

### POST /api/auth/register

Регистрация нового сотрудника. Логин должен присутствовать в списке допущенных.

**Тело запроса:**

```json
{
    "login": "employee1",
    "password": "pass123"
}
```

**Пример запроса:**

```bash
curl -X POST https://alfa-campus-qa.ru/automation/auth-api/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login": "employee1", "password": "pass123"}'
```

**Успешный ответ (200):**

```json
{
    "login": "employee1",
    "message": "Employee registered successfully"
}
```

**Ошибки (400):**

| Код ошибки | Описание |
|------------|----------|
| LOGIN_NOT_ALLOWED | Логин отсутствует в списке допущенных |
| ALREADY_REGISTERED | Сотрудник с таким логином уже зарегистрирован |

```json
{
    "error": "LOGIN_NOT_ALLOWED",
    "message": "Login is not in the allowed list: unknown_user"
}
```

### POST /api/auth/login

Аутентификация сотрудника. JWT-токен возвращается в заголовке `Authorization`.

**Тело запроса:**

```json
{
    "login": "employee1",
    "password": "pass123"
}
```

**Пример запроса:**

```bash
curl -i -X POST https://alfa-campus-qa.ru/automation/auth-api/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "employee1", "password": "pass123"}'
```

**Успешный ответ (200):**

Тело ответа пустое. Токен передаётся в заголовке:

```
HTTP/1.1 200
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJlbXBsb3llZTEi...
```

**Ошибки (400):**

| Код ошибки | Описание |
|------------|----------|
| EMPLOYEE_NOT_FOUND | Сотрудник не найден (не зарегистрирован) |
| INVALID_PASSWORD | Неверный пароль |

**Параметры JWT-токена:**
- Алгоритм: HS384
- Время жизни: 10 минут
- Payload: `sub` — логин сотрудника

---

## compliance-api (новый сервис)

**Базовый путь:** `https://alfa-campus-qa.ru/automation/compliance-api`

Сервис проверки платежей по правилам 115-ФЗ. Все эндпоинты требуют JWT-токен в заголовке `Authorization`.

### Аутентификация

Каждый запрос должен содержать заголовок:

```
Authorization: Bearer <jwt-токен>
```

Токен получается через `POST /api/auth/login` сервиса auth-api.

**Ответ при отсутствии/невалидном токене (401):**

```json
{
    "error": "UNAUTHORIZED",
    "message": "Missing or invalid Authorization header"
}
```

### Справочник API

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| GET | `/api/compliance/check/{paymentRef}` | Проверка платежа по правилам 115-ФЗ |
| PUT | `/api/compliance/suspicious/{orgId}` | Добавление организации в список подозрительных |
| GET | `/api/compliance/operations?userId={id}&date={date}` | Получение операций пользователя за дату |

### GET /api/compliance/check/{paymentRef}

Проверяет платёж по трём правилам 115-ФЗ. При обнаружении нарушения переводит платёж в статус `M`.

**Правила проверки (проверяются последовательно):**

| # | Правило | Условие |
|---|---------|---------|
| 1 | Крупная операция | Сумма платежа > 600 000 |
| 2 | Дневной лимит | Сумма всех платежей пользователя за день > 600 000 |
| 3 | Подозрительный получатель | Организация-получатель в списке подозрительных |

**Предусловие:** платёж должен быть в статусе `S` (Успешно).

**Пример запроса:**

```bash
curl https://alfa-campus-qa.ru/automation/compliance-api/api/compliance/check/P152702202600000001 \
  -H "Authorization: Bearer <jwt-токен>"
```

**Ответ при нарушении (200):**

```json
{
    "result": "NOT_OK",
    "paymentReference": "P152702202600000001",
    "paymentStatus": "M"
}
```

**Ответ при отсутствии нарушений (200):**

```json
{
    "result": "OK",
    "paymentReference": "P152702202600000001",
    "paymentStatus": "S"
}
```

**Ошибки (400):**

| Код ошибки | Описание |
|------------|----------|
| INVALID_PAYMENT_STATUS | Платёж не в статусе S |

### PUT /api/compliance/suspicious/{orgId}

Добавляет организацию в список подозрительных. После этого все платежи с этой организацией-получателем будут блокироваться при проверке.

**Пример запроса:**

```bash
curl -X PUT https://alfa-campus-qa.ru/automation/compliance-api/api/compliance/suspicious/UAAAAB \
  -H "Authorization: Bearer <jwt-токен>"
```

**Ответ (200):**

```json
{
    "orgId": "UAAAAB"
}
```

### GET /api/compliance/operations

Получение списка платежей пользователя за указанную дату. Проксирует запрос к payments-api.

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| userId | string | да | X-pin пользователя |
| date | string (ISO date) | да | Дата в формате YYYY-MM-DD |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/compliance-api/api/compliance/operations?userId=XAAAAA&date=2026-02-27" \
  -H "Authorization: Bearer <jwt-токен>"
```

**Ответ (200):**

```json
[
    {
        "reference": "P152702202600000001",
        "from": {
            "userId": "XAAAAA",
            "organizationId": "UAAAAA",
            "accountNumber": "40702810540051266334"
        },
        "to": {
            "organizationId": "UAAAAB"
        },
        "amount": 700000.00,
        "status": "M",
        "createdAt": "2026-02-27T19:04:38.899473",
        "updatedAt": "2026-02-27T19:05:44.102517"
    }
]
```

---

## Сквозной сценарий: Проверка платежа по 115-ФЗ

Полный сценарий от создания платежа до блокировки комплаенс-службой.

### Шаг 1–9: Стандартный платёжный процесс

Выполняется аналогично основному процессу из v1 (см. DOCUMENTATION.md), но с суммой > 600 000:

```bash
# 1. Генерация пользователя
curl -X POST .../api/users/generate
# → XAAAAA

# 2-3. Генерация двух организаций
curl -X POST .../api/organizations/generate
# → UAAAAA (отправитель), UAAAAB (получатель)

# 4. Создание счёта
curl -X POST .../api/accounts/create \
  -d '{"uPin": "UAAAAA"}'

# 5-6. Добавление прав
curl -X POST .../api/roles/add -d '{"xPin":"XAAAAA","uPin":"UAAAAA","rule":"CREATE_PAYMENT"}'
curl -X POST .../api/roles/add -d '{"xPin":"XAAAAA","uPin":"UAAAAA","rule":"SIGN_PAYMENT"}'

# 7. Создание платежа на 700 000
curl -X POST .../api/payments/create \
  -d '{"from":{"userId":"XAAAAA","organizationId":"UAAAAA","accountNumber":"..."},"to":{"organizationId":"UAAAAB"},"amount":700000}'
# → reference: P15..., status: N

# 8. Подписание
curl -X POST .../api/sign/init -d '{"paymentReference":"P15...","xPin":"XAAAAA","uPin":"UAAAAA"}'
curl -X POST .../api/sign/confirm -d '{"signReference":"S19...","code":"1234"}'
# → status: S
```

### Шаг 10: Регистрация сотрудника

```bash
curl -X POST https://alfa-campus-qa.ru/automation/auth-api/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login": "employee1", "password": "pass123"}'
```

### Шаг 11: Получение JWT-токена

```bash
curl -i -X POST https://alfa-campus-qa.ru/automation/auth-api/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "employee1", "password": "pass123"}'
```

Из заголовка ответа извлекается `Authorization: Bearer <token>`.

### Шаг 12: Проверка платежа комплаенс-службой

```bash
curl https://alfa-campus-qa.ru/automation/compliance-api/api/compliance/check/P152702202600000001 \
  -H "Authorization: Bearer <token>"
```

**Ожидаемый результат:** `NOT_OK`, статус платежа `M` (сумма 700 000 > порога 600 000).

```json
{
    "result": "NOT_OK",
    "paymentReference": "P152702202600000001",
    "paymentStatus": "M"
}
```

### Шаг 13: Подтверждение через payment-proxy

```bash
curl https://alfa-campus-qa.ru/automation/payment-proxy-api/api/payment-proxy/P152702202600000001
```

**Ожидаемый результат:** статус платежа `M`.

---
