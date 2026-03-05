# Jackson - Справочник по аннотациям

Jackson - библиотека для сериализации/десериализации JSON в Java. Входит в Spring Boot по умолчанию.

---

## Аннотации сериализации (Java -> JSON)

### @JsonProperty

Задаёт имя поля в JSON, отличное от имени поля в Java.

```java
public class SignDto {
    @JsonProperty("xPin")
    private String xPin;  // без аннотации Jackson сериализует как "xpin"
}
```

```json
{ "xPin": "XAAAAA" }
```

**Зачем нужно в проекте:** Lombok генерирует геттер `getXPin()`, который Jackson по умолчанию интерпретирует как поле `"xpin"`. Аннотация `@JsonProperty("xPin")` фиксирует правильное имя.

### @JsonIgnore

Исключает поле из сериализации и десериализации.

```java
public class UserDto {
    private String id;
    @JsonIgnore
    private String internalSecret;  // не попадёт в JSON
}
```

### @JsonIgnoreProperties

Игнорирует указанные поля на уровне класса. Полезно, если в JSON приходят поля, которых нет в классе.

```java
@JsonIgnoreProperties(ignoreUnknown = true)  // игнорировать неизвестные поля при десериализации
public class PaymentDto {
    private String reference;
    private String status;
}
```

```java
@JsonIgnoreProperties({"createdAt", "updatedAt"})  // игнорировать конкретные поля
public class PaymentDto {
    private String reference;
    private String status;
    private LocalDateTime createdAt;   // не сериализуется
    private LocalDateTime updatedAt;   // не сериализуется
}
```

### @JsonInclude

Управляет, когда поле включается в JSON.

```java
@JsonInclude(JsonInclude.Include.NON_NULL)  // не включать null-поля
public class AccountDto {
    private String accountNumberFull;
    private String currency;
    private String description;  // если null — не попадёт в JSON
}
```

Варианты:
| Значение | Описание |
|----------|----------|
| `ALWAYS` | Всегда включать (по умолчанию) |
| `NON_NULL` | Исключить null |
| `NON_EMPTY` | Исключить null, пустые строки, пустые коллекции |
| `NON_DEFAULT` | Исключить значения по умолчанию (0, false, null) |

### @JsonPropertyOrder

Задаёт порядок полей в JSON.

```java
@JsonPropertyOrder({"reference", "status", "amount"})
public class PaymentDto {
    private BigDecimal amount;
    private String status;
    private String reference;  // будет первым в JSON
}
```

---

## Аннотации десериализации (JSON -> Java)

### @JsonCreator + @JsonProperty

Десериализация через конструктор (вместо сеттеров).

```java
public class RoleRequest {
    private final String xPin;
    private final String uPin;
    private final String rule;

    @JsonCreator
    public RoleRequest(
            @JsonProperty("xPin") String xPin,
            @JsonProperty("uPin") String uPin,
            @JsonProperty("rule") String rule) {
        this.xPin = xPin;
        this.uPin = uPin;
        this.rule = rule;
    }
}
```

### @JsonAlias

Позволяет десериализовать поле из нескольких альтернативных имён.

```java
public class AccountCreateRequest {
    @JsonAlias({"uPin", "u_pin", "organizationId"})
    private String uPin;  // примет любое из трёх имён
}
```

### @JsonSetter / @JsonGetter

Привязка к конкретному сеттеру/геттеру.

```java
public class PaymentDto {
    private String status;

    @JsonGetter("paymentStatus")
    public String getStatus() { return status; }

    @JsonSetter("status")
    public void setStatus(String status) { this.status = status; }
}
```

Результат: при сериализации — `"paymentStatus"`, при десериализации — принимает `"status"`.

---

## Аннотации для дат и форматов

### @JsonFormat

Задаёт формат сериализации для дат, чисел и т.д.

```java
public class PaymentDto {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;  // сериализуется как строка: "4200.00"
}
```

---

## Работа с вложенными объектами

### @JsonUnwrapped

Встраивает поля вложенного объекта на верхний уровень.

```java
public class PaymentDto {
    private String reference;

    @JsonUnwrapped
    private PaymentFromDto from;
}

public class PaymentFromDto {
    private String userId;
    private String organizationId;
}
```

Без `@JsonUnwrapped`:
```json
{ "reference": "P15...", "from": { "userId": "X...", "organizationId": "U..." } }
```

С `@JsonUnwrapped`:
```json
{ "reference": "P15...", "userId": "X...", "organizationId": "U..." }
```

### @JsonManagedReference / @JsonBackReference

Решают проблему циклических ссылок (например, parent <-> child).

```java
public class Organization {
    @JsonManagedReference
    private List<Account> accounts;  // сериализуется
}

public class Account {
    @JsonBackReference
    private Organization organization;  // НЕ сериализуется (избегает цикл)
}
```

---

## JsonNode (программная работа с JSON)

Используется для работы с JSON без привязки к конкретному классу.

```java
// Десериализация ответа в JsonNode
ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
JsonNode body = response.getBody();

// Чтение полей
String value = body.get("fieldName").asText();
boolean flag = body.get("hasPermission").asBoolean();
int number = body.get("count").asInt();

// Проверка наличия поля
if (body.has("optionalField")) { ... }

// Обход массива
for (JsonNode item : body.get("items")) {
    String id = item.get("id").asText();
}
```

**Пример из проекта** (`RoleClient.java`):
```java
ResponseEntity<JsonNode> response = restTemplate.getForEntity(
        baseUrl + "/api/roles/check?xPin=" + xPin + "&uPin=" + uPin + "&rule=" + rule,
        JsonNode.class);
return response.getBody().get("hasPermission").asBoolean();
```

---

## ObjectMapper

Центральный класс Jackson для ручной сериализации/десериализации.

```java
ObjectMapper mapper = new ObjectMapper();

// Java -> JSON (строка)
String json = mapper.writeValueAsString(paymentDto);

// JSON -> Java
PaymentDto dto = mapper.readValue(json, PaymentDto.class);

// JSON -> JsonNode
JsonNode node = mapper.readTree(json);

// Файл -> Java
PaymentDto dto = mapper.readValue(new File("payment.json"), PaymentDto.class);
```
