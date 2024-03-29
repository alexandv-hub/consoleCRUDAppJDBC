Практика.

Описание задачи:

Необходимо реализовать консольное CRUD приложение, которое взаимодействует с БД и позволяет выполнять все CRUD операции над сущностями:
- Writer (id, firstName, lastName, List<Post> posts)
- Post (id, content, created, updated, List<Label> labels)
- Label (id, name)
- PostStatus (enum ACTIVE, UNDER_REVIEW, DELETED)

Требования:
- Все CRUD операции для каждой из сущностей
- Придерживаться подхода MVC
- Для сборки проекта использовать Maven
- Для взаимодействия с БД - Hibernate
- Для конфигурирования Hibernate - аннотации
- Инициализация БД должна быть реализована с помощью flyway
- Сервисный слой приложения должен быть покрыт юнит тестами (junit + mockito)
- Результатом выполнения задания должен быть репозиторий на github.

Технологии: 
- Java, PostgreSQL, Hibernate, Flyway, Maven,

## Консольное CRUD Приложение

### Описание
Это консольное приложение для управления данными (CRUD - Создание, Чтение, Обновление, Удаление) в различных категориях, таких как Writers (Авторы), Posts (Посты) и Labels (Метки).

### Функциональность
Приложение предоставляет следующие возможности:

#### Главное меню
Пользователь может выбрать одну из следующих опций:

1. **Переход в меню Авторов (Writers)**: Управление данными авторов.
2. **Переход в меню Постов (Posts)**: Управление данными постов.
3. **Переход в меню Меток (Labels)**: Управление данными меток.
4. **Выход из приложения**

#### Меню Entity (Авторы, Посты, Метки)
В каждом из меню (Авторы, Посты, Метки) пользователь может выполнять следующие действия:

1. **Создание новой сущности**: Добавление нового автора, поста или метки.
2. **Поиск сущности по ID**: Поиск автора, поста или метки по уникальному идентификатору.
3. **Показ всех сущностей**: Отображение списка всех авторов, постов или меток.
4. **Обновление существующей сущности по ID**: Изменение данных существующего автора, поста или метки.
5. **Удаление сущности по ID**: Удаление автора, поста или метки.
6. **Возврат в Главное меню**

### Инструкции по запуску
Для запуска приложения следуйте этим шагам:

1. Клонируйте репозиторий на ваш локальный компьютер.
2. Установите и настройте PostgreSQL Server.
3. Откройте терминал и перейдите в директорию проекта.
4. Выполните команду `mvn clean install` или `mvn package`.
5. Запустите приложение, используя команду `java -jar target/consoleCRUDApp-1.0-SNAPSHOT.jar`. 

### Технические требования
- Java версии 8 или выше.
- Наличие JRE (Java Runtime Environment) на компьютере.

### Разработчик
(VA)

---
