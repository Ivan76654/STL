# Stolnoteniska Liga (STL)

## Baza podataka (PostgreSQL)
U terminalu se treba pozicionirati unutar 'database' mape te pokrenuti sljedeću naredbu (potrebna je instalacija dockera):
```
> docker compose up
```
## Poslužitelj (Server)
U terminalu se treba pozicionirati unutar 'backend/stl' mape te pokrenuti sljedeće naredbe:
### Windows:
```
> gradlew build
> gradlew bootRun
```

### Linux:
```
> ./gradlew build
> ./gradlew bootRun
```

## Aplikacija (React)
U terminalu se treba pozicionirati unutar 'frontend/STL' mape te pokrenuti sljedeće naredbe:
```
> npm install
> npm run dev
```
