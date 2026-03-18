# TigerBank — Модуль учёта финансов

## Предметная область

Модуль личного учёта финансов для банковского приложения ТигрБанк. Программа моделирует три сущности: банковский счёт (`BankAccount`) с именем и балансом, категорию операции (`Category`) с типом INCOME или EXPENSE, и финансовую операцию (`Operation`), привязанную к счёту и категории. Баланс счёта пересчитывается автоматически при каждом изменении операций. Поддерживается аналитика по периодам, экспорт и импорт данных в трёх форматах.

---

## Реализованный функционал

| Функция | Класс | Метод |
|---|---|---|
| Создать счёт | `BankAccountFacade` | `create(id, name)` |
| Переименовать счёт | `BankAccountFacade` | `updateName(id, newName)` |
| Удалить счёт | `BankAccountFacade` | `delete(id)` — с проверкой привязанных операций |
| Создать категорию | `CategoryFacade` | `create(id, type, name)` |
| Переименовать категорию | `CategoryFacade` | `updateName(id, newName)` |
| Удалить категорию | `CategoryFacade` | `delete(id)` — с проверкой привязанных операций |
| Создать операцию | `OperationFacade` | `create(...)` — с валидацией и пересчётом баланса |
| Изменить сумму операции | `OperationFacade` | `updateAmount(id, newAmount)` — с пересчётом баланса |
| Изменить дату операции | `OperationFacade` | `updateDate(id, newDate)` |
| Изменить описание операции | `OperationFacade` | `updateDescription(id, newDescription)` |
| Удалить операцию | `OperationFacade` | `delete(id)` — с пересчётом баланса |
| Разница доходов и расходов за период | `AnalyticsFacade` | `calculateDifference(accountId, from, to)` |
| Группировка по категориям за период | `AnalyticsFacade` | `groupByCategory(accountId, from, to)` |
| Экспорт в JSON/CSV/YAML | `ExportCommand` | `execute()` — через `DataExporter` |
| Импорт из JSON/CSV/YAML | `ImportCommand` | `execute()` — через `DataImporter` |
| Замер времени | `CommandExecutor` | оборачивает каждый `Command.execute()` |

---

## Архитектура

### Основные классы

| Пакет | Классы | Роль |
|---|---|---|
| `domain` | `BankAccount`, `Category`, `Operation`, `OperationType` | Доменная модель |
| `factory` | `BankAccountFactory`, `CategoryFactory`, `OperationFactory` | Создание доменных объектов |
| `repository` | `BankAccountRepository`, `CategoryRepository`, `OperationRepository` + InMemory-реализации | Хранение данных |
| `service` | `BankAccountServiceImpl`, `CategoryServiceImpl`, `OperationServiceImpl`, `AnalyticalServiceImpl`, `BalanceServiceImpl` | Бизнес-логика |
| `validator` | `OperationValidator` | Валидация при создании операции |
| `facade` | `BankAccountFacade`, `CategoryFacade`, `OperationFacade`, `AnalyticsFacade` | Оркестрация сервисов, единая точка входа для `Main` |
| `command` | `Command`, `CommandExecutor`, 21 команда | Пользовательские сценарии |
| `export` | `DataExporter`, `DataImporter`, `AbstractImporter`, `ExportData`, Json/Csv/Yaml реализации | Экспорт и импорт данных |
| `aspect` | `Timed`, `TimingAspect` | AOP-замер времени |

---

## Паттерны GoF

### Factory Method

`BankAccountFactory`, `CategoryFactory`, `OperationFactory` инкапсулируют создание доменных объектов. Сервисы получают готовый объект от фабрики, не зная деталей его построения. Фабрика отвечает за валидацию входных данных (не null, не blank); сервис — за бизнес-правила (например, уникальность id).

```java
@Component
public class BankAccountFactory {
    public BankAccount create(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id не может быть пустым");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name не может быть пустым");
        return new BankAccount(id, name);
    }
}
```

Точка расширения: при появлении `SavingsAccount` или `CreditAccount` достаточно добавить подклассы фабрики, не трогая сервисы.

---

### Facade

Четыре фасада (`BankAccountFacade`, `CategoryFacade`, `OperationFacade`, `AnalyticsFacade`) — единственный слой, который видит `Main`. Сервисный слой скрыт за фасадами.

**`OperationFacade`** оркестрирует три шага при создании операции: валидацию, сохранение и пересчёт баланса. `updateAmount` и `delete` также вызывают `balanceService.recalculate` после изменения.

```java
public void create(String id, OperationType type, String bankAccountId,
                   BigDecimal amount, LocalDate date, String description, String categoryId) {
    validator.validate(id, bankAccountId, categoryId, type);  // (1) валидация
    operationService.create(...);                              // (2) сохранение
    balanceService.recalculate(bankAccountId);                 // (3) пересчёт баланса
}
```

**`BankAccountFacade`** и **`CategoryFacade`** защищают от удаления сущности, на которую есть привязанные операции: один сервис проверяет условие, другой выполняет действие.

```java
public void delete(String id) {
    if (!operationService.findByBankAccountId(id).isEmpty()) {
        throw new IllegalStateException("Нельзя удалить счёт " + id + ": существуют привязанные операции");
    }
    bankAccountService.delete(id);
}
```

**`AnalyticsFacade`** скрывает от `Main` существование `AnalyticalService` — без него `Main` должен был бы знать о двух отдельных сервисах и сам решать, когда какой вызывать.

---

### Command

Каждый пользовательский сценарий — отдельный Command-класс. Всего 21 команда в пакетах `account`, `category`, `operation`, `analytics`, `export`.

| Роль | Класс |
|---|---|
| Command (интерфейс) | `Command` |
| ConcreteCommand (×21) | `CreateAccountCommand`, `DeleteCategoryCommand`, ... |
| Invoker | `CommandExecutor` |
| Receiver | Фасады (`BankAccountFacade`, ...) |
| Client | `Main` |

`CommandExecutor` не знает, что делает команда — только измеряет время выполнения:

```java
public void execute(Command command) {
    long start = System.nanoTime();
    command.execute();
    long elapsed = System.nanoTime() - start;
    System.out.printf("[%.2f ms]%n", elapsed / 1_000_000.0);
}
```

Все команды используют `@RequiredArgsConstructor` — Lombok генерирует конструктор по `final`-полям:

```java
@RequiredArgsConstructor
public class CreateAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final String id;
    private final String name;

    @Override
    public void execute() {
        facade.create(id, name);
    }
}
```

---

### Template Method

`JsonImporter`, `CsvImporter`, `YamlImporter` наследуют `AbstractImporter`. Чтение файла одинаково для всех форматов, парсинг — уникален.

```java
public abstract class AbstractImporter implements DataImporter {

    @Override
    public final ExportData importData(String filePath) {
        String content = readFile(filePath);  // общий шаг — в базовом классе
        return parse(content);                // вариантный шаг — в подклассе
    }

    private String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    protected abstract ExportData parse(String content);
}
```

`importData` — `final`, алгоритм нельзя изменить в подклассе. `readFile` — `private`, подкласс не знает о его существовании. `parse` — единственная точка расширения. Каждый из трёх подклассов реализует только её:

```java
@Component
public class JsonImporter extends AbstractImporter {
    @Override
    protected ExportData parse(String content) {
        try { return mapper.readValue(content, ExportData.class); }
        catch (IOException e) { throw new RuntimeException("Ошибка при парсинге JSON", e); }
    }
}
// CsvImporter и YamlImporter — аналогично, только парсер другой
```

---

## SOLID

### S — Single Responsibility

- `BankAccountServiceImpl` — создание, переименование, удаление, поиск счетов. Баланс не трогает.
- `BalanceServiceImpl` — только пересчёт баланса по всем операциям счёта.
- `OperationValidator` — только валидация при создании операции: существование счёта, категории, совпадение типов, уникальность id.
- `CommandExecutor` — только замер времени, без бизнес-логики.
- `JsonExporter`, `CsvExporter`, `YamlExporter` — каждый пишет только свой формат.
- `AbstractImporter` — только чтение файла; `JsonImporter`, `CsvImporter`, `YamlImporter` — только парсинг.

### O — Open/Closed

`DataExporter` и `DataImporter` позволяют добавить новый формат без изменения существующего кода:

```java
// Добавить XML — только новый класс:
public class XmlExporter implements DataExporter {
    public void export(ExportData data, String filePath) { ... }
}
```

Аналогично `Command` — новый сценарий = новый класс, `CommandExecutor` и `Main` не меняются.

### L — Liskov Substitution

- `InMemoryBankAccountRepository` полностью заменяет `BankAccountRepository` — `BankAccountServiceImpl` не знает о конкретной реализации.
- `JsonImporter`, `CsvImporter`, `YamlImporter` взаимозаменяемы через `DataImporter` — `ImportCommand` работает с любым через `importer.importData(path)`.
- В тестах сервисы создаются с `new InMemory...()` без Spring — поведение идентично продакшн-контексту.

### I — Interface Segregation

- `DataExporter` и `DataImporter` — раздельные интерфейсы. `JsonExporter` реализует только экспорт.
- `BankAccountService`, `CategoryService`, `OperationService`, `AnalyticalService`, `BalanceService` — пять отдельных интерфейсов, каждый по своей области.

### D — Dependency Inversion

