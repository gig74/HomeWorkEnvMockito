# HomeWorkEnvMockito
 Домашнее задание по теме "Среда тестирования Mockito" (ProductStar)

# Постановка задачи
1.  Скачать код проекта из воркшопа, открыть в любой IDE.
2.  Необходимо реализовать «тело»‎ unit тестов для сервисов, используя Mockito:
    - CustomerServiceTest
    - OrderServiceTest
    - WarehouseServiceTest
3.  Требования к тестам описаны в комментариях соответствующих классов.
4. Прислать на проверку домашнее задание.

## Подключенные зависимости и плагины
- junit-jupiter-engine - это основной движок тестирования для JUnit 5, отвечающий за запуск тестов
- mockito-core - это стандартная версия фреймворка Mockito для тестирования приложений на языке Java. Он позволяет создавать фиктивные объекты (моки), которые имитируют поведение реальных объектов, и определять их поведение в тестах.
- mockito-junit-jupiter расширение для фреймворка JUnit 5 («Jupiter»), которое позволяет интегрировать фреймворк Mockito для создания фиктивных объектов (моков) в тесты

## Описание основных файлов
- java/ru/productstar/mockito/model/Customer.java - класс объекта одного покупателя
- java/ru/productstar/mockito/model/Delivery.java - класс объекта одной доставки
- java/ru/productstar/mockito/model/Order.java - класс объекта одного заказа
- java/ru/productstar/mockito/model/Product.java - класс объекта одного товара
- java/ru/productstar/mockito/model/Stock.java - класс объекта одного запаса продукта на складе
- java/ru/productstar/mockito/model/Warehouse.java - класс объекта одного склада

- java/ru/productstar/mockito/repository/CustomerRepository.java - класс для объекта репозитария для покупателей
- java/ru/productstar/mockito/repository/OrderRepository.java - класс для объекта репозитария для заказов
- java/ru/productstar/mockito/repository/ProductRepository.java - класс для объекта репозитария для перечня товаров
- java/ru/productstar/mockito/repository/WarehouseRepository.java - класс для объекта репозитария для складов (включая запасы товаров со стоимостью)
- java/ru/productstar/mockito/repository/InitRepository.java - объект инициалиализации для всех объектов репозитария (singleton)

- java/ru/productstar/mockito/service/CustomerService.java - класс для объекта сервиса по работе с покупателями
- java/ru/productstar/mockito/service/OrderService.java - класс для объекта сервиса по работе с заказами
- java/ru/productstar/mockito/service/WarehouseService.java - класс для объекта сервиса по работе со складами

- java/ru/productstar/mockito/ProductNotFoundException.java - объектисключения "товар не найден". Используется в сервисе по работе с заказами.
#### Тесты 
- ru/productstar/mockito/repository/CustomerRepositoryTest.java - тест класса для объекта репозитария для покупателей
- ru/productstar/mockito/repository/ProductRepositoryTest.java - тест класса для объекта репозитария для товаров
- ru/productstar/mockito/repository/WarehouseRepositoryTest.java - тест класса для объекта репозитария для складов
- ru/productstar/mockito/repository/InitRepositoryTest.java - тест класса для объекта иницализации репозитария

- ru/productstar/mockito/service/CustomerServiceTest.java - тест класса для объекта сервиса по работе с покупателями 
- ru/productstar/mockito/service/OrderServiceTest.java - тест класса для объекта сервиса по работе с заказами
- ru/productstar/mockito/service/WarehouseServiceTest.java - тест класса для объекта сервиса по работе со складами
- ru/productstar/mockito/service/WarehouseServiceTestNoRealObject.java - Дополнительно тест полностью без реальных объектов мока класса для объекта сервиса по работе со складами
## Примечания
Проект "HomeWorkEnvMockito" был перекопирован изначально из предложенного спикером проекта "mockito_dz".
В соответствии с заданием были дописаны классы тестирования:
- CustomerServiceTest
- OrderServiceTest
- WarehouseServiceTest
Дополнительно сделан класс тестирования
- WarehouseServiceTestNoRealObject