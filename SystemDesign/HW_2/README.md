# TigerBank — Модуль учёта финансов

## Предметная область

Модуль личного учёта финансов для банковского приложения ТигрБанк. Программа моделирует три сущности: банковский счёт (`BankAccount`) с именем и балансом, категорию операции (`Category`) с типом INCOME или EXPENSE, и финансовую операцию (`Operation`), привязанную к счёту и категории. Баланс счёта пересчитывается автоматически при каждом изменении операций. Поддерживается аналитика по периодам, экспорт и импорт данных в трёх форматах.

---

## Реализованный функционал

| Функция | Класс | Метод |
|---|---|---|
| Создать счёт | `BankAccountServiceImpl` | `create(id, name)`, `create(id, name, balance)` |
| Переименовать счёт | `BankAccountServiceImpl` | `updateName(id, newName)` |
| Удалить счёт | `BankAccountServiceImpl` | `delete(id)` |
| Создать категорию | `CategoryServiceImpl` | `create(id, type, name)` |
| Переименовать категорию | `CategoryServiceImpl` | `updateName(id, newName)` |
| Удалить категорию | `CategoryServiceImpl` | `delete(id)` |
| Создать операцию | `OperationServiceImpl` | `create(...)` — два варианта: с описанием и без |
| Изменить сумму операции | `OperationServiceImpl` | `updateAmount(id, newAmount)` |
| Изменить дату операции | `OperationServiceImpl` | `updateDate(id, newDate)` |
| Изменить описание операции | `OperationServiceImpl` | `updateDescription(id, newDescription)` |
| Удалить операцию | `OperationServiceImpl` | `delete(id)` |
| Автопересчёт баланса | `OperationServiceImpl` | `recalculateBalance(accountId)` — вызывается после каждого create/update/delete |
| Разница доходов и расходов за период | `AnalyticalServiceImpl` | `calculateDifference(accountId, from, to)` |
| Группировка по категориям за период | `AnalyticalServiceImpl` | `groupByCategory(accountId, from, to)` |
| Экспорт в JSON | `JsonExporter` | `export(data, filePath)` |
| Импорт из JSON | `JsonImporter` | `importData(filePath)` |
| Экспорт в CSV | `CsvExporter` | `export(data, filePath)` |
| Импорт из CSV | `CsvImporter` | `importData(filePath)` |
| Экспорт в YAML | `YamlExporter` | `export(data, filePath)` |
| Импорт из YAML | `YamlImporter` | `importData(filePath)` |
| Замер времени | `TimingAspect` | перехватывает методы с аннотацией `@Timed` |

---

## SOLID

### S — Single Responsibility

Каждый класс имеет одну причину для изменения:

- `BankAccountServiceImpl` — только бизнес-логика счетов: создание, переименование, удаление, поиск. Не занимается хранением, аналитикой или экспортом.
- `CategoryServiceImpl` — только бизнес-логика категорий. Не знает про операции или счета.
- `OperationServiceImpl` — бизнес-логика операций и пересчёт баланса. Пересчёт баланса живёт здесь, а не в `BankAccountServiceImpl`, потому что именно операции являются источником изменения баланса.
- `AnalyticalServiceImpl` — только аналитика. Не знает про репозитории, работает исключительно через `OperationService` и `CategoryService`. Если изменится хранилище — этот класс не трогаем.
- `InMemoryBankAccountRepository`, `InMemoryCategoryRepository`, `InMemoryOperationRepository` — только хранение данных в памяти. Не содержат никакой бизнес-логики.
- `JsonExporter`, `CsvExporter`, `YamlExporter` — каждый отвечает только за запись в своём формате.
- `JsonImporter`, `CsvImporter`, `YamlImporter` — каждый отвечает только за чтение своего формата. Экспорт и импорт намеренно разделены на разные классы.
- `TimingAspect` — только замер времени. Логика замера полностью вынесена из сервисов через AOP. Если нужно изменить формат вывода — меняем только `TimingAspect`, бизнес-логика не затронута.

### O — Open/Closed

Система открыта для расширения и закрыта для изменения:

