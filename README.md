# Reprografía MVC

Aplicación Java de consola que simula la recepción e impresión de trabajos de una
fotocopiadora escolar.

## Requisitos

- JDK 17 o posterior.
- No necesita dependencias externas.

## Compilar y ejecutar

Desde la carpeta del proyecto, en PowerShell:

```powershell
$fuentes = Get-ChildItem -Recurse src/main/java -Filter *.java
javac -d out $fuentes.FullName
java -cp out dam.reprografia.Main
```

La opción `0` simula la llegada de un trabajo por correo. La opción `1` imprime
un único trabajo; se atienden primero los trabajos del profesorado y se conserva
el orden de llegada dentro de cada tipo de cliente. La impresión dura 0,1
segundos por página. El historial conserva los datos de impresión agrupados por
DNI durante la ejecución; no se guarda el contenido del documento ni se persiste
el historial al cerrar el programa.

## Comprobación básica

```powershell
$fuentes = Get-ChildItem -Recurse src/main/java,src/test/java -Filter *.java
javac -d out $fuentes.FullName
java -cp out dam.reprografia.test.TestRepro
```
