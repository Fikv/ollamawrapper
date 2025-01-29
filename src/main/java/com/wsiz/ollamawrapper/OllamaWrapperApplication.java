package com.wsiz.ollamawrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wsiz.ollamawrapper.ollamaexe.OllamaRunner;

@SpringBootApplication
public class OllamaWrapperApplication {

    public static void main(final String[] args) {
        final OllamaRunner ollamaRunner = new OllamaRunner();
        ollamaRunner.makeOllamaThread();

        SpringApplication.run(OllamaWrapperApplication.class, args);
    }

}
