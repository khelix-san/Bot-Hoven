import java.util.ArrayList;

public class User {
    private String chatId, instrument, genre, pattern, text;
    private int octave;

    public User(String chatId){
        this.chatId = chatId;
        instrument = "Piano";
        genre = "DEFAULT";
        text = "Fra’ Martino, campanaro,\n" +
                "cosa fai? Non dormir!\n" +
                "Suona il mattutino, suona il mattutino,\n" +
                "din, don, dan, din, don, dan,\n" +
                "suona il mattutino, din, don, dan!";
        octave = 3;

    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setOctave(int octave){this.octave = octave;}

    public int getOctave(){return octave;}

    private String getText(){return text;}

    private void setText(String text){this.text=text;}



}
