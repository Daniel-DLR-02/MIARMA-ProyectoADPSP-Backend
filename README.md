# MIARMA-ProyectoADPSP-Backend

La aplicación inicia en el puerto 8080, por lo que la url base será http://localhost:8080.

# Peticiones:

# Usuario

<br />

# Peticiones de registro de Usuario (/auth/register)

## Creación de Usuario 1


- Adjuntar imagen: Recomendado imagen guest.png.
- Adjuntar json crear_perfil1.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear Usuario.

<br />

## Creación de Usuario 2

- Adjuntar imagen: Recomendado imagen guest.png.
- Adjuntar json crear_perfil2.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear Usuario 2.

<br />

# Peticiones de inicio de sesión de Usuario (/auth/login)

## Inicio de sesión Usuarios 1 y Usuario 2


- Ejecutar la petición POST de postman hará que automáticamente inicie sesión con estos usuarios respectivamente, ya que los JSONs se encuentra en el body de estas peticiones. Esta petición nos asignará el token de sesión de este usuario que podrá ser usado en próximas peticiones.

<br />

# Usuario actual (/me)

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petrición de inicio de sesión).

Esta devolverá los datos del usuario que tiene la sesión iniciada en el que se realiza la petición.

<br />

# Solicitud de seguimiento

## Crear la solicitud (/follow/{nick})

Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 2
    - Especificar nick del usuario a seguir en la url.

Al hacer esta petición se creara una solicitud de parte del usuario 2, al usuario 1.

<br />

## Aceptar la solicitud (/follow/accept/{id})

Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 1
    - Especificar id de la solicitud en la url.

Esta solicitud aceptará la solicitud ya creada por el Usuario 2.

<br />

## Rechazar la solicitud (/follow/decline/{id})

Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 1
    - Especificar id de la solicitud en la url.

Esta solicitud rechazará la solicitud ya creada por el Usuario 2.

<br />

## Ver solicitudes pendientes (/follow/list)

Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 1.
    - Haber hecho la petición de seguimiento con el Usuario 2.

Esta solicitud mostrar las solicitudes de seguimiento que tiene el usuario de la sesión actual.

<br />

# Post

<br />

# Peticiones de creación de Post (/post/)

## Creación de Post de Imágen público

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).

- Adjuntar imagen: Recomendado imagen landscape.jpg.
- Adjuntar json crear_post_publico.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear nuevo post imagen público.

<br />

## Creación de Post de Imágen privado

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).

- Adjuntar imagen: Recomendado imagen landscape.jpg.
- Adjuntar json crear_post_privado.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear nuevo post imagen privado.

<br />

## Creación de Post de video

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).

- Adjuntar imagen: Recomendado video video_prueba.mp4.
- Adjuntar json crear_post_privado.json o bien crear_post_publico.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear nuevo post video.

<br />

# Peticiones de edición de Post (/post/{id})

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).
    - Tener al menos un post creado.
    - Especificar id del post en la url.

- Adjuntar imagen: Recomendado archivo landscape.jpg o video_prueba.mp4.
- Adjuntar json crear_post_privado.json o bien crear_post_publico.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición PUT llamada Editar post.
  
Esta petición modificara el post especificado por id con los nuevos datos enviados.

<br />

# Peticiones de eliminar de Post (/post/{id})

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).
    - Tener al menos un post creado.
    - Especificar id del post en la url.

- Ejecutar la petición DELETE llamada Eliminar post.

Esta petición elimina el post especificado.

<br />

# Mostrar Posts públicos (/post/public)

- Ejecutar petición GET Mostrar posts públicos

Esta petición traera los post públicos de todos los usuarios.

<br />

# Mostrar Posts del usuario logueado (/post/me)

Se recomienda tener algún post creado con el usuario actual.

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).

- Ejecutar petición GET Mostrar posts del usuario logueado.

Esta petición traera los post del usuario actual.

<br />

# Mostrar Post por id (/post/{id})

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).
    - Tener creado el post al que apunta

- Ejecutar petición GET Post por id.

Esta petición traera los post buscado y lo mostrará solo si el post es público o el usuario que hace la petición sigue al usuario propietario del post.

<br />

# Mostrar posts de un usuario (/post/{nick})

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petición de inicio de sesión).
    - Especificar el nick del usuario del cual queremos obtener los posts en la url.

Esta petición traerá los posts públicos del usuario especificado en la url, en caso de que el usuario que hace la petición, siga a este usuario, se mostrarán también los posts públicos.