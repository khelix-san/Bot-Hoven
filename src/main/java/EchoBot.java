import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class EchoBot extends TelegramLongPollingBot {

    private static HashMap<String, User> lista_user = new HashMap<String,User>();
    @Override
    public String getBotToken(){
        return tgBot.getBotToken(); //Custom class to provide telegram bot token
    }
    /*m:x -> menu
      i:x -> strumento
      g:x -> genere
      a:x -> avanzate
      n:x -> note level
      m:3   -> start
    */
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            String msg = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if(msg.equals("/start")){

                if(!lista_user.containsKey(chatId)){
                    lista_user.put(chatId, new User(chatId));
                    System.out.println("User aggiunto con chatId: "+ chatId);
                }

                InlineKeyboardMarkup ikm= new InlineKeyboardMarkup();
                ikm.setKeyboard(mainMenu());
                SendMessage sendMessage = new SendMessage();

                sendMessage.setText("Welcome to Bot-Hoveen, the first bot that allows you to create music from a phrase!\n" +
                        "In the menu below you can customize the result or start the simulation, enjoy!");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(ikm);

                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    System.out.println("Errore");
                    e.printStackTrace();
                }
            }
            else{
                User u;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                if(lista_user.containsKey(chatId)){
                    u=lista_user.get(chatId);

                    System.out.println("PRE: "+u.toString());

                    if(u.getPattern().equals("INSERT")){
                        u.setPattern(msg);
                        sendMessage.setText("Pattern saved!");
                    } else if (u.getText().equals("INSERT")) {
                        u.setText(msg);
                        sendMessage.setText("What a musical composition! \n" +
                                "I think I have found the next Mozart!\n" +
                                "If you have more in mind, keep using the composer!");
                        Composer.compose(u);

                        String command = "python3 src/main/pyScript/converter.py "+chatId;

                        try {
                            Process process = Runtime.getRuntime().exec(command);
                            process.waitFor();

                            execute(sendMp3(chatId,"src/main/fileOutput/"+chatId+".mp3",""));

                            command="rm src/main/fileOutput/"+chatId+".mp3";

                            process = Runtime.getRuntime().exec(command);
                            process.waitFor();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        sendMessage.setText("No configuration changed!");
                    }

                    InlineKeyboardMarkup ikm= new InlineKeyboardMarkup();
                    ikm.setKeyboard(mainMenu());
                    sendMessage.setReplyMarkup(ikm);

                    System.out.println("POST: "+u.toString());

                }
                else {
                    sendMessage.setText("ERROR, type /start to begin");
                }

                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    System.out.println("Errore");
                    e.printStackTrace();
                }
            }
        }
        else if(update.hasCallbackQuery()){
            User u = lista_user.get(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));

            CallbackQuery cbQuery = update.getCallbackQuery();
            String data = cbQuery.getData();
            long id = cbQuery.getMessage().getChatId();
            //ricavo il menu
            char type = data.split(":")[0].charAt(0);
            char option = data.split(":")[1].charAt(0);
            InlineKeyboardMarkup ikm= new InlineKeyboardMarkup();

            SendMessage send = new SendMessage();
            switch (option){
                case '0':
                    if(type == 'm'){
                        ikm.setKeyboard(Instrument());
                        send.setText("Choose which instrument to play on :");//funzione che accetta come parametro il numero affiancato alla lettera e crea il sottomenù corrispettivo
                        //es m:1 ti porta al menù dei generi
                    } else if (type == 'i') {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the piano instrument, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setInstrument("Piano");
                    } else if (type == 'g') {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the pop genre, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setGenre("pop");
                    }else if(type == 'a'){
                        ikm.setKeyboard(octaveChoose());
                        send.setText("Choose which octave you want to use in this composition");
                    }else if (type == 'r'){
                        ikm.setKeyboard(mainMenu());
                        send.setText("Have you changed your mind? Customize the composer and when you are ready remember to press the \"start\" button!");
                    }else{
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the low notes! Get ready for spooky music! Continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setOctave(1);
                    }
                    break;
                case '1':
                    if(type == 'm'){
                        ikm.setKeyboard(Genre());
                        send.setText("Choose the music genre you prefer :");
                    } else if (type == 'i') {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the guitar instrument, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setInstrument("Guitar");
                    } else if (type == 'g') {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the rock genre, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setGenre("rock");
                    }else if (type == 'a'){
                        ikm.setKeyboard(onlyReturn());
                        send.setText("Set your pattern, remember the current pattern is \""+u.getPattern()+"\"");
                        u.setPattern("INSERT");
                    }else {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the high notes! Get ready for music that will send you to heaven! Continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setOctave(4);
                    }
                    break;
                case '2':
                    if (type == 'm'){
                        ikm.setKeyboard(AdSettings());
                        send.setText("Are you a real expert? \n" + "Well have fun customizing to the best of your ability!");
                    } else if (type == 'i') {
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the flute instrument, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setInstrument("Flute");
                    }else if (type == 'n'){
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the middle notes, continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setOctave(2);
                    }else if (type == 'a'){
                        ikm.setKeyboard(mainMenu());
                        String user_id = u.getChatId();
                        lista_user.remove(user_id);
                        lista_user.put(user_id,new User(user_id));
                        send.setText("We've done a good cleaning, haven't we?\n" +
                                "Let's get back to composing new music! Remember that now the bot has restored all its standard values!");
                    }else{
                        ikm.setKeyboard(mainMenu());
                        send.setText("Ready to play? Press the \"Start\" button and let's get the dancing started!");
                    }
                    break;
                case '3':
                    if( type == 'm'){
                        ikm.setKeyboard(onlyReturn());
                        send.setText("Set the text from which to create your musical composition!");
                        u.setText("INSERT");
                        if(u.getPattern().equals("INSERT")) u.setPattern("DEFAULT");
                    } else if (type == 'n'){
                        ikm.setKeyboard(mainMenu());
                        send.setText("Set the random notes, ready to discover your composition? Continue to customize the composer or press the \"Start\" button to start playing!");
                        u.setOctave(5);
                    }else{
                        ikm.setKeyboard(mainMenu());
                        send.setText("Ready to play? Press the \"Start\" button and let's get the dancing started!");
                    }
                    break;
                case '4':
                    ikm.setKeyboard(mainMenu());
                    send.setText("Ready to play? Press the \"Start\" button and let's get the dancing started!");
                    break;
            }
            send.setChatId(id);
            send.setReplyMarkup(ikm);
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
    private List<List<InlineKeyboardButton>> onlyReturn(){
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();


        InlineKeyboardButton tmp = new InlineKeyboardButton();
        String in = "return";


        tmp.setText(in);
        tmp.setCallbackData("r:"+0);
        buttons1.add(tmp);



        inlineButtons.add(buttons1);
        return inlineButtons;
    }

    private List<List<InlineKeyboardButton>> Instrument(){
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();

        InlineKeyboardMarkup ikm= new InlineKeyboardMarkup();
        SendMessage send = new SendMessage();

        for(int i = 0; i<4; i++){
            InlineKeyboardButton tmp = new InlineKeyboardButton();
            String in = "";

            switch (i){
                case 0:
                    in="Piano";
                    break;
                case 1:
                    in = "Guitar";
                    break;
                case 2:
                    in = "Flute";
                    break;
                case 3:
                    in = "Return";
                    ikm.setKeyboard(mainMenu());
                    break;
                default:
                    break;
            }
            tmp.setText(in);
            tmp.setCallbackData("i:"+String.valueOf(i));
            if(i<2) buttons1.add(tmp);
            else buttons2.add(tmp);
        }
        inlineButtons.add(buttons1);
        inlineButtons.add(buttons2);
        return inlineButtons;

        //send.setChatId(id);
    }

    private List<List<InlineKeyboardButton>> Genre(){
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();

        for(int i=0; i<3; i++){
            InlineKeyboardButton tmp = new InlineKeyboardButton();
            String in = "";

            switch (i){
                case 0:
                    in = "Pop";
                    break;
                case 1:
                    in = "Rock";
                    break;
                case 2:
                    in = "Return";
                    break;
                default:
                    break;
            }
            tmp.setText(in);
            tmp.setCallbackData("g:"+String.valueOf(i));
            if (i<2) buttons1.add(tmp);
            else buttons2.add(tmp);
        }
        inlineButtons.add(buttons1);
        inlineButtons.add(buttons2);
        return inlineButtons;
    }

    private List<List<InlineKeyboardButton>> AdSettings(){
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            InlineKeyboardButton tmp = new InlineKeyboardButton();
            String in = "";

            switch (i){
                case 0:
                    in = "Custom octave";
                    break;
                case 1:
                    in = "Custom pattern";
                    break;
                case 2:
                    in = "Reset";
                    break;
                case 3:
                    in = "Return";
                    break;
                default:
                    break;
            }
            tmp.setText(in);
            tmp.setCallbackData("a:"+String.valueOf(i));
            if (i<2) buttons1.add(tmp);
            else buttons2.add(tmp);
        }
        inlineButtons.add(buttons1);
        inlineButtons.add(buttons2);
        return inlineButtons;
    }

    private List<List<InlineKeyboardButton>> octaveChoose(){
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        for(int i = 0; i < 5 ; i++){
            InlineKeyboardButton tmp = new InlineKeyboardButton();
            String in = "";

            switch (i){
                case 0:
                    in = "Low notes";
                    break;
                case 1:
                    in = "High notes";
                    break;
                case 2:
                    in = "Medium notes";
                    break;
                case 3:
                    in = "Random notes";
                    break;
                case 4:
                    in = "Return";
                    break;
                default:
                    break;
            }
            tmp.setText(in);
            tmp.setCallbackData("n:"+String.valueOf(i));
            if (i<2) buttons1.add(tmp);
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

    private SendDocument sendMp3(String chatId, String fileName, String caption) throws TelegramApiException {

        InputFile file = new InputFile(new File(fileName),fileName);

        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(file);
        sendDocumentRequest.setCaption(caption);

        return sendDocumentRequest;
    }

        /*ESEMPIO USO SendDcoument()

         try {
                execute(sendMp3(chatId,"DEFINIRE PERCORSO(magari far salvare in resources)","Here is your"));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            questo blocco va inserito nella funzione onUpdateReceived()
         */

}// END CLASSE BOT-----------------