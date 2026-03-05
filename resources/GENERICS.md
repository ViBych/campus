# Java Generics - Справочник

Дженерики (обобщения) позволяют писать код, который работает с разными типами данных, сохраняя при этом типобезопасность на этапе компиляции.

---

## Дженерик-метод

```java
protected <T> T post(String path, Class<T> responseType) {
    return given().spec(requestSpec)
        .when()
            .post(path)
        .then()
            .statusCode(200)
            .extract().as(responseType);
}
```

Разберём синтаксис по частям:

```
protected <T> T post(String path, Class<T> responseType)
          ───  ─                  ────────
           │   │                      │
           │   │                      └─ параметр: «класс типа T»
           │   └─ возвращаемый тип: «объект типа T»
           └─ объявление: «метод параметризован типом T»
```

Когда метод вызывается, компилятор **сам выводит** тип `T` из аргумента:

```java
// Class<UserResponse> → T = UserResponse → возвращается UserResponse
UserResponse user = post("/users/generate", UserResponse.class);

// Class<OrganizationResponse> → T = OrganizationResponse
OrganizationResponse org = post("/organizations/generate", OrganizationResponse.class);
```

Ошибка типа ловится **при компиляции**, а не в рантайме:

```java
// Не скомпилируется! Метод вернёт UserResponse, а не PaymentResponse
PaymentResponse oops = post("/users/generate", UserResponse.class);  // ✗ compile error
```

---

## Аналогия: коробка

### Без дженериков — коробка для чего угодно

```java
class Box {
    private Object value;

    void put(Object value) { this.value = value; }
    Object get() { return value; }
}

Box box = new Box();
box.put("hello");
String s = (String) box.get();       // каст вручную
Integer i = (Integer) box.get();     // скомпилируется, но упадёт в рантайме 
```

### С дженериком — коробка для конкретного типа

```java
class Box<T> {
    private T value;

    void put(T value) { this.value = value; }
    T get() { return value; }
}

Box<String> box = new Box<>();
box.put("hello");
String s = box.get();                // каст не нужен
box.put(42);                         // не скомпилируется ✗
```


