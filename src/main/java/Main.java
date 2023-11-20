import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


public class Main {

    static class EchoBot extends TelegramLongPollingBot {
        @Override
        public String getBotToken(){
            return tgBot.getBotToken(); //Cusom class to provide telegram bot token
        }

        public void print(String txt){
            System.out.println(txt);
        }


        /*m:x -> menu
          i:x -> strumento
          g:x -> genere
          a:x -> avanzate
          m:3   -> start
        */
        @Override
        public void onUpdateReceived(Update update) {
            if(update.hasMessage()){
                String msg = update.getMessage().getText();
                String chatId = update.getMessage().getChatId().toString();

                if(msg.equals("/start")){
                    InlineKeyboardMarkup ikm= new InlineKeyboardMarkup();
                    ikm.setKeyboard(mainMenu());
                    SendMessage sendMessage = new SendMessage();

                    sendMessage.setText("Choose an operation:");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(ikm);

                    try{
                        execute(sendMessage);
                    }catch (TelegramApiException e){
                        System.out.println("Errore");
                        e.printStackTrace();
                    }
                }
            }
            else if(update.hasCallbackQuery()){
                CallbackQuery cbQuery = update.getCallbackQuery();
                String data = cbQuery.getData();
                print(data);
                SendMessage send = new SendMessage();
                send.setChatId(cbQuery.getMessage().getChatId());
                send.setText("PRESS- "+data);

                try{
                    execute(send);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }

        private List<List<InlineKeyboardButton>> mainMenu(){
            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
            List<InlineKeyboardButton> buttons1 = new ArrayList<>();
            List<InlineKeyboardButton> buttons2 = new ArrayList<>();

            for(int i=0;i<4;i++){
                InlineKeyboardButton tmp = new InlineKeyboardButton();
                String in;

                switch (i){
                    case 0:
                        in="Instrument";
                        break;
                    case 1:
                        in="Genre";
                        break;
                    case 2:
                        in="Advanced Settings";
                        break;
                    case 3:
                        in="Start";
                        break;
                    default:
                        in="Start";
                        break;
                }

                tmp.setText(in);
                tmp.setCallbackData("m:"+String.valueOf(i));
                if(i<2)buttons1.add(tmp);
                else buttons2.add(tmp);
            }


            inlineButtons.add(buttons1);
            inlineButtons.add(buttons2);
            return inlineButtons;
        }
    
        @Override
        public String getBotUsername() {
            return "Musimatic_bot";
        }


        private SendMessage sendBotMessage(String text, String chatId){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);

            return sendMessage;
        }

    }// END CLASSE BOT-----------------

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
