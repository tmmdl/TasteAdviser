package telegram;

import dbworks.DataBase;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

public class Interaction extends TelegramLongPollingBot{

    ArrayList<String> inputs = new ArrayList<String>();
    static ArrayList<String> messages = new ArrayList<String>();
    HashMap<String, String> commands = new HashMap<String, String>();

    public static void add(String data) {

        messages.add(data);
    }

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

        String command = "/start music genre artist year film director imdb book author next";

        if (update.hasMessage()) {

            String query = update.getMessage().getText();
            String text = commands.get(query);
            inputs.add(query);

            if (!command.contains(query)) {
                DataBase.get(inputs.get(1), inputs.get(2), inputs.get(3));
                text = messages.get(0);
                System.out.println("sent sms: " + text);
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
                    System.out.println("sent sms: " + text);
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
                catch (IndexOutOfBoundsException e) {
                    System.out.println(e);
                }
            }
            if (command.contains(query) && !query.equals("next")) {
                SendMessage sms = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(text);
                System.out.println(text);
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