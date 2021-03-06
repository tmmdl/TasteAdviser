import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import telegram.Interaction;

/**
 * Aim of creating TasteAdviser is to find the particular book, song or movie by taste.
 * That is why, first of all, particular websites providing list of the best books,
 * songs, and movies of all time were parsed. To parse the websites there was used Jsoup library.
 * Thereafter the required data were inserted into table in JSON format.
 * This enables to keep the data that differs from each other depending on type (e.g. book, song or movie) in one table.
 * Encoding and decoding of JSON was handled by using org.json library.
 * The rubenlagus library sets up the interaction part with Telegram.
 */

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new Interaction());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
