# PROMPTS.md — Registro de uso de inteligencia artificial

## 1. Información del proyecto

**Proyecto:** Pokédex Pokémon  
**Laboratorio:** Integrador 11-12  
**Integrante:** Carlos Andia  
**Repositorio:** https://github.com/carlosandia-cpu/lab-integrador-fire-andia  
**Rama de mejoras:** `mejora-ia`

## 2. Objetivo

Documentar el uso de inteligencia artificial como herramienta de apoyo durante el desarrollo, revisión, implementación y mejora de la aplicación Android Pokédex.

La IA se utilizó para proponer cambios, analizar problemas y generar alternativas de código. Las soluciones se revisaron y probaron manualmente en Android Studio.

## 3. Herramientas utilizadas

- ChatGPT: análisis de código, revisión de errores, propuestas de interfaz y asistencia en implementación.
- Asistente de IA integrado en Android Studio: propuestas de mejoras y apoyo en modificaciones Kotlin.

Los prompts siguientes resumen las instrucciones utilizadas durante el trabajo.

---

## 4. Mejora 1 — Pull-to-Refresh

### Problema identificado

La aplicación permitía consultar y sincronizar Pokémon, pero el listado no contaba con un gesto directo para actualizar la información.

### Prompt representativo

> Analiza mi proyecto Android desarrollado con Kotlin, Jetpack Compose y arquitectura MVVM. Implementa Pull-to-Refresh en ListaScreen utilizando Material 3. Mantén la lógica existente de Room, Retrofit y Firebase. No elimines la búsqueda, los filtros ni los favoritos. Verifica la compilación y las pruebas unitarias.

### Implementación

Se incorporó Pull-to-Refresh en la pantalla del listado.

Se utilizó un estado reactivo para identificar cuándo se está actualizando la información.

El gesto de actualización se conecta con el método `refrescar()` de `ListaViewModel`.

### Archivos modificados

- `ListaScreen.kt`
- `ListaViewModel.kt`

### Resultado observado

Se comprobó en el emulador que el usuario puede deslizar hacia abajo para solicitar una actualización de la Pokédex.

La mejora conserva el uso de datos almacenados cuando no existe conexión.

---

## 5. Mejora 2 — Estado vacío y limpieza de filtros

### Problema identificado

Cuando una búsqueda o combinación de filtros no devolvía resultados, la pantalla solamente mostraba un mensaje sencillo.

El usuario debía restablecer manualmente los filtros.

### Prompt representativo

> Mejora la pantalla de resultados vacíos de mi Pokédex. Cuando no existan coincidencias, muestra un diseño temático inspirado en Pokémon, con una Poké Ball, un mensaje descriptivo y un botón Limpiar filtros. El botón debe restablecer búsqueda, región y tipo. Conserva Pull-to-Refresh y la arquitectura MVVM.

### Implementación

Se creó una vista de resultados vacíos con una Poké Ball ilustrativa y un botón para restablecer los filtros.

Se añadió el método `limpiarFiltros()` para reiniciar los estados de búsqueda, tipo y región.

### Archivos modificados

- `ListaScreen.kt`
- `ListaViewModel.kt`

### Resultado observado

Se probó una búsqueda sin coincidencias y apareció la pantalla personalizada.

Al seleccionar Limpiar filtros, los filtros se restablecieron y volvió a mostrarse el listado.

---

## 6. Mejora 3 — Validación de inicio de sesión

### Problema identificado

El formulario de autenticación necesitaba proporcionar mensajes más claros y validar información antes de realizar solicitudes a Firebase.

### Prompt representativo

> Mejora LoginScreen agregando validaciones locales de correo electrónico y contraseña. Muestra mensajes claros cuando los campos estén vacíos o el correo tenga un formato incorrecto. Añade un botón para mostrar u ocultar la contraseña, configura el teclado y evita múltiples intentos mientras se procesa el inicio de sesión. Mantén Firebase Authentication y la navegación existente.

### Implementación

Se incorporaron:

- Validación de campos vacíos.
- Verificación del formato del correo.
- Mensajes de error.
- Control para mostrar y ocultar la contraseña.
- Indicador de carga.
- Control de disponibilidad del botón durante la autenticación.
- Configuración del teclado para facilitar la entrada de datos.

### Archivos modificados

- `LoginScreen.kt`

### Resultado observado

Se comprobaron errores de campos vacíos y credenciales incorrectas.

También se verificó que la aplicación permite iniciar sesión con las credenciales válidas.

---

## 7. Mejoras adicionales — Interfaz visual Pokémon

### Objetivo

Crear una identidad visual coherente entre las diferentes pantallas de la aplicación.

