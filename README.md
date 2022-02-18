# MIARMA-ProyectoADPSP-Backend

<br />

# Peticiones de registro de Usuario

## Creación de Usuario 1
___

- Adjuntar imagen: Recomendado imagen guest.png.
- Adjuntar json crear_perfil1.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear Usuario.

<br />

## Creación de Usuario 2
___
- Adjuntar imagen: Recomendado imagen guest.png.
- Adjuntar json crear_perfil2.json a la hora de hacer la petición Postman con el content type asignado a "application/json".
- Ejecutar la petición POST llamada Crear Usuario 2.

<br />

# Peticiones de inicio de sesión de Usuario

## Inicio de sesión Usuarios 1 y Usuario 2


- Ejecutar la petición POST de postman hará que automáticamente inicie sesión con estos usuarios respectivamente, ya que los JSONs se encuentra en el body de estas peticiones. Esta petición nos asignará el token de sesión de este usuario que podrá ser usado en próximas peticiones.

<br />

# Usuario actual

Requisitos para esta petición:

    - Tener una sesión iniciada(Ejecutar cualquier petrición de inicio de sesión).

Esta devolverá los datos del usuario que tiene la sesión iniciada en el que se realiza la petición.

<br />

# Solicitud de seguimiento

## Crear la solicitud
---
Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 2

Al hacer esta petición se creara una solicitud de parte del usuario 2, al usuario 1.

<br />

## Aceptar la solicitud
---
Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 1

Esta solicitud aceptará la solicitud ya creada por el Usuario 2.

<br />

## Rechazar la solicitud
---
Requisitos para esta petición:

    - Tener una como sesión actual la del Usuario 1

Esta solicitud rechazará la solicitud ya creada por el Usuario 2.

<br />
