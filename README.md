# SokobanFX

SokobanFX is a simple Sokoban implementation written with JavaFX. It targets JDK 17 and is intended to be opened and run from IntelliJ IDEA. Levels are stored as plain text files with a custom `.sok` extension (the format is a simple text map — see "Level file format" below).

---

## Authors
- Martinho Caeiro - 23917
- Paulo Abade - 23919

---

## Features

- Classic Sokoban gameplay (push boxes onto goal squares)
- Levels loaded from plain-text `.sok` files
- Built with JavaFX (UI) and JDK 17
- Easy to extend with new levels (text format)

---

## Requirements

- JDK 17
- JavaFX SDK 17+ (matching your platform)
- IntelliJ IDEA (or another IDE that supports Java 17 + JavaFX)
- Git (optional, for cloning the repo)

---

## Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/MartinhoCaeiro/SokobanFX.git
   cd SokobanFX
   ```

2. Open the project in IntelliJ IDEA:
   - File → Open... → select the project folder.
   - Set the Project SDK to JDK 17 (File → Project Structure → Project).
   - If the project uses a build tool (Gradle/Maven), import it; otherwise configure the SDK manually.

3. Add JavaFX libraries to the project:
   - Download JavaFX SDK 17+ from https://openjfx.io/.
   - In IntelliJ: File → Project Structure → Libraries → + → select the `lib` folder inside the JavaFX SDK.
   - Alternatively, if you use Gradle/Maven, add the appropriate JavaFX dependencies to your build file.

4. Configure the run configuration:
   - Create an application run configuration that launches the class that extends `javafx.application.Application` (the project's main application class).
   - Add the JavaFX VM options (adjust the path to your JavaFX SDK `lib` folder):
     ```
     --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml
     ```
   - Optionally add a program argument pointing to a level file:
     ```
     path/to/levels/level1.sok
     ```

5. Run the application from IntelliJ using the configured run configuration.

---

## Running from the command line

If you build the project to produce compiled classes or a jar, run with the JavaFX module-path flags. Example (adjust paths and main class name):

```
java --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml -cp out/production/SokobanFX com.yourpackage.Main path/to/level.sok
```

If you create a modular jar, use `--module` and include the JavaFX modules as described in OpenJFX docs.

---

## Level file format (.sok)

Levels are plain text files where each line is a row of the map. Use a fixed-width, monospaced layout. A typical set of characters (common Sokoban convention) that this project supports or can easily be adapted to:

- `#` — Wall
- ` ` (space) — Floor / empty
- `.` — Goal (target)
- `$` — Box
- `@` — Player (worker)
- `*` — Box on goal
- `+` — Player on goal

Example level (example.sok):
```
#######
#     #
#.$ @#
#  * #
#  . #
#######
```

Notes:
- The exact accepted characters and parsing details can be adapted in the game's level-loader. Check the source if you want to change or extend the format.
- Save new levels with the `.sok` extension (or `.txt` if you prefer) and load them from the game's level loader or pass their path as an argument if supported.

---

## Controls

- Arrow keys (or WASD) — Move the player
- Undo — revert last move
- Restart level — restart current level
- Open level — open a `.sok` file from disk

(Exact controls are implemented in the UI — check the in-game help or source code for precise mappings.)

---

## Troubleshooting

- "JavaFX classes not found" — Ensure the JavaFX SDK is downloaded and the `--module-path` VM option is set to the SDK `lib` folder, and the `--add-modules` option lists the modules you need (at least `javafx.controls` and `javafx.fxml` if you use FXML).
- Make sure your IntelliJ run configuration is using Project SDK 17 and that the VM options are set for JavaFX.
- If the game won't load a level, check that the level file is valid plain text and uses the supported characters described above.

---

## Contributing

Contributions are welcome. If you want to add features, levels, or fixes:

1. Fork the repository.
2. Create a feature branch.
3. Make your changes, add tests if applicable.
4. Open a pull request describing your changes.
