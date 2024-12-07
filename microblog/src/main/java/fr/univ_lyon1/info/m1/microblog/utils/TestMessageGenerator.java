package fr.univ_lyon1.info.m1.microblog.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestMessageGenerator {
    private static final String DEFAULT_PATH = "src/main/resources/messages/";

    public static void generateMessages() {
        generateMessages(DEFAULT_PATH);
    }

    public static void generateMessages(String messagesPath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> messages = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        messages.add("Just another day at the office...|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(1))) + "|Joey");
        messages.add("Remember to pick up groceries on your way home!|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(2))) + "|Ugo");
        messages.add("Quick status update: project is on track for delivery next week.|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(3))) + "|John");
        messages.add("Has anyone seen my coffee mug?|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(4))) + "|Joey");
        messages.add("The weather is absolutely perfect today. Time for a walk!|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(5))) + "|Ugo");
        messages.add("Meeting cancelled - please disregard previous calendar invite.|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(6))) + "|John");
        messages.add("New feature deployment successful!|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(7))) + "|Joey");
        messages.add("Don't forget about the team lunch tomorrow at noon.|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(8))) + "|Ugo");
        messages.add("Working from home today due to the snowstorm.|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(9))) + "|John");
        messages.add("Happy birthday to our awesome team lead! 🎉|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(10))) + "|Joey");
        messages.add("System maintenance scheduled for tonight at 11 PM EST.|" + dateFormat.format(new Date(currentTime - TimeUnit.DAYS.toMillis(11))) + "|Ugo");

        String filePath = messagesPath + "test-messages.txt";
        try {
            Path path = Paths.get(filePath);
            Files.write(path, messages, StandardCharsets.UTF_8);
            System.out.println("Messages written to " + filePath);
        } catch (IOException e ) {
            System.err.println("Error writing test messages: " + e.getMessage());
        }

    }
}
