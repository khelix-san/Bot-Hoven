import java.util.ArrayList;

public class User {
    private long chatId;
    private String instrument;
    private String genre;
    private String pattern;
    private int tempo;
    private ArrayList<String> chords;
    private boolean replaceChords;

    public User(long chatId){
        this.chatId = chatId;
        instrument = "DEFAULT";
        genre = "DEFAULT";
        chords = new ArrayList<String>();
        tempo = -1;
        replaceChords = false;
    }

    public boolean isReplaceChords() {
        return replaceChords;
    }

    public void setReplaceChords(boolean replaceChords) {
        this.replaceChords = replaceChords;
    }

    public void addChord(String s){
        chords.add(s);
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
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

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public ArrayList<String> getChords() {
        return chords;
    }

    public void setChords(ArrayList<String> chords) {
        this.chords = chords;
    }
}
