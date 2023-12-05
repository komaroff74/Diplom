## Дипломный проект по профессии «Тестировщик ПО»
### Документы
1. [Задание для работы](https://github.com/netology-code/qa-diploma)
2. [План автоматизации тестирования](https://github.com/komaroff74/Diplom/blob/main/Plan.md)
3. [Отчёт по итогам тестирования]()
4. [Отчёт по автоматизации тестирования]()
### Предустановленное окружение
- IntelliJ IDEA
- Docker Desktop
### Запуск проекта по тестированию сервиса для покупки туров
1. Запустить Docker Desktop.
2. Склонированный проект окрыть в IDEA.
3. Открыть терминал в IDEA.
4. Запустить контейнеры командой `docker-compose up`.
5. Запустить приложение командой: 
    -Для работы с MySQL `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar aqa-shop.jar`.
    -Для работы с Postgres `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar aqa-shop.jar`.
6. Проверить доступность приложения в браузере по ссылке http://localhost:8080/.
7. Запустить тесты: `Ctrl+Shift+F10`
8. Запустить Allure для создания отчёта командой `./gradlew allureServe`.
9. Остановить Allure комбинацией клавиш **Ctrl+C**, а после ввести **Y** для подтверждения.
10. Остановить работу контейнеров командой `docker-compose down`.