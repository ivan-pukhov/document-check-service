Пример показывает использование Rabbit MQ policy.

В примере представлена система проверки документов. Документ проверяется по полям firstName, lastName и number
В проекте два модуля:
1) government-service-client - модель сервиса, которому нужно проверить документ и сохранить результат.
Сервис отправляет в exchange document-check-exchange сообщение, оно попадает в очередь document-check-queue.
Swagger: http://localhost:8083/swagger-ui/index.html
2) government-service-adapter - микросервис, который вычитывает сообщение из очереди document-check-queue.
После чего делает запрос в государственный сервис, получает ответ и в exchange document-check-result-exchange
отправляет сообщение со статусом проверки.
3) process-exception-service (ветка policy-refactoring-ttl) - микросервис для обработки ошибок при запросе в государственный сервис. 
   При применении  policy из файла dead-letter-exchange-policy-config.json сообщения попадают и в мёртвую очередь document-check-queue.dlq
   и в сервис process-exception-service

Класс org.example.government.service.adapter.service.GovernmentCallService моделирует вызов государственного сервиса.
Статусы документов описаны в enum org.example.government.service.adapter.model.DocumentStatus

Если поле number документа для проверки заканчивается на 4 или 5, статус документа будет INVALID.

Если поле number документа для проверки заканчивается на 12. То результат проверки будет CHECK_ERROR.
Сообщение с таким документом попадает в мёртвую очередь (Dead Letter Queue) c названием document-check-queue.dlq

Через какое-то время сообщение снова окажется в очереди document-check-queue и попытка проверки повторится.
Максимальное количество проверок находится в свойстве max-request-count. Если количество попыток превышено,
то статус документа окажется CHECK_COMPLETED_WITH_ERROR

Ветка master содержит пример без использования policy

Ветка http-config-dlq содержит пример переопределения свойств очереди (TTL).
Свойства будут переопределятся в соответствии с правилами, описанными в документации https://www.rabbitmq.com/docs/policies#operator-policy-conflicts

Ветка policy-refactoring-ttl содержит конфигурацию policy в definitions.json

Изменения в policy можно делать с помощью rest api. В папке rabbit-http-config скрипты put_message_ttl.sh и put_new_dead_letter_exchange.sh. 
При запуске через curl вызывается http запрос, который добавляет policy, описанное в файлах ttl-policy-config.json или dead-letter-exchange-policy-config.json.

dead-letter-exchange-policy-config.json - это конфигурация для x-dead-letter-exchange. Сообщения будут отправляться и в мёртвую очередь document-check-queue.dlq 
и в сервис process-exception-service 
ttl-policy-config.json - конфигурация для изменения x-message-ttl




