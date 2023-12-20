import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
public class Main {
    public static void main(String[] args) {
        try {
            // Create a TelegramBotsApi object to register bots
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Register your bot (instance of Test class) with the TelegramBotsApi
            botsApi.registerBot(new EchoBot());

            System.out.println("Bot is started!");

            // Your bot (instance of Test class) is now running and ready to receive updates
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