- Интерфейсы `DataExporter` и `DataImporter` позволяют добавлять новые форматы без изменения существующего кода. YAML-поддержка была добавлена созданием двух новых классов — `YamlExporter` и `YamlImporter` — без изменения ни одной строки в `JsonExporter`, `CsvExporter` или `Main`.
- Интерфейсы `BankAccountRepository`, `CategoryRepository`, `OperationRepository` позволяют менять реализацию хранилища (например, перейти на PostgreSQL) без изменения сервисов.
- `AnalyticalServiceImpl` можно заменить другой реализацией `AnalyticalService` (например, с кэшированием) без изменения `Main` или других сервисов.

```java
// Добавить XML — только новый класс, ничего существующего не меняем:
public class XmlExporter implements DataExporter {
    public void export(ExportData data, String filePath) { ... }
}
```

### L — Liskov Substitution

Любую реализацию интерфейса можно подставить вместо другой без нарушения поведения системы:

- `InMemoryBankAccountRepository` полностью заменяет `BankAccountRepository` — `BankAccountServiceImpl` не знает и не должен знать, какая реализация подставлена.
- Аналогично для `InMemoryCategoryRepository` → `CategoryRepository` и `InMemoryOperationRepository` → `OperationRepository`.
- В тестах это проверяется напрямую: сервисы создаются с `new InMemoryBankAccountRepository()` без Spring — поведение идентично поведению в продакшн-контексте.
- `JsonExporter`, `CsvExporter`, `YamlExporter` — все три реализуют `DataExporter` и взаимозаменяемы. `Main` работает с любым из них через один и тот же вызов `exporter.export(data, path)`.

### I — Interface Segregation

Интерфейсы узкие и сфокусированные — клиент получает только то, что ему нужно:

- `DataExporter` и `DataImporter` — два отдельных интерфейса. `JsonExporter` реализует только `DataExporter` и ничего не знает про импорт. Если нужен только экспорт — не нужно реализовывать импорт.
- `BankAccountService`, `CategoryService`, `OperationService`, `AnalyticalService` — четыре отдельных интерфейса. `AnalyticalServiceImpl` зависит только от `OperationService` и `CategoryService` — не от полного `BankAccountService`.
- `BankAccountRepository`, `CategoryRepository`, `OperationRepository` — у каждого свой набор методов, соответствующий именно этой сущности. `OperationRepository` имеет `findByBankAccountId` и `findByDateBetween`, которых нет в других репозиториях.

### D — Dependency Inversion

Модули высокого уровня зависят от абстракций, а не от конкретных реализаций:

- `BankAccountServiceImpl` зависит от интерфейса `BankAccountRepository`, а не от `InMemoryBankAccountRepository`.
- `OperationServiceImpl` зависит от трёх интерфейсов: `OperationRepository`, `BankAccountRepository`, `CategoryRepository` — ни один конкретный класс не упоминается.
- `AnalyticalServiceImpl` зависит от интерфейсов `OperationService` и `CategoryService`.

```java
// OperationServiceImpl — все зависимости через интерфейсы:
private final OperationRepository operationRepository;
private final BankAccountRepository bankAccountRepository;
private final CategoryRepository categoryRepository;
```

Spring IoC подставляет конкретные реализации через `@Service`, `@Repository`, `@RequiredArgsConstructor`. Нигде в бизнес-логике нет `new InMemory...()`.

---

## DI-контейнер

Используется Spring IoC (`AnnotationConfigApplicationContext`). Контекст поднимается в `Main.java`:

```java
var context = new AnnotationConfigApplicationContext("tigerbank");
```

Spring сканирует пакет `tigerbank` и находит:
- `@Repository` — `InMemoryBankAccountRepository`, `InMemoryCategoryRepository`, `InMemoryOperationRepository`
- `@Service` — `BankAccountServiceImpl`, `CategoryServiceImpl`, `OperationServiceImpl`, `AnalyticalServiceImpl`
- `@Component` — `JsonExporter`, `JsonImporter`, `CsvExporter`, `CsvImporter`, `YamlExporter`, `YamlImporter`, `TimingAspect`

