package com.wsiz.ollamawrapper.ollamaexe;

import java.io.File;
import java.util.Optional;

public class OllamaExe {

    public static final File get() {
        final Optional<File> prop = OllamaExeProperty.getProperty();
        final String cwd = System.getProperty("user.dir");

        if (prop.isEmpty()) {
            return new File(cwd, "./scripts/ollama");
        }

        return prop.get();
    }

}
