# Pokédex Pokémon — Laboratorio Integrador

## 1. Información general

**Proyecto:** Pokédex Pokémon  
**Curso:** Desarrollo de Aplicaciones Móviles — Laboratorio Integrador 11-12  
**Integrante:** Carlos Andia  
**Repositorio:** https://github.com/carlosandia-cpu/lab-integrador-fire-andia  
**Plataforma:** Android  
**Lenguaje:** Kotlin  
**Interfaz:** Jetpack Compose y Material Design 3

## 2. Descripción del proyecto

Pokédex es una aplicación móvil Android que permite explorar información de Pokémon mediante el consumo de la API pública PokeAPI.

La aplicación implementa autenticación de usuarios con Firebase, control de roles, persistencia local con Room, favoritos, sincronización y funcionamiento sin conexión utilizando datos almacenados previamente.

Además, incorpora una interfaz visual inspirada en los videojuegos Pokémon, con fondos personalizados, colores temáticos, tarjetas y navegación inferior.

## 3. Objetivos

### Objetivo general

Desarrollar una aplicación móvil Android que integre servicios web, autenticación, persistencia local y arquitectura MVVM.

### Objetivos específicos

- Implementar autenticación mediante Firebase Authentication.
- Gestionar roles de usuario y administrador mediante Cloud Firestore.
- Consumir información de Pokémon utilizando Retrofit.
- Almacenar datos localmente mediante Room.
- Implementar búsqueda y filtros por región y tipo.
- Permitir agregar y eliminar Pokémon favoritos.
- Consultar información previamente guardada sin conexión.
- Implementar notificaciones locales.
- Desarrollar un panel de administración.
- Incorporar pruebas unitarias y mejoras asistidas por inteligencia artificial.

## 4. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Kotlin | Desarrollo de la aplicación |
| Jetpack Compose | Construcción de interfaces |
| Material Design 3 | Componentes visuales |
| MVVM | Arquitectura y separación de responsabilidades |
| StateFlow | Gestión reactiva del estado |
| Retrofit | Consumo de API REST |
| PokeAPI | Información de Pokémon |
| Room | Persistencia local |
| Firebase Authentication | Inicio de sesión y registro |
| Cloud Firestore | Gestión de roles |
| Kotlin Coroutines | Operaciones asíncronas |
| Coil | Carga de imágenes |
| JUnit | Pruebas unitarias |
| Git y GitHub | Control de versiones |

## 5. Funcionalidades

### 5.1. Autenticación

La aplicación permite iniciar sesión mediante correo electrónico y contraseña.

También incorpora un formulario de registro de usuarios, validación de campos y opción para mostrar u ocultar contraseñas.

### 5.2. Gestión de roles

La aplicación distingue dos roles:

**Usuario:** acceso al listado de Pokémon, favoritos, detalles y perfil.

**Administrador:** acceso a las funciones anteriores y al panel de administración.

Los roles son consultados desde Cloud Firestore.

### 5.3. Pokédex

Permite:

- Consultar Pokémon desde PokeAPI.
- Buscar por nombre.
- Filtrar por región.
- Filtrar por tipo.
- Consultar información detallada.
- Visualizar imágenes y tipos.
- Actualizar información deslizando hacia abajo.

### 5.4. Detalles Pokémon

La pantalla de detalle muestra:

- Número y nombre.
- Imagen oficial.
- Tipos.
- Altura y peso.
- Habilidades.
- Estadísticas.
- Versión variocolor.
- Opción para agregar a favoritos.

### 5.5. Favoritos

El usuario puede marcar o desmarcar Pokémon como favoritos.

Los favoritos se almacenan localmente y pueden consultarse desde su sección correspondiente.

### 5.6. Persistencia y modo sin conexión

Room permite almacenar Pokémon en la base de datos local.

Cuando no existe conexión a internet, la aplicación puede mostrar información previamente descargada.

La actualización requiere conexión a la API.

### 5.7. Panel de administrador

Incluye:

- Cantidad de Pokémon almacenados.
- Cantidad de favoritos.
- Sincronización de datos.
- Limpieza de caché.
- Confirmación antes de eliminar los datos locales.

### 5.8. Perfil

Permite consultar el correo electrónico, visualizar el rol asignado, seleccionar un avatar de entrenador y cerrar sesión.

### 5.9. Notificaciones