- Все сервисы зависят от интерфейсов репозиториев, а не от `InMemory`-реализаций.
- `OperationFacade` зависит от `OperationService`, `OperationValidator`, `BalanceService` — интерфейсы.
- Spring IoC подставляет реализации через `@Service`, `@Repository`, `@Component` и `@RequiredArgsConstructor`. В бизнес-логике нет `new InMemory...()`.

---

## GRASP

### High Cohesion

- `AnalyticalServiceImpl` — только `calculateDifference` и `groupByCategory`, без CRUD и без знания о репозиториях.
- `BalanceServiceImpl` — только `recalculate(bankAccountId)`. Единственный класс, где меняется баланс.
- `OperationValidator` — только проверки. Не сохраняет данные, не вычисляет баланс.
- Каждая команда делает ровно одно действие — `CreateAccountCommand.execute()` вызывает только `facade.create(id, name)`.

### Low Coupling

До выделения `BalanceService` и `OperationValidator` сервис `OperationServiceImpl` зависел от `BankAccountRepository`, `CategoryRepository` и `OperationRepository` одновременно. После разделения каждый класс знает только о тех зависимостях, которые нужны для его работы:

- `OperationServiceImpl` — только `OperationRepository` и `OperationFactory`.
- `OperationValidator` — `BankAccountService`, `CategoryService`, `OperationRepository`.
- `BalanceServiceImpl` — `BankAccountService`, `OperationRepository`.

Фасады — единственное место, где несколько сервисов взаимодействуют. Командные классы зависят только от фасадов.

---

## DI-контейнер

Используется Spring IoC (`AnnotationConfigApplicationContext`):

```java
var context = new AnnotationConfigApplicationContext("tigerbank");
```

Spring сканирует пакет `tigerbank` и регистрирует:
- `@Repository` — `InMemoryBankAccountRepository`, `InMemoryCategoryRepository`, `InMemoryOperationRepository`
- `@Service` — `BankAccountServiceImpl`, `CategoryServiceImpl`, `OperationServiceImpl`, `AnalyticalServiceImpl`, `BalanceServiceImpl`
- `@Component` — фабрики, фасады, `OperationValidator`, импортеры, экспортеры, `CommandExecutor`

Зависимости связываются через `@RequiredArgsConstructor` — Lombok генерирует конструктор по всем `final`-полям, Spring его вызывает. В бизнес-логике нет `new InMemory...()`.

---

## Замер времени

`CommandExecutor` оборачивает каждый вызов команды:

```
> Создать счёт
Счёт acc1 создан.
[0.84 ms]
```

Замер сосредоточен в Invoker (`CommandExecutor`). Аннотация `@Timed` и `TimingAspect` сохранены для точечного замера внутри сервисов при необходимости.

---

## Тестирование

| Класс | Тестов | Что проверяется |
|---|---|---|
| `BankAccountServiceTest` | 9 | create, дубликат id, findById, updateName, delete, findAll |
| `CategoryServiceTest` | 9 | create, дубликат id, findById, findAll, findByType, updateName, delete |
| `OperationServiceTest` | 12 | create (с описанием и без), updateAmount/Date/Description, delete, findByBankAccountId, findByDateBetween |
| `AnalyticalServiceTest` | 6 | calculateDifference, groupByCategory — за период, пустой период |
| `ExportImportTest` | 8 | полный цикл export→import для JSON/CSV/YAML, null description, ошибки пути |
| `BankAccountFactoryTest` | 5 | create, null/blank id, null/blank name |
| `CategoryFactoryTest` | 4 | create, null/blank id, null name |
| `OperationFactoryTest` | 6 | create с описанием и без, null/blank поля |
| `BankAccountFacadeTest` | 3 | delete с операциями (запрет), delete без операций |
| `CategoryFacadeTest` | 3 | delete с операциями (запрет), delete без операций |
| `OperationFacadeTest` | 3 | create/updateAmount/delete пересчитывают баланс |
| `AbstractImporterTest` | 2 | успешный импорт, ошибка при неверном пути |
| `CommandExecutorTest` | 2 | команда выполняется, время выводится |

---

## Архитектурные дилеммы при проектировании

### Где должна жить логика пересчёта баланса?

Первоначально пересчёт баланса находился в `OperationServiceImpl`, который для этого зависел от `BankAccountRepository` и `CategoryRepository`. Это нарушало Low Coupling: сервис операций был связан с хранилищем счетов, хотя его основная ответственность — CRUD операций.

Рассматривалось три варианта:
- оставить в `OperationServiceImpl` — нарушение SRP и Low Coupling;
- перенести в `BankAccountServiceImpl` — нарушение SRP (счёт не должен знать про операции);
- вынести в отдельный `BalanceService` — принятое решение.

`BalanceService` зависит от `BankAccountService` и `OperationRepository` напрямую (не через `OperationService`), потому что ему нужны только сырые данные для агрегации, а не бизнес-логика сервиса.

