public class User {
    private String chatId, instrument, genre, pattern, text;
    private int octave;
    public User(String chatId){
        this.chatId = chatId;
        instrument = "Piano";
        genre = "DEFAULT";
        pattern = "aeiou";
        text = "";
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
    public String getText(){return text;}
    public void setText(String text){this.text=text;}
    @Override
    public String toString() {
        return "User{" +
                "chatId='" + chatId + '\'' +
                ", instrument='" + instrument + '\'' +
                ", genre='" + genre + '\'' +
                ", pattern='" + pattern + '\'' +
                ", text='" + text + '\'' +
                ", octave=" + octave +
                '}';
    }
}
