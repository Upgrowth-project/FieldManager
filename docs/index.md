# Upgrowth project documentation

## Менеджер поля

Менеджер поля представляет интерфейс для взаимодействия с сущностью "поле" в программе. FieldManager требует для инициализации параметры,
передаваемые в объекте FieldManagerInitializer. Класс имплементирует public поля и методы, описанные в интерфейсе CoreFieldInterface и позволяющие взаимодействовать с полем:

Описание public полей:

| Поле | Содержимое |
|------|------------|
| val animals: Array<Animal> | массив со всеми животными на игровом поле |
| val players: Array<Player> | массив со всеми игроками |
| val locations: Array<Location> | массив со всеми локациями |
| val deckSize: Int | размер колоды |

Описание public методов:

| Метод | Описание |
|------|------------|
| <T> refresh(entity: T): T | чево |
| giveCards(player: Player, number: Int) | дать игроку определенное количество карт |
| amountOfCards(player: Player, location: Location): Int | количество карт, которое должен получить игрок за данную локацию |
| addAnimal(location: Location, player: Player): Animal | создать в локации новое животное игрока и вернуть его |
| deleteAnimal(animal: Animal) | удалить животное с поля |
| moveAnimal(animal: Animal, destination: Location) | переместить животное в новую локацию |
| getLocation(animal: Animal): Location | найти локацию, в которой находится животное |
| addProperty(property: Property, animal: Animal) | добавить животному свойство |
| deleteProperty(property: Property, animal: Animal) | удалить свойство у животного |
| generateFood(location: Location) | сгенерировать еду в этой локации |
| food(location: Location): Int | количество еды в локации |
| setFood(location: Location, number: Int) | установить количество еды в локации |
| setFood(animal: Animal, number: Int) | установить количество пищи, съеденное животным (если животное не может столько съесть, установится максимальное число еды для него) |
| canMove(from: Location, to: Location): Boolean | возвращает true, если можно осуществить непосредственный переход между локациями |





### Игровое поле
Поле -- объект, используемый для представления игрового поля: колоды карт, локаций и животных в них. Локации также хранят функции благ.

Структура поля приведена ниже (элементы сомнительной целесообразности обозначены маркером недоумения "??" и сомнительным оранжевым цветом).
![Структура поля](./images/struct.png)