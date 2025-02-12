package com.wsiz.ollamawrapper.ollamaexe;

public class OllamaRunner {

    private final void runOllama() {
        // This will use a SH script. Not supported on Windows, obviously.
        switch (System.getProperty("os.name").toLowerCase()) {
            case "unix":
            case "linux":
            case "mac":
                break;

            default:
                return;
        }

        try {
            final String exe = OllamaExe.get().toString();
            final Process process = new ProcessBuilder(exe, "serve").inheritIO().start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));
            process.waitFor();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public final void makeOllamaThread() {
        final Thread ollamaThread = new Thread(() -> runOllama());

        ollamaThread.setDaemon(true);
        ollamaThread.start();
    }

}
