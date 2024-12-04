package fr.univ_lyon1.info.m1.microblog.model;

import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Default Message Factory. */
public class DefaultMessageFactory implements MessageFactory {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public MessageDecorator createMessage(final String content) {
        return new MessageDecorator(content);
    }

    public MessageDecorator createMessage(final String content, final String userId) {
        return new MessageDecorator(content, userId);
    }

    @Override
    public MessageDecorator createMessage(final String content, final Date publicationDate) {
        MessageDecorator decorator = new MessageDecorator(content);
        if (publicationDate != null) {
            decorator.setPublicationDate(publicationDate);
        }
        return decorator;
    }

    public MessageDecorator createMessage(final String content, final String userId, final Date publicationDate) {
        MessageDecorator decorator = new MessageDecorator(content, userId);
        if (publicationDate != null) {
            decorator.setPublicationDate(publicationDate);
        }
        return decorator;
    }

    @Override
    public List<MessageDecorator> loadMessagesFromFile(final String resourcePath) throws IOException {
        List<MessageDecorator> messages = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException(resourcePath + " not found");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String content = parts[0].trim();
                    String dateStr = parts[1].trim();
                    String username = parts[2].trim();
                    try {
                        Date date = DATE_FORMAT.parse(dateStr);
                        messages.add(createMessage(content, username, date));
                    } catch (ParseException e) {
                        messages.add(createMessage(content, username));
                    }
                }
            }
        }
        return messages;
    }
}
