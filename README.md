# Rick And Morty Sample App

Based on second homework of FIT CTU BI-AND course:

[Second homework specification](https://courses.fit.cvut.cz/BI-AND/homeworks/index.html#_second-homework-30b)

Main purpose of this app is to load and store Rick And Morty Characters using [Rick And Morty API](https://rickandmortyapi.com/).

Characters are loaded using Retrofit and stored in local Room database.

Koin is used as a dependency injection framework.

App has four screens:
- Character list - list of Rick And Morty characters (LazyColumn) loaded from local database with updates from [API endpoint](https://rickandmortyapi.com/api/character) (only the first page)
- Favorites list - list of favorite characters (user defined, favorite information is stored only locally and is not override with changes from API)
- Detail screen - provides detail information about character with possibility to add to / remove from favorites
- Search screen - search among all the characters from [special search API endpoint](https://rickandmortyapi.com/documentation/#filter-characters), not overriding favorites information stored locally