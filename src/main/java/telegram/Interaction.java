package telegram;

import dbworks.DataBase;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class is tend to keep interaction with user.
 */

public class Interaction extends TelegramLongPollingBot{

    ArrayList<String> inputs = new ArrayList<String>(); //keeps user`s inputs
    static ArrayList<String> messages = new ArrayList<String>();
    HashMap<String, String> commands = new HashMap<String, String>();

    /**
     * adds passed data to arraylist.
     * in this case data from table based on queries.
     * @param data
     */

    public static void add(String data) {

        messages.add(data);
    }

    /**
     *The method provides interaction with user
     * @param update
     */

    public void onUpdateReceived(Update update) {

        commands.put("/start", "Welcome to TasteAdviser. \nIf you interested in music, please, type: music" +
                " \nIf you interested in films, please, type: film" +
                " \nIf you interested in literature, please, type: literature");
        commands.put("music", "You are in music section. \nIf you want to find music by genre, please, type: genre" +
                " \nIf you want ro find music by artist name, please, type: artist" +
                " \nIf you want to find music by released year, please, type: year");
        commands.put("genre", "please, type the genre you wish");
        commands.put("artist", "please, type the artist name you wish");
        commands.put("year", "please, type the year you wish");
        commands.put("film", "You are in film section. \nIf you want to find a film by genre, please, type: genre" +
                " \nIf you want ro find a film by director name, please, type: director" +
                " \nIf you want to find a film by released year, please, type: year" +
                " \nIf you want to find a film by Imdb score, please, type: imdb");
        commands.put("director", "please, type the director name you wish");
        commands.put("imdb", "please, type the imdb score you wish");
        commands.put("literature", "You are in literature section. \nIf you want to find a book by genre, please, type: genre" +
                " \nIf you want ro find a book by author name, please, type: author" +
                " \nIf you want to find a book by released year, please, type: year");
        commands.put("author", "please, type the author name you wish");

        String commandList = "/start music genre artist year film director imdb book author next";

        if (update.hasMessage()) {

            String query = update.getMessage().getText(); //user`s input
            String text = commands.get(query); //text to be sent
            inputs.add(query);

            if (!commandList.contains(query)) {
                DataBase.get(inputs.get(1), inputs.get(2), inputs.get(3));
                text = messages.get(0);
                SendMessage sms = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(text);
                messages.remove(text);
                System.out.println(text);

                try {
                    execute(sms);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (query.equals("next")) {
                try {
                    text = messages.get(0);
                    SendMessage sms = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(text);
                    messages.remove(text);
                    try {
                        execute(sms);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println(e);
                }
            }
            if (commandList.contains(query) && !query.equals("next")) {
                SendMessage sms = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(text);
                try {
                    execute(sms);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return "4594243944:AAHRpQwP-Vj6rt0EHfxhUS4VpzzVZcwA0bU\n";
    }

    public String getBotUsername() {
        return "TasteAdviser";
    }
}