package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {
    private static final Dotenv dotenv = Dotenv.load();

    public static Dotenv getDotenv() {
        return dotenv;
    }
}