### Prompt representativo

> Rediseña la interfaz de mi aplicación Pokédex para que tenga una estética inspirada en los videojuegos Pokémon. Utiliza tonos azul oscuro, amarillo dorado, fondos ilustrados, tarjetas semitransparentes y componentes personalizados. Conserva las funcionalidades existentes, la arquitectura MVVM y la navegación.

### Cambios realizados

Se trabajó en:

- Fondos temáticos Pokémon.
- Cabeceras azules con títulos amarillos.
- Tarjetas personalizadas.
- Barra de navegación inferior.
- Colores específicos para cada tipo Pokémon.
- Perfil con tarjeta de entrenador.
- Panel de administrador.
- Pantalla de detalles.
- Diseño de formularios de login y registro.

### Archivos relacionados

- `ItemFila.kt`
- `ListaScreen.kt`
- `FavoritosScreen.kt`
- `AdminScreen.kt`
- `PerfilScreen.kt`
- `DetalleScreen.kt`
- `AppNav.kt`
- `FondoPokemon.kt`
- `LoginScreen.kt`
- `RegistroScreen.kt`

### Resultado observado

Se comprobó visualmente en el emulador que las diferentes pantallas comparten una identidad gráfica relacionada con Pokémon.

---

## 8. Mejoras adicionales — Filtros desplegables

### Problema identificado

Los filtros horizontales ocupaban demasiado espacio, especialmente en dispositivos con pantallas pequeñas.

### Prompt representativo

> Reemplaza los filtros horizontales de la Pokédex por dos menús desplegables: región y tipo. Limita la altura del menú para mostrar aproximadamente seis opciones antes de desplazarse. Mantén los filtros seleccionados, la búsqueda por nombre y el funcionamiento en orientación vertical y horizontal.

### Implementación

Se modificó la interfaz de filtros utilizando componentes desplegables de Material 3.

Los filtros continúan conectados al estado de `ListaViewModel`.

### Archivo modificado

- `ListaScreen.kt`

### Resultado observado

Se comprobó que los filtros funcionan mediante menús desplegables y que los resultados cambian según la selección.

---

## 9. Correcciones durante la implementación

Durante el desarrollo asistido por IA se identificaron errores que requirieron revisión manual:

### Problema de navegación

Los botones de retroceso y favoritos aparecían demasiado cerca de la barra de estado del teléfono.

Se corrigió la distribución visual de `DetalleScreen.kt` para respetar el espacio de la barra de estado.

### Problemas de compilación

Se encontraron referencias no resueltas relacionadas con componentes de Jetpack Compose, como `NavigationBarItem` y el modificador `background`.

Se revisaron imports y se utilizó una implementación personalizada de navegación inferior.

### Problemas estéticos

Se realizaron ajustes adicionales de colores, transparencias, fondos y tarjetas para mejorar la legibilidad y mantener un estilo uniforme.

Estas correcciones demuestran que el código generado o sugerido por inteligencia artificial requiere revisión, adaptación y validación antes de incorporarse al proyecto.

---

## 10. Validación

Las mejoras se revisaron mediante:

- Compilación en Android Studio.
- Ejecución en un emulador Android.
- Pruebas unitarias de `ListaViewModel`.
- Pruebas manuales de navegación.
- Pruebas de autenticación.
- Pruebas de búsqueda y filtros.
- Pruebas de favoritos.
- Pruebas de actualización de datos.
- Pruebas sin conexión.
- Revisión visual de las pantallas.

Durante el desarrollo se obtuvo un resultado de tres pruebas unitarias aprobadas.

El resultado final debe verificarse nuevamente después de integrar todos los cambios.

---

## 11. Control de versiones

Las mejoras asistidas por IA se desarrollaron en la rama:

`mejora-ia`

Repositorio del proyecto:

https://github.com/carlosandia-cpu/lab-integrador-fire-andia

Los cambios se organizaron mediante Git para facilitar su seguimiento y posterior integración con `main`.

Los identificadores definitivos de commits y el Pull Request deben consultarse en el historial del repositorio.

---

## 12. Reflexión sobre el uso de IA

La inteligencia artificial permitió identificar alternativas de implementación, mejorar la presentación visual y reducir el tiempo dedicado a tareas repetitivas.

Sin embargo, algunas propuestas iniciales provocaron errores de compilación o problemas visuales, por lo que fue necesario analizar sus resultados y realizar correcciones.

El aprendizaje principal fue que la inteligencia artificial es una herramienta de apoyo y no sustituye las pruebas, la comprensión del código ni la responsabilidad del desarrollador.

**Responsable del proyecto: Carlos Andia.**