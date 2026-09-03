# Guide Trade - Android App Build Instructions

## Build

```bash
./gradlew assembleDebug
```

## Project Structure

```
app/
  src/main/
    java/com/guidetrade/app/
      GuideTradeApp.kt          # Application class (Firebase init)
      MainActivity.kt           # Entry point (auth flow → main navigation)
      data/repository/          # Firebase repository implementations
      domain/model/             # Domain models (User, ChatMessage, ResearchNote, etc.)
      domain/repository/        # Repository interfaces
      domain/usecase/           # Business logic use cases
      ui/navigation/            # Navigation (NavRoutes, NavGraph)
      ui/screens/               # Composable screens
      ui/theme/                 # Material 3 design system
```