### Нужен ли Request-объект или Builder для создания операции?

`Operation` имеет 7 полей. При создании через фабрику сигнатура метода выглядит так:

```java
operationFactory.create(id, type, bankAccountId, amount, date, description, categoryId)
```

Рассматривался вариант с `OperationRequest` (или Builder) — объект-обёртка для передачи параметров, который исключает путаницу при большом числе аргументов одного типа. Решение против: для текущей модели это over-engineering — поля разнотипные, перепутать `String id` и `String bankAccountId` сложнее, чем `String firstName` и `String lastName`. `OperationRequest` оправдан при появлении опциональных полей или нескольких вариантов создания объекта.

### Нужен ли AnalyticsFacade или достаточно AnalyticalService?

`AnalyticalServiceImpl` уже зависит от `OperationService` и `CategoryService` — он сам по себе агрегирует данные из нескольких источников. Возникал вопрос: не является ли он уже фасадом?

Разница принципиальная: `AnalyticalService` — это вычислительная логика (алгоритм группировки, агрегация сумм). `AnalyticsFacade` — это точка входа, скрывающая существование `AnalyticalService` от `Main`. Без `AnalyticsFacade` `Main` должен был бы знать о двух разных сервисах — аналитическом и счетовом — и сам решать, когда какой вызывать.

### Нужен ли фасад, если он только делегирует?

Большинство методов `BankAccountFacade` и `CategoryFacade` — чистое делегирование в сервис:

```java
public void create(String id, String name) {
    bankAccountService.create(id, name);  // просто передаёт вызов
}
```

Это можно считать избыточным. Ценность такого фасада — в единой точке входа: `Main` работает исключительно с фасадами и не знает о существовании сервисного слоя. При добавлении новой логики в `create` (например, аудит-лог или нотификация) достаточно изменить один метод в фасаде, не трогая команды и не меняя `Main`.

---

## Проблемы при расширении

1. **Переход на реальную БД** — методы `updateName`, `updateAmount`, `updateDate`, `updateDescription` изменяют поле объекта через `setX()` без вызова `repository.save()`. В InMemory это работает, потому что `HashMap` хранит ссылку на тот же объект. В реальной БД объект — локальная копия, изменения не попадут обратно без явного `save()`.

2. **Добавление новых полей в доменные объекты** — если в `Operation` появится новое поле (например, `currency`), придётся менять `OperationFactory`, `OperationService` (сигнатуры методов create), `OperationFacade`, все команды, которые передают параметры при создании, и тесты. Применение Builder-паттерна или Request-объекта (`OperationRequest`) локализовало бы изменение: добавить поле в один класс, остальные вызовы продолжили бы компилироваться.

3. **Пересчёт баланса при каждом изменении** — `BalanceService.recalculate` проходит по всем операциям счёта при каждом `create`/`updateAmount`/`delete`. При большом числе операций это становится проблемой производительности. Решения: инкрементальный пересчёт (прибавить/вычесть только дельту) или кэш с инвалидацией.

4. **Main создаёт команды через `new`** — при добавлении нового сценария требуется менять `Main`. При большом числе команд `Main` превращается в класс с нарушением SRP. Решение — вынести меню в отдельный класс-контроллер, управляемый Spring.

---

## Почему абстракции улучшают дизайн

**Интерфейсы репозиториев** полностью изолируют бизнес-логику от хранилища. Сервисы тестируются без Spring — в 68 тестах контекст не поднимается ни разу: каждый сервис создаётся с `new InMemoryBankAccountRepository()` напрямую. При переходе на PostgreSQL меняется только реализация репозитория, все сервисы и тесты остаются нетронутыми.

**`DataExporter` / `DataImporter`** — добавить новый формат (XML, Protobuf) = создать два новых класса. `ExportCommand` и `ImportCommand` принимают интерфейс — смена формата не требует изменения команд, фасадов или сервисов.

**`Command` интерфейс** отделяет формирование запроса от его выполнения. `CommandExecutor` не знает, что делает команда. Любую сквозную логику — логирование, undo, очередь выполнения — можно добавить в Invoker без изменения ни одной команды.

**`AbstractImporter`** — логика чтения файла существует в одном месте. Замена способа чтения (например, буферизованное чтение или чтение из classpath) затронет одну строку в одном классе, а не три копии в трёх импортерах.

**`OperationValidator`** изолирует все проверки при создании операции в одном месте. Добавление новой проверки (например, лимит суммы) требует изменения только `OperationValidator` — фасад, сервис и команды не меняются.

**`BalanceService` как отдельный сервис** — логику пересчёта баланса (добавить кэш, сделать инкрементальный пересчёт, добавить транзакционность) можно изменить независимо от CRUD-операций. Ни `OperationService`, ни `BankAccountService` не знают о деталях пересчёта.
