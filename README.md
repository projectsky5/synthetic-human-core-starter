Д3 №3 Т1 Иннотех

---

Задание: Реализовать собственный spring-boot-starter

Демонстрационный проект: https://github.com/projectsky5/bishop-prototype

---

Реализовано:
1. Модуль выполнения команд (пакет command), очередь реализована с помощью ThreadPoolExecutor + LinkedBlockingQueue
2. Модуль аудита (пакет audit), Реализация аннотации через AOP (Before + AfterReturning для console, Around для Kafka)
3. Модуль метрик (пакет metrics), Реализация двух метрик - Counter, Gauge с помощью MeterRegistry + Counter
4. Модуль исключений (пакет exception), Реализация собственного исключения, читаемой обработки исключений через собственную модель + Enum для сообщения кастомных исключений, обработка исключений через @ExceptionHandler
5. Создание resources/META-INFORMATION/spring/*.imports файла для указания пути к файлу автогенерации
6. SyntheticProperties в качестве POJO для хранения настроек конфигурации
7. *AutoConfiguration, в котором происходит инициализация всех необходимых бинов + создание бинов с условием из настроек конфигурации
8. Убран spring-boot-maven-plugin

---

**Инструкция для тестирования**

1. В директории проекта в консоли выполнить команду
```
mvn clean install
```
2. Продолжить работу с демонстрационным проектом
