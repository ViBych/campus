# Automation Services — Документация изменений

## Обзор

Документ описывает все изменения относительно версии v2 (auth-api + compliance-api). 

**Новые компоненты:**

| Компонент | Описание |
|-----------|----------|
| ui-app-automation | Веб-приложение — логин, создание платежей, подписание |

**Доработанные сервисы:**

| Сервис | Что изменилось |
|--------|----------------|
| accounts-api | Справочник банков, баланс на вход, объект банка в счёте, проверка существования счёта, списание средств |
| payments-api | Банк и номер счёта получателя в платеже, получение всех платежей пользователя |
| roles-api | Поиск ролей по X-пину и правилу |
| test-user-data-api | Поиск организаций по названию, ИНН, КПП |

---

## Доработки accounts-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/accounts-api`

### Новый эндпоинт: Справочник банков

```
GET /api/accounts/get-bank-info
```

Возвращает список топ-20 банков РФ (название и БИК).

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/accounts-api/api/accounts/get-bank-info"
```

**Пример ответа:**

```json
[
    {
        "name": "АО \"АЛЬФА-БАНК\"",
        "bic": "044525593"
    },
    {
        "name": "ПАО Сбербанк",
        "bic": "044525225"
    }
]
```

### Новый эндпоинт: Проверка существования счёта

```
GET /api/accounts/exists?accountNumber={accountNumber}
```

Проверяет существование счёта по номеру (без привязки к организации). Отличается от `/api/accounts/check` тем, что не требует `organizationId`.

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| accountNumber | string | да | Номер счёта |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/accounts-api/api/accounts/exists?accountNumber=40702810401120042270"
```

**Пример ответа:**

```json
{
    "exists": true
}
```

### Новый эндпоинт: Списание со счёта

```
POST /api/accounts/deduct
```

Списывает указанную сумму с баланса счёта. Используется внутренне при проведении платежей.

**Тело запроса:**

```json
{
    "accountNumber": "40702810401120042270",
    "amount": "5000.00"
}
```

**Пример ответа:**

```json
{
    "accountNumber": "40702810401120042270",
    "balance": 995000.00
}
```

### Обновлённый эндпоинт: Создание счёта

```
POST /api/accounts/create
```

Добавлено опциональное поле `balance` в запросе. Если не указано — используется значение по умолчанию `1000000.00`.

При создании счёта автоматически устанавливается банк — АО "АЛЬФА-БАНК" (БИК 044525593).

**Обновлённый формат запроса:**

```json
{
    "uPin": "UAAAAA",
    "currency": "RUR",
    "accountInfo": "PAYMENT_RUR",
    "balance": 500000.00
}
```

**Обновлённый формат ответа:**

```json
{
    "accountNumberFull": "40702810401120042270",
    "currency": "RUR",
    "accountInfo": "PAYMENT_RUR",
    "accountBranch": "1234",
    "organizationId": "UAAAAA",
    "balance": 500000.00,
    "bank": {
        "name": "АО \"АЛЬФА-БАНК\"",
        "bic": "044525593"
    }
}
```

### Обновлённый эндпоинт: Получение баланса

```
GET /api/accounts/balance?accountNumber={accountNumber}
```

**Формат ответа:**

```json
{
    "accountNumber": "40702810401120042270",
    "balance": 1000000.00
}
```

### Обновлённый эндпоинт: Проверка принадлежности счёта

```
GET /api/accounts/check?organizationId={uPin}&accountNumber={accountNumber}
```

**Формат ответа:**

```json
{
    "exists": true
}
```

### Полный справочник API accounts-api

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| GET | `/api/accounts/get-bank-info` | Справочник банков (топ-20 РФ) |
| POST | `/api/accounts/create` | Создание счёта (+ balance, bank) |
| GET | `/api/accounts/{uPin}` | Счета организации |
| GET | `/api/accounts/check?organizationId=U&accountNumber=N` | Проверка принадлежности счёта организации |
| GET | `/api/accounts/exists?accountNumber=N` | Проверка существования счёта |
| GET | `/api/accounts/balance?accountNumber=N` | Баланс счёта |
| POST | `/api/accounts/deduct` | Списание средств со счёта |

