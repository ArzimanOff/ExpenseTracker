# ExpenseTracker

**ExpenseTracker** — это RESTful API для управления личными расходами. Проект предоставляет удобный способ добавлять, просматривать, обновлять и удалять траты, а также анализировать их с помощью фильтров, сортировки и подсчёта общей суммы за определённый период.

---

## Технологии

- **Spring Boot** — фреймворк для создания веб-приложений на Java.
- **Spring Data JPA** — для взаимодействия с базой данных через ORM.
- **PostgreSQL** — реляционная база данных для хранения информации о расходах.
- **Spring Security** — для реализации аутентификации и авторизации пользователей.
- **Maven** — Для сборки проекта и настройки зависимостей.

---

## Функционал

- **Аутентификация пользователей** — регистрация и вход.
- **Управление расходами**:
  - Добавление новой траты.
  - Просмотр списка всех трат.
  - Обновление существующей траты.
  - Удаление траты.
- **Фильтрация трат** — по категории и диапазону дат.
- **Сортировка трат** — по стоимости и дате.
- **Анализ расходов** — подсчёт общей суммы трат за указанный период.

---

## API Эндпоинты

Ниже приведены все доступные эндпоинты с описанием их назначения и примерами использования. Все запросы требуют заголовок `Authorization` для аутентификации.

### 1. Добавить новую трату
- **Метод**: `POST`
- **Путь**: `/api/expenses`
- **Описание**: Создаёт новую запись о расходе.
- **Тело запроса**:
  ```json
  {
    "amount": 100.50,
    "date": "2023-10-01",
    "description": "Lunch",
    "category": {"id": 1}
  }

### 2. Получить список трат
- Метод: `GET`
- Путь: `/api/expenses`
- Описание: Возвращает список трат с возможностью фильтрации и сортировки.
- Параметры запроса:
  - categoryId — ID категории (опционально).
  - startDate — начальная дата (опционально, формат: YYYY-MM-DD).
  - endDate — конечная дата (опционально, формат: YYYY-MM-DD).
  - sortByCost — сортировка по стоимости (asc или desc, опционально).
  - sortByDate — сортировка по дате (asc или desc, опционально).
- Примеры запросов:
  - Найти весь список расходов без фильтрации: `/api/expenses`

    <img src="https://github.com/user-attachments/assets/e6e66b7b-2b55-4ba4-b6ca-8a75bbf98352" width="460" />
    <hr>
  - Найти список расходов, которые были сделаны в период между `2025-03-01` и `2025-03-22`: \
     `/api/expenses?startDate=2025-03-01&endDate=2025-03-22`

    <img src="https://github.com/user-attachments/assets/79802538-2894-45db-a090-e354317b43ab" width="460" />
    <hr>
  - Найти список расходов, которые относятся только к категории `7`: \
    `/api/expenses?categoryId=7`

    <img src="https://github.com/user-attachments/assets/1d71a311-d1ad-445f-a25d-7ffbbf84ae7f" width="460" />
    <hr>
  - Найти список расходов, которые относятся к категории `7`, отсортировать сперва по дате (убывание), затем по цене(возрастание): \
     `/api/expenses?sortByDate=desc&sortByCost=asc&categoryId=7`

    <img src="https://github.com/user-attachments/assets/390c0f89-a267-4f52-a2b8-fda92b561fba" width="460" />

### 3. Обновить трату
- Метод: `PUT`
- Путь: `/api/expenses/{id}`
- Описание: Обновляет данные существующей траты по её ID.
- Тело запроса:
```json
  {
    "amount": 150.00,
    "date": "2023-10-02",
    "description": "Dinner",
    "category": {"id": 1}
  }
```

### 4. Удалить трату
- Метод: `DELETE`
- Путь: `/api/expenses/{id}`
- Описание: Удаляет трату по её ID.
- Пример:
`/api/expenses/7`

### 5. Получить общую сумму трат
- Метод: `GET`
- Путь: `/api/expenses/total`
-Описание: Возвращает общую сумму трат за указанный период.
- Параметры запроса:
  - startDate — начальная дата (обязательно, формат: YYYY-MM-DD).
  - endDate — конечная дата (обязательно, формат: YYYY-MM-DD).
- Пример:
  - Найти общую потраченную сумму: `/api/expenses/total`
  
      <img src="https://github.com/user-attachments/assets/0c3749bd-b64a-402d-832f-c5a56372fd6f" width="460" />
      <hr>

  - Найти потраченную сумму в период с `2025-03-01` до `2025-03-21`  : \
     `/api/expenses/total?startDate=2025-03-01&endDate=2025-03-21`
    
      <img src="https://github.com/user-attachments/assets/e895e488-4953-4f55-8772-adc18179f52a" width="460" />
      <hr>


## Установка
### Клонируйте репозиторий:
```bash
git clone https://github.com/yourusername/ExpenseTracker.git
```
### Настройте базу данных в файле application.properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expensetracker
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Запустите приложение:
```bash
mvn spring-boot:run
```
