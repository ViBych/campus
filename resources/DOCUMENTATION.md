# Automation Services - Документация

## Обзор

6 сервисов для тестирования банковских операций.

## Сервисы

| Сервис | Путь | Описание |
|--------|------|----------|
| test-user-data-api | `/automation/test-user-data-api/` | Пользователи (X-pin) и Организации (U-pin) |
| accounts-api | `/automation/accounts-api/` | Управление банковскими счетами |
| roles-api | `/automation/roles-api/` | Управление правами на основе ролей |
| payments-api | `/automation/payments-api/` | Платёжные операции |
| sign-api | `/automation/sign-api/` | Подписание платежей |
| payment-proxy-api | `/automation/payment-proxy-api/` | Прокси для получения информации о платежах |

**Базовый URL:** `https://alfa-campus-qa.ru`

## Основной процесс (Flow)

### 1. Генерация X-pin (Пользователь)

```bash
curl -X POST https://alfa-campus-qa.ru/automation/test-user-data-api/api/users/generate
```

Ответ:
```json
{
    "id": "XAAAAA",
    "firstName": "Александр",
    "middleName": "Владимирович",
    "lastName": "Иванов",
    "gender": "MALE",
    "clientType": "UP",
    "contactInfo": {
        "phone": "79507617952",
        "email": "aleksandr.ivanov1234@testmail.ru"
    },
    "embossingName": "ALEKSANDR IVANOV",
    "passportData": {
        "birthDate": "11-08-1998",
        "birthPlace": "г. Челябинск",
        "issueDate": "14-11-2021",
        "series": "5925",
        "number": "130086"
    },
    "actualAddress": {
        "country": "Россия",
        "region": "Московская область",
        "city": "Москва",
        "street": "Тверская",
        "building": "1",
        "apartment": "10"
    },
    "registrationAddress": {
        "country": "Россия",
        "region": "Волгоградская область",
        "city": "Волгоград",
        "street": "Советская",
        "building": "117",
        "apartment": "26"
    }
}
```

### 2. Генерация U-pin (Организация)

```bash
curl -X POST https://alfa-campus-qa.ru/automation/test-user-data-api/api/organizations/generate
```

Ответ:
```json
{
    "organizationId": "UAAAAA",
    "organizationName": {
        "shortName": "Краткое № 79071",
        "fullName": "Полное наименование Орг № 79071",
        "embName": "EMB NAME LLC 79071"
    },
    "supportInfo": {
        "inn": "0277343946",
        "kpp": "036246021",
        "ogrn": "1146670027679"
    }
}
```

### 3. Генерация второго U-pin (организация-получатель)

```bash
curl -X POST https://alfa-campus-qa.ru/automation/test-user-data-api/api/organizations/generate
```

### 4. Создание счёта для организации

```bash
curl -X POST https://alfa-campus-qa.ru/automation/accounts-api/api/accounts/create \
  -H "Content-Type: application/json" \
  -d '{
    "uPin": "UAAAAA",
    "currency": "RUR",
    "accountInfo": "PAYMENT_RUR"
  }'
```

Ответ:
```json
{
    "accountNumberFull": "40702810401120042270",
    "currency": "RUR",
    "accountInfo": "PAYMENT_RUR",
    "accountBranch": "0112",
    "organizationId": "UAAAAA",
    "balance": 1000000.00
}
```

### 5. Добавление права CREATE_PAYMENT

```bash
curl -X POST https://alfa-campus-qa.ru/automation/roles-api/api/roles/add \
  -H "Content-Type: application/json" \
  -d '{
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "rule": "CREATE_PAYMENT"
  }'
```

Ответ:
```json
{
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "rule": "CREATE_PAYMENT"
}
```

### 6. Добавление права SIGN_PAYMENT

```bash
curl -X POST https://alfa-campus-qa.ru/automation/roles-api/api/roles/add \
  -H "Content-Type: application/json" \
  -d '{
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "rule": "SIGN_PAYMENT"
  }'
```

### 7. Создание платежа

```bash
curl -X POST https://alfa-campus-qa.ru/automation/payments-api/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "from": {
        "userId": "XAAAAA",
        "organizationId": "UAAAAA",
        "accountNumber": "40702810401120042270"
    },
    "to": {
        "organizationId": "UAAAAB"
    },
    "amount": 4200.00
  }'
```