---

## Доработки payments-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/payments-api`

### Обновлённый эндпоинт: Создание платежа

```
POST /api/payments/create
```

В объект `to` добавлены опциональные поля: `accountNumber` (номер счёта получателя) и `bank` (объект с `name` и `bic`).

**Обновлённый формат запроса:**

```json
{
    "from": {
        "userId": "XAAAAA",
        "organizationId": "UAAAAA",
        "accountNumber": "40702810401120042270"
    },
    "to": {
        "organizationId": "UAAAAB",
        "accountNumber": "40702810123456789012",
        "bank": {
            "name": "ПАО Сбербанк",
            "bic": "044525225"
        }
    },
    "amount": 1000.00,
    "createdAt": "2026-03-05T10:00:00"
}
```

> Поля `to.accountNumber`, `to.bank` и `createdAt` — опциональные.

**Обновлённый формат ответа:**

```json
{
    "reference": "P150503202600000001",
    "from": {
        "userId": "XAAAAA",
        "organizationId": "UAAAAA",
        "accountNumber": "40702810401120042270"
    },
    "to": {
        "organizationId": "UAAAAB",
        "accountNumber": "40702810123456789012",
        "bank": {
            "name": "ПАО Сбербанк",
            "bic": "044525225"
        }
    },
    "amount": 1000.00,
    "status": "N",
    "createdAt": "2026-03-05T10:00:00",
    "updatedAt": "2026-03-05T10:00:00"
}
```

### Новый эндпоинт: Все платежи пользователя

```
GET /api/payments/by-user?fromUserId={xPin}
```

Возвращает все платежи пользователя без фильтрации по дате (в отличие от `GET /api/payments/search`, который требует дату).

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| fromUserId | string | да | X-pin пользователя-отправителя |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/payments-api/api/payments/by-user?fromUserId=XAAAAA"
```

**Пример ответа:**

```json
[
    {
        "reference": "P150503202600000001",
        "from": {
            "userId": "XAAAAA",
            "organizationId": "UAAAAA",
            "accountNumber": "40702810401120042270"
        },
        "to": {
            "organizationId": "UAAAAB",
            "accountNumber": "40702810123456789012",
            "bank": {
                "name": "ПАО Сбербанк",
                "bic": "044525225"
            }
        },
        "amount": 4200.00,
        "status": "S",
        "createdAt": "2026-03-05T10:30:00",
        "updatedAt": "2026-03-05T10:31:00"
    }
]
```

### Полный справочник API payments-api

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/payments/create` | Создание платежа (+ to.bank, to.accountNumber) |
| PUT | `/api/payments/{reference}/status` | Обновление статуса |
| GET | `/api/payments/{reference}` | Получение платежа |
| GET | `/api/payments/search?fromUserId=X&date=Y` | Поиск платежей по дате |
| GET | `/api/payments/by-user?fromUserId=X` | Все платежи пользователя |

---

## Доработки roles-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/roles-api`

### Новый эндпоинт: Поиск ролей по X-пину

```
GET /api/roles/by-xpin?xPin={xPin}&rule={rule}
```

Возвращает список ролей пользователя для указанного правила. Используется UI для получения организаций, от имени которых пользователь может создавать платежи.

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| xPin | string | да | X-pin пользователя |
| rule | string | да | Название правила (CREATE_PAYMENT, SIGN_PAYMENT) |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/roles-api/api/roles/by-xpin?xPin=XAAAAA&rule=CREATE_PAYMENT"
```

**Пример ответа:**

```json
[
    {
        "xPin": "XAAAAA",
        "uPin": "UAAAAA",
        "rule": "CREATE_PAYMENT"
    }
]
```

### Полный справочник API roles-api

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/roles/add` | Добавление роли |
| GET | `/api/roles/check?xPin=X&uPin=U&rule=R` | Проверка наличия права |
| GET | `/api/roles/by-xpin?xPin=X&rule=R` | Поиск ролей по X-пину и правилу |

---

## Доработки test-user-data-api