La aplicación implementa notificaciones locales para informar determinadas operaciones de actualización de datos.

## 6. Arquitectura

Se utiliza el patrón **MVVM (Model-View-ViewModel)**.

La estructura principal se organiza en:

- `data/auth`: autenticación con Firebase.
- `data/local`: Room, DAO y entidades.
- `data/remote`: consumo de PokeAPI mediante Retrofit.
- `data/repository`: acceso y coordinación de datos.
- `domain/model`: modelos de dominio.
- `ui/lista`: listado de Pokémon.
- `ui/detalle`: información detallada.
- `ui/favoritos`: favoritos.
- `ui/admin`: administración.
- `ui/login`: autenticación y perfil.
- `ui/navigation`: navegación entre pantallas.
- `ui/components`: componentes reutilizables.
- `notification`: notificaciones locales.

### Flujo de información

Interfaz Compose → ViewModel → Repository → Retrofit / Room / Firebase

Los cambios se presentan de manera reactiva mediante StateFlow.

## 7. Servicios web

Se consume PokeAPI:

https://pokeapi.co/

Entre los recursos utilizados se encuentran:

- `/pokemon`
- `/pokemon/{id}`
- `/type/{tipo}`

El repositorio combina información de la API con los datos guardados en Room.

## 8. Instalación y ejecución

### Requisitos

- Android Studio.
- Android SDK compatible.
- JDK compatible con Gradle.
- Emulador Android o dispositivo físico.
- Configuración de Firebase correspondiente al proyecto.
- Conexión a internet para la primera sincronización.

### Pasos

1. Clonar el repositorio:

```bash
git clone https://github.com/carlosandia-cpu/lab-integrador-fire-andia.git
```

2. Abrir la carpeta del proyecto en Android Studio.
3. Sincronizar Gradle.
4. Configurar Firebase utilizando el archivo `google-services.json` correspondiente al proyecto, cuando sea necesario.
5. Ejecutar la aplicación en el emulador o dispositivo Android.
6. Iniciar sesión o registrar una cuenta.
7. Explorar las funcionalidades disponibles según el rol.

**Nota:** la configuración de Firebase debe tener habilitados los servicios necesarios y las reglas de seguridad adecuadas. No se deben publicar claves privadas ni contraseñas.

## 9. Pruebas

Se implementaron pruebas unitarias para `ListaViewModel`, utilizando JUnit y herramientas de prueba de corrutinas.

Se contemplan los siguientes escenarios:

1. Error de conexión sin datos guardados.
2. Uso de datos locales ante un fallo de conexión.
3. Notificación después de una actualización exitosa.

Para ejecutar las pruebas unitarias:

```bash
./gradlew testDebugUnitTest
```

En Windows:

```powershell
.\gradlew.bat testDebugUnitTest
```

También se realizaron pruebas manuales relacionadas con búsqueda, filtros, favoritos, sincronización, rotación y navegación.

## 10. Mejoras realizadas con inteligencia artificial

Se utilizó inteligencia artificial como herramienta de apoyo para analizar alternativas de implementación, generar propuestas, revisar código y mejorar la interfaz.

Entre las mejoras trabajadas se encuentran:

- Actualización mediante Pull-to-Refresh.
- Estado vacío con botón para limpiar filtros.
- Validaciones locales en el formulario de inicio de sesión.
- Mejoras en el formulario de registro.
- Personalización visual inspirada en Pokémon.
- Filtros desplegables.
- Corrección de problemas de navegación y adaptación de la interfaz.

El registro del proceso se encuentra en `PROMPTS.md`.

## 11. Control de versiones

El proyecto utiliza Git y GitHub.

**Repositorio:** https://github.com/carlosandia-cpu/lab-integrador-fire-andia

Ramas principales de trabajo:

- `main`: versión principal.
- `mejora-ia`: desarrollo de mejoras asistidas por inteligencia artificial.

## 12. Autor

**Carlos Andia**

Desarrollo, integración, pruebas y documentación del proyecto.

## 13. Conclusión

El proyecto permitió integrar autenticación, consumo de servicios REST, persistencia local, interfaces reactivas y arquitectura MVVM en una aplicación Android.

La Pokédex demuestra cómo combinar herramientas modernas de desarrollo móvil y mejorar progresivamente la experiencia del usuario mediante pruebas y asistencia de inteligencia artificial.