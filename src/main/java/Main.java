import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Main {

    static class EchoBot extends TelegramLongPollingBot {
        @Override
        public String getBotToken(){
            return tgBot.getBotToken(); //Cusom class to provide telegram bot token
        }
    
        @Override
        public void onUpdateReceived(Update update) {
            String msg = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
    
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(msg);
    
            try{
                execute(sendMessage);
            }catch (TelegramApiException e){
                System.out.println("Errore");
                e.printStackTrace();
            }
        }
    
        @Override
        public String getBotUsername() {
            return "Musimatic_bot";
        }
    }

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
