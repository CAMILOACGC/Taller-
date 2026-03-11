# Puzzle Deslizante - Documentación del Proyecto

Este proyecto es una aplicación Android moderna desarrollada con **Jetpack Compose**. Consiste en un juego de puzzle numérico de 3x3 donde el objetivo es ordenar los números del 1 al 9 realizando intercambios entre piezas adyacentes.

---

## 📂 Estructura de Archivos y Directorios

### 1. Configuración del Proyecto (Gradle)
*   **`build.gradle.kts` (Raíz):** Define los plugins globales (Android, Kotlin, Compose).
*   **`app/build.gradle.kts`:** Configura el módulo de la aplicación, incluyendo el `compileSdk`, `minSdk`, y declara las dependencias necesarias.
*   **`gradle/libs.versions.toml`:** El Catálogo de Versiones. Aquí se centralizan todas las versiones y definiciones de librerías para que el mantenimiento sea sencillo.

### 2. Manifiesto y Recursos
*   **`AndroidManifest.xml`:** El archivo de identidad de la app. Define que la actividad principal es `MainActivity` y que es la que se debe abrir al iniciar la aplicación.

### 3. Código Fuente (Kotlin)
*   **`MainActivity.kt`:** Contiene todo el código funcional del juego, dividido en tres partes fundamentales siguiendo el patrón MVVM.

---

## 🛠️ Dependencias Utilizadas

| Librería | Propósito |
| :--- | :--- |
| **Jetpack Compose (UI, Graphics)** | Framework para construir la interfaz de usuario declarativa. |
| **Material 3** | Sistema de diseño de Google para botones, tarjetas y tipografía moderna. |
| **Lifecycle ViewModel** | Gestiona los datos para que no se pierdan al girar la pantalla. |
| **Activity Compose** | Integración entre las Actividades clásicas de Android y Compose. |

---

## 🧠 Lógica y Funciones (ViewModel)

La clase `PuzzleViewModel` actúa como el "cerebro" del juego:

*   **`board`**: Un estado que contiene la lista de números. Al usar `mutableStateOf`, cualquier cambio aquí redibuja la pantalla automáticamente.
*   **`selectCell(index)`**: Gestiona la interacción. Guarda la primera celda tocada y, al tocar la segunda, intenta realizar el movimiento.
*   **`move(i1, i2)`**: Verifica si las celdas son adyacentes. Si lo son, intercambia sus posiciones en la lista, aumenta el contador de movimientos y recalcula la meta.
*   **`isAdjacent(i1, i2)`**: Función matemática que determina si dos índices están juntos (arriba, abajo, izquierda o derecha) en una cuadrícula de 3x3.
*   **`calculateGoal(board)`**: Calcula cuántas piezas están en la posición incorrecta para dar una pista al usuario.
*   **`isSolved()`**: Comprueba si el tablero actual es igual a la lista ordenada `[1, 2, 3, 4, 5, 6, 7, 8, 9]`.
*   **`resetGame()`**: Mezcla los números y reinicia los contadores.

---

## 🎨 Interfaz de Usuario (UI)

La interfaz se construye con funciones `@Composable`:

*   **`PuzzleScreen`**: La pantalla principal que organiza el título, la cuadrícula de juego, las estadísticas y el botón de reinicio.
*   **`LazyVerticalGrid`**: Crea una rejilla eficiente de 3 columnas para los números.
*   **`Card` y `Box`**: Se usan para dar forma y estilo a cada ficha del puzzle.
*   **`StatItem`**: Un componente reutilizable para mostrar los movimientos y la meta de forma elegante.

---

## 🚀 ¿Cómo se hizo? (Proceso de Desarrollo)

1.  **Arquitectura MVVM**: Se separó la lógica (ViewModel) de la vista (Compose) para que el código sea limpio y escalable.
2.  **Estados Reactivos**: Se utilizó el sistema de estados de Compose para que el desarrollador no tenga que "avisar" a la pantalla que debe actualizarse; ella lo hace sola cuando los datos cambian.
3.  **Algoritmos de Posición**: Se implementó una lógica basada en filas (`index / 3`) y columnas (`index % 3`) para manejar la rejilla bidimensional usando una lista unidimensional simple.
4.  **Estilo Material 3**: Se aplicaron contenedores `Surface` y `Card` con elevaciones y colores dinámicos para una experiencia de usuario nativa y fluida.