Зависимости связываются через `@RequiredArgsConstructor` — Lombok генерирует конструктор по всем `final` полям, Spring его вызывает и подставляет нужные бины. Ни один объект не создаётся через `new` в бизнес-логике.

---

## Замер времени

Аннотация `@Timed` (`tigerbank.aspect.Timed`) навешена на методы `create` и `delete` в `BankAccountServiceImpl`, `CategoryServiceImpl`, `OperationServiceImpl`.

`TimingAspect` перехватывает вызов через `@Around`:

```java
@Around("@annotation(Timed)")
public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.nanoTime();
    Object result = joinPoint.proceed();
    double ms = (System.nanoTime() - start) / 1_000_000.0;
    System.out.printf("[Timed] %s — %.3f мс%n", methodName, ms);
    return result;
}
```

Пример вывода при создании счёта:
```
[Timed] BankAccountServiceImpl.create — 0.142 мс
```

Аспект работает только с объектами, полученными через Spring-контекст (`context.getBean(...)`), не через `new`.

---

## Тестирование

47 модульных тестов на JUnit 5, без Spring-контекста:

| Класс | Тестов | Что проверяется |
|---|---|---|
| `BankAccountServiceTest` | 9 | create (с балансом и без), дубликат id, findById (существующий и нет), updateName, delete (существующий и нет), findAll |
| `CategoryServiceTest` | 9 | create, дубликат id, findById, findAll, findByType, updateName, delete, ошибки на несуществующих |
| `OperationServiceTest` | 16 | create (с описанием и без), дубликат id, несуществующий счёт, несуществующая категория, несовпадение типов, updateAmount/Date/Description, delete, пересчёт баланса, findByBankAccountId, findByDateBetween, recalculateBalance |
| `AnalyticalServiceTest` | 6 | calculateDifference за период с доходом и расходом, только расход, пустой период, пустой счёт; groupByCategory — корректные суммы, пустой период |
| `ExportImportTest` | 7 | полный цикл export→import для JSON/CSV/YAML, сохранение null описания, сохранение описания, ошибки при неверном пути |

---

## Проблемы при расширении

1. **Переход на реальную БД — методы update** — `updateName` в `BankAccountServiceImpl` и `CategoryServiceImpl`, `updateAmount`/`updateDate`/`updateDescription` в `OperationServiceImpl` изменяют поле объекта через `setX()`, но не вызывают `repository.save()`. В InMemory это работает, потому что HashMap хранит ссылку на тот же объект — изменение поля сразу видно. В реальной БД объект является локальной копией, полученной из БД, и без явного `save()` изменения никогда не попадут обратно. Потребуется добавить `repository.save(entity)` после каждого `set` во всех методах update.

2. **`recalculateBalance` в `OperationService`** напрямую вызывает `account.setBalance()` через `BankAccountRepository`. Если в будущем логика баланса усложнится (транзакции, лимиты), это место станет узким местом.

3. **Строковые id** — уникальность не гарантируется. Пользователь может создать счёт с id `""` или с пробелом. При масштабировании стоит перейти на UUID с автогенерацией.

---

## Почему абстракции улучшают дизайн

- **Интерфейсы репозиториев** (`BankAccountRepository`, `CategoryRepository`, `OperationRepository`) позволяют тестировать сервисы без Spring и без файловой системы. В 47 тестах ни разу не поднимается контекст — каждый сервис тестируется изолированно с `new InMemory...()`.

- **`DataExporter` / `DataImporter`** — если потребуется добавить новый формат (например, XML), достаточно создать два новых класса, реализующих эти интерфейсы. Ни один существующий класс не будет затронут.

- **`AnalyticalService` отделён от CRUD** — аналитику можно переписать (поменять алгоритм группировки, добавить кэш) без риска сломать создание/удаление операций.

- **`TimingAspect` через AOP** — если убрать замер времени, достаточно удалить один класс и аннотации. Бизнес-логика не изменится ни на строчку.

- **Разделение интерфейсов сервисов** — `AnalyticalServiceImpl` зависит только от `OperationService` и `CategoryService`, а не от полного монолитного сервиса. Это упрощает понимание зависимостей и замену отдельных частей.