**Базовый путь:** `https://alfa-campus-qa.ru/automation/test-user-data-api`

### Новый эндпоинт: Поиск организаций

```
GET /api/organizations/search?name={query}
```

Поиск организаций по подстроке в названии (shortName), ИНН или КПП. Регистронезависимый для названия.

**Параметры запроса:**

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| name | string | да | Строка поиска (название, ИНН или КПП) |

**Пример запроса:**

```bash
curl "https://alfa-campus-qa.ru/automation/test-user-data-api/api/organizations/search?name=Краткое"
```

**Пример ответа:**

```json
[
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
]
```

**Поиск по ИНН:**

```bash
curl "https://alfa-campus-qa.ru/automation/test-user-data-api/api/organizations/search?name=0277343946"
```

### Полный справочник API test-user-data-api

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/users/generate` | Генерация пользователя |
| GET | `/api/users/{pin}` | Получение пользователя |
| POST | `/api/users/create` | Создание пользователя |
| POST | `/api/organizations/generate` | Генерация организации |
| GET | `/api/organizations/{pin}` | Получение организации |
| POST | `/api/organizations/create` | Создание организации |
| GET | `/api/organizations/search?name=X` | Поиск организаций по названию/ИНН/КПП |

---

## ui-app-automation (новый компонент)

Веб-приложение банковского портала для QA-тестирования. Предоставляет графический интерфейс для платёжных операций через существующие микросервисы.

**Расположение:** `automation-services/ui-app-automation/`

### Страницы

| Страница | URL | Описание |
|----------|-----|----------|
| Логин | `/login` | авторизация: X-пин → паспорт → СМС |
| Создание платежа | `/create-payment` | Выбор отправителя, поиск получателя, выбор банка, ввод суммы |
| Все платежи | `/all-payments` | Таблица платежей с пагинацией (10 на страницу), детали и подписание |
| Успех | `/success` | Подтверждение успешной подписи |

### Процесс логина

| Шаг | Ввод | Валидация |
|-----|------|-----------|
| 1 | X-пин | Формат `X + 5 символов`, проверка через `GET /api/users/{xPin}` |
| 2 | Серия (4 цифры) + номер (6 цифр) паспорта | Сравнение с паспортными данными из API |
| 3 | СМС-код | Код = `000000` (фиксированный) |

Сессия сохраняется в `sessionStorage` — при обновлении страницы авторизация не теряется. Номер телефона маскируется при отображении.

### Процесс создания платежа

1. **Загрузка организаций отправителя:** `GET /api/roles/by-xpin` → для каждого uPin: `GET /api/organizations/{uPin}` и `GET /api/accounts/{uPin}`
2. **Выбор организации и счёта** (выпадающие списки, автовыбор если одна)
3. **Поиск получателя** по названию/ИНН/КПП: `GET /api/organizations/search` (debounce 300мс, выпадающий список)
4. **Выбор банка получателя:** `GET /api/accounts/get-bank-info` (автозаполнение)
5. **Ввод номера счёта получателя** (20 цифр)
6. **Ввод суммы** (форматирование в рублях)
7. `POST /api/payments/create` → платёж в статусе `N`
8. **Модалка подписания:** ввод СМС-кода `000000` → `POST /api/sign/init` → `POST /api/sign/confirm`
9. При успехе → статус платежа `S`, переход на `/success`

### Страница «Все платежи»

- Таблица с колонками: reference, сумма, дата создания, статус
- Пагинация по 10 записей на странице
- Клик по строке → **модал деталей платежа**:
  - Информация об отправителе: организация (название, ИНН, КПП), счёт
  - Информация о получателе: организация (название, ИНН, КПП), банк (если указан), счёт
  - Сумма, дата, время, статус
  - **Кнопка «Подписать»** — для платежей в статусе `N` (открывает модалку подписания)
  - **Кнопка «Повторить»** — для платежей в статусе `S` (переход на `/create-payment` с предзаполненными данными)

### Маппинг статусов в UI

| Код | Отображение |
|-----|-------------|
| N | Новый |
| C | На подписании |
| S | Успешно |
| M | Заблокирован |
| F | Ошибка |