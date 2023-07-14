# Bank account API

## Как он работает?
### Запустите сервис в IDEA либо выполните команду:

```shell
mvn clean package compile
java -jar target/kz-s1lk-account-api-0.0.1.jar
```



### После переходим на http://localhost:8080/swagger-ui/index.html#/

## 1. Создание банковского аккаунта:

### http://localhost:8080/swagger-ui/index.html#/Account%20controller/createAccount

#### Обозначения:

##### Request:

`{
"ownerName": "Имя владельца аккаунта",
"ownerSecondName": "Фамилия владельца аккаунта",
"balance": Cтартовый баланс аккаунта
}`

##### Response:

`{
"ownerName": "Имя владельца аккаунта",
"ownerSecondName": "Фамилия владельца аккаунта",
"number": "Номер счета",
"balance": Баланс аккаунта
}`

## 2.Получение данных по номеру счета:

### http://localhost:8080/swagger-ui/index.html#/Account%20controller/getAccount

#### Обозначения:

##### Request:

`?number="Номер банковского счета"`

##### Response:

`{
"ownerName": "Имя владельца аккаунта",
"ownerSecondName": "Фамилия владельца аккаунта",
"number": "Номер счета",
"balance": Баланс аккаунта
}`

## 3. Перевод средств между счетами:

### http://localhost:8080/swagger-ui/index.html#/Account%20controller/transferMoney

#### Обозначения:

##### Request:

`{
"senderAccountNumber": "Номер счета отправителя",
"receiverAccountNumber": "Номер счета получателя",
"amount": Сумма отправки
}`

##### Response:

`{
"ownerName": "Имя владельца аккаунта",
"ownerSecondName": "Фамилия владельца аккаунта",
"number": "Номер счета",
"balance": Баланс аккаунта
}`



