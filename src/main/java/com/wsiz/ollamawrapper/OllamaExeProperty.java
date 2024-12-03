package com.wsiz.ollamawrapper;

import java.io.File;
import java.util.Optional;

// CLI: mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DollamaExe=/usr/bin/ollama"
// Code: OllamaExeProperty.getProperty().get()

public class OllamaExeProperty {

    private static Optional<File> property;

    static {
        final String prop = System.getProperty("ollamaExe");

        switch (prop) {
            case "FALSE":
            case "NO":
            case null:
                property = Optional.empty();
                break;
            default:
                property = Optional.of(new File(prop));
                break;
        }
    }

    private OllamaExeProperty() {

    }

    public static final Optional<File> getProperty() {
        return property;
    }

}
