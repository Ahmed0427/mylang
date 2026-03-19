package com.github.ahmed0427;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {

    static Stream<Path> mylangFiles() throws IOException {
        return Files.list(Paths.get("examples"))
                    .filter(path -> path.toString().endsWith(".mylang"));
    }


    @ParameterizedTest
    @MethodSource("mylangFiles")
    void testExampleOutput(Path scriptPath) throws IOException {
        PrintStream console = System.out;
        String fileName = scriptPath.getFileName().toString();

        console.println("[INFO] Running test: " + fileName);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            Main.main(new String[]{scriptPath.toString()});
        } finally {
            System.setOut(console); 
        }

        Path expectedPath = Paths.get(scriptPath.toString().replace(".mylang", ".out"));
        String actualOutput = outContent.toString().trim();

        if (Files.exists(expectedPath)) {
            String expectedOutput = Files.readString(expectedPath).trim();
            assertEquals(expectedOutput, actualOutput, "Output mismatch for " + fileName);
            console.println("[INFO] Result for " + fileName + ": PASSED");
        } else {
            console.println("[INFO] No snapshot found. Created: " + expectedPath.getFileName());
            Files.writeString(expectedPath, actualOutput);
        }
    }

}
