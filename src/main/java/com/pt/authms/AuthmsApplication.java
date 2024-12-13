package com.pt.authms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;

@SpringBootApplication
public class AuthmsApplication {

    public static void main(String[] args) {
        // Configura manualmente el contexto de Logback para establecer LOG_PATH
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset(); // Resetea cualquier configuración previa

        // Define el directorio donde se generarán los logs
        System.setProperty("LOG_PATH", "./logs");

        // Configura Logback automáticamente con el contexto inicializado
        try {
            new ContextInitializer(context).autoConfig();
        } catch (Exception e) {
            System.err.println("Error al configurar Logback: " + e.getMessage());
        }

        // Ejecuta la aplicación Spring Boot
        SpringApplication.run(AuthmsApplication.class, args);
    }
}
