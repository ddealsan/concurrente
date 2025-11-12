# Concurrente Benchmark - Análisis de rendimiento con Spring Boot

## Introducción
Este proyecto implementa una aplicación desarrollada en Spring Boot para analizar el rendimiento y la eficiencia de diferentes estrategias de ejecución concurrente en Java. El objetivo es comparar tres modos de ejecución de tareas bajo distintos modelos de concurrencia, midiendo tiempos de ejecución, aceleración y eficiencia.

## Descripción general
La aplicación permite configurar el número de tareas y el número máximo de hilos desde un endpoint REST. Cada modo ejecuta un conjunto de tareas simuladas (operaciones computacionales intensivas) y calcula las siguientes métricas:

- Tiempo total de ejecución (ms)
- Aceleración (Speedup = T_secuencial / T_concurrente)
- Eficiencia (Efficiency = Speedup / número_de_hilos)

Los resultados se devuelven en formato JSON formateado.

## Modos de ejecución implementados
1. SEQUENTIAL: ejecución secuencial en un solo hilo.
2. EXECUTOR_SERVICE: ejecución concurrente utilizando ExecutorService con un pool de hilos configurable.
3. SPRING_ASYNC: ejecución asíncrona mediante métodos anotados con @Async.

## Endpoints disponibles

### GET /benchmark/modes
Devuelve los modos de ejecución disponibles y su descripción.

### POST /benchmark/start?tasks={n}&threads={p}
Inicia una prueba de rendimiento con el número indicado de tareas e hilos.

### GET /benchmark/result
Devuelve el resultado del último benchmark ejecutado.

## Ejemplo de uso

### 1. Ejecutar la aplicación
Desde terminal:
mvn spring-boot:run  
El servidor estará disponible en:
http://localhost:8080

### 2. Realizar peticiones desde consola

Modos de ejecución disponibles:
curl.exe -X GET "http://localhost:8080/benchmark/modes"

Iniciar benchmark con 50 tareas y 8 hilos:
curl.exe -X POST "http://localhost:8080/benchmark/start?tasks=50&threads=8"

Consultar el último resultado:
curl.exe -X GET "http://localhost:8080/benchmark/result"

## Ejemplo de salida JSON
{
  "totalTasks": 50,
  "threadsUsed": 8,
  "results": [
    {
      "mode": "SEQUENTIAL",
      "timeMs": 483,
      "speedup": 1.0,
      "efficiency": 1.0
    },
    {
      "mode": "EXECUTOR_SERVICE",
      "timeMs": 77,
      "speedup": 6.27,
      "efficiency": 0.78
    },
    {
      "mode": "SPRING_ASYNC",
      "timeMs": 478,
      "speedup": 1.01,
      "efficiency": 0.12
    }
  ]
}

## Archivos relevantes del proyecto

- ConcurrenteBenchmarkApplication.java: Clase principal del proyecto.
- controller/BenchmarkController.java: Contiene los endpoints /modes, /start y /result.
- service/BenchmarkService.java: Implementa la lógica de ejecución, tiempos y métricas.
- config/AsyncConfig.java: Configura el ejecutor asíncrono ThreadPoolTaskExecutor.
- resources/application.properties: Configuración del proyecto y formato JSON.

## Tecnologías utilizadas
- Java 17
- Spring Boot 3.x
- Maven
- ExecutorService y @Async
- cURL para pruebas desde consola

## Autor
Proyecto desarrollado por Daniel de Alfonso, Jose Carlos Zorrilla y Dmitry Kravets para la asignatura Programación Concurrente.
