<h1>CLEVERTEC (homework-09)</h1>

<p>CLEVERTEC homework-09 concurrency:</p>
<ol>
<li>Создать любой gradle проект</li>
<li>Придерживаться GitFlow: master -> develop -> feature/fix</li>
<li>Создать два класса:</li>
<ul>
<li>
Клиент - имеет список данных в виде List<Integer> от 1 до n.  Отдельными потоками,
по случайному индексу из списка выбирается значение (метод remove()) и в виде запроса
(класс с int -полем), содержащего это значение, отправляется на сервер в асинхронном 
режиме (например отправляются со случайной задержкой между запросами - диапазон - от 100 до 500 мс). 
Количество запросов равно размеру первоначального списка. Контроль: после отправки всех запросов 
размер списка данных = 0
</li>
<li>
Сервер - получает запросы от клиента. Метод обрабатывающий запрос имеет задержку в виде 
рандомного инта. Диапазон - от 100 до 1000 мс. Сервер обрабатывает запросы, используя 
общий для всех потоков ресурс: List<Integer>, в который складываются значения приходящие 
с запросом. В ответ от сервера передаем размер листа на момент формирования ответа (класс с int-полем). 
Итоговый контроль правильности данных на стороне сервера: список (общий ресурс) должен содержать 
значения от 1 до n без пробелов, повторений, размерность его должна составлять n
</li>
</ul>
<li>
Клиент получает от сервера ответ и в общий для всех потоков ресурс accumulator суммирует 
значение из ответа от сервера. Итоговый контроль: accumulator = (1+n) * (n/2). 
Т.е. для диапазона 1-100 ответ должен быть 5050
</li>
<li>Протестировать эти два класса с проверкой многопоточности</li>
<li>
Протестировать взаимодействие клиента - сервера отдельным тестом (интеграционный) - обязательно
</li>
<li>
В реализации использовать классы пакета java.util.concurrent (обязательно Lock, Callable, 
Executor, Future, остальное - по выбору) 
</li>
<li>
Методы класса Object (относящиеся к потокам и монитору) и ключевое слово synchronized НЕ использовать
</li>
<li>Заполнить и отправить форму</li>
</ol>

<h2>Что сделано:</h2>
<ol>
<li>Создан gradle проект.</li>
<li>При разработке использовалась концепция GitFlow: master -> develop -> feature/fix.</li>
<li>Создано два класса:</li>
<ul>
<li>
Клиент - имеет список данных в виде List<Integer> от 1 до n.  Отдельными потоками,
по случайному индексу из списка выбирается значение (метод remove()) и в виде запроса
(класс IntegerRequest с int -полем), содержащего это значение, отправляется на сервер в асинхронном 
режиме (отправляются со случайной задержкой между запросами - диапазон - от 100 до 500 мс). 
Количество запросов равно размеру первоначального списка. Контроль: после отправки всех запросов 
размер списка данных = 0;
</li>
<li>
Сервер - получает запросы от клиента. Метод обрабатывающий запрос имеет задержку в виде 
рандомного инта. Диапазон - от 100 до 1000 мс. Сервер обрабатывает запросы, используя 
общий для всех потоков ресурс: List<Integer>, в который складываются значения приходящие 
с запросом. В ответ от сервера передаем размер листа на момент формирования ответа 
(класс IntegerResponse с int-полем). 
Итоговый контроль правильности данных на стороне сервера: список (общий ресурс) 
содержит значения от 1 до n без пробелов, повторений, размерность его составляет n.
</li>
</ul>
<li>
Клиент получает от сервера ответ и в общий для всех потоков ресурс accumulator суммирует 
значение из ответа от сервера. Итоговый контроль: accumulator = (1+n) * (n/2). 
Сделано для диапазона 1-100 и ответ - 5050.
</li>
<li>Протестированы эти два класса (Server, Client) с проверкой многопоточности.</li>
<li>
Протестировано взаимодействие клиента - сервера отдельным тестом (интеграционный).
</li>
<li>
В реализации использованы реализации интерфейсов и классы пакета java.util.concurrent (Lock, Callable, 
Executor, Future). 
</li>
<li>
Методы класса Object (относящиеся к потокам и монитору) и ключевое слово synchronized НЕ использовались.
</li>
<li>Заполнена и отправлена форма.</li>
</ol>

<h3>Как запускать:</h3>
<ol>
<li>Билдим проект: .\gradlew clean build</li>
</ol>
<p>Запускаем main метод из Main класса, проверяем работу приложения.</p>
