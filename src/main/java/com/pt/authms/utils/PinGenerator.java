package com.pt.authms.utils;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PinGenerator {

    private static final int PIN_LENGTH = 6;
    private static final int EXPIRATION_MINUTES = 60;
    private static final SecureRandom random = new SecureRandom();

    // Almacenamiento temporal de PIN y tiempos de expiración
    private static final Map<String, LocalDateTime> pinStorage = new HashMap<>();

    // Método para generar un nuevo PIN con tiempo de expiración
    public static String generatePin() {
        StringBuilder pinBuilder = new StringBuilder();
        for (int i = 0; i < PIN_LENGTH; i++) {
            int digit = random.nextInt(10);
            pinBuilder.append(digit);
        }

        String pin = pinBuilder.toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        pinStorage.put(pin, expirationTime); // Almacena el PIN y su tiempo de expiración

        return pin;
    }

    // Método para verificar si el PIN es válido y no ha expirado
    public static boolean isPinValid(String pin) {
        LocalDateTime expirationTime = pinStorage.get(pin);

        if (expirationTime == null) {
            return false; // El PIN no existe
        }

        if (LocalDateTime.now().isAfter(expirationTime)) {
            pinStorage.remove(pin); // Remueve el PIN expirado
            return false;
        }

        return true;
    }

    // Método opcional para eliminar el PIN después de la verificación
    public static void removePin(String pin) {
        pinStorage.remove(pin);
    }
}