Ответ:
```json
{
    "reference": "P151902202600000001",
    "from": {
        "userId": "XAAAAA",
        "organizationId": "UAAAAA",
        "accountNumber": "40702810401120042270"
    },
    "to": {
        "organizationId": "UAAAAB"
    },
    "amount": 4200.00,
    "status": "N",
    "createdAt": "2026-02-19T17:30:00",
    "updatedAt": "2026-02-19T17:30:00"
}
```

### 8. Инициализация подписания

```bash
curl -X POST https://alfa-campus-qa.ru/automation/sign-api/api/sign/init \
  -H "Content-Type: application/json" \
  -d '{
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "paymentReference": "P151902202600000001"
  }'
```

Ответ:
```json
{
    "signReference": "S191902202600000001",
    "paymentReference": "P151902202600000001",
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "status": "INITIATED",
    "createdAt": "2026-02-19T17:31:00"
}
```

После этого шага статус платежа меняется на **C** (На подписании).

### 9. Подтверждение подписания

```bash
curl -X POST https://alfa-campus-qa.ru/automation/sign-api/api/sign/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "xPin": "XAAAAA",
    "uPin": "UAAAAA",
    "signReference": "S191902202600000001"
  }'
```

После этого шага статус платежа меняется на **S** (Успешно).

### 10. Проверка статуса платежа через прокси

```bash
curl https://alfa-campus-qa.ru/automation/payment-proxy-api/api/payment-proxy/P151902202600000001
```

Платёж должен иметь статус **S** (Успешно).

---

## Справочник API

### test-user-data-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/test-user-data-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/users/generate` | Генерация пользователя со случайными данными |
| GET | `/api/users/{pin}` | Получение пользователя по X-pin |
| POST | `/api/users/create` | Создание пользователя с указанными данными |
| POST | `/api/organizations/generate` | Генерация организации со случайными данными |
| GET | `/api/organizations/{pin}` | Получение организации по U-pin |
| POST | `/api/organizations/create` | Создание организации с указанными данными |

**Формат pin:** X + 5 символов (A-Z, 0-9) для пользователей, U + 5 символов для организаций. Последовательная нумерация (XAAAAA, XAAAAB, ..., XAAAA0, ..., XAAABA, ...).

### accounts-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/accounts-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/accounts/create` | Создание счёта (с валидацией U-pin) |
| GET | `/api/accounts/{uPin}` | Получение счетов по организации |
| GET | `/api/accounts/check?organizationId=X&accountNumber=Y` | Проверка существования счёта |
| GET | `/api/accounts/balance?accountNumber=X` | Получение баланса счёта |

**Формат номера счёта:** `40702810` + 12 случайных цифр. Баланс по умолчанию: 1 000 000,00 RUR.

### roles-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/roles-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/roles/add` | Добавление роли (xPin + uPin + rule) |
| GET | `/api/roles/check?xPin=X&uPin=U&rule=R` | Проверка наличия права |

**Используемые правила:** `CREATE_PAYMENT`, `SIGN_PAYMENT`.

### payments-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/payments-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/payments/create` | Создание платежа (полная валидация) |
| PUT | `/api/payments/{reference}/status` | Обновление статуса платежа |
| GET | `/api/payments/{reference}` | Получение платежа по reference |

**Формат reference:** `P15` + DDMMYYYY + 8 цифр (например, P151902202600000001).

**Переходы статусов:** N (Новый) -> C (На подписании) -> S (Успешно). F (Ошибка) из любого состояния.

**Валидации при создании:**
- Формат userId (X + 5 символов) и существование
- Формат organizationId (U + 5 символов) и существование
- Существование счёта для организации
- Существование организации-получателя
- Достаточность баланса
- Наличие права CREATE_PAYMENT

### sign-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/sign-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/sign/init` | Инициализация подписания (проверяет SIGN_PAYMENT, устанавливает статус платежа C) |
| POST | `/api/sign/confirm` | Подтверждение подписания (устанавливает статус платежа S) |

**Формат sign reference:** `S19` + DDMMYYYY + 8 цифр (например, S191902202600000001).

### payment-proxy-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/payment-proxy-api`

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| GET | `/api/payment-proxy/{reference}` | Проксирование запроса к payments-api |
