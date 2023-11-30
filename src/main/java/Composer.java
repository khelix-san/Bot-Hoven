
import java.util.*;
import org.jfugue.player.Player;

public class Composer {
    static String text = "oroscopo del giorno quindici maggio";
    static String patternLetters = "aiou";
    static String word = "ABCDEF";
    static String octave = "345";

    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7"));
    static ArrayList<String> duration = new ArrayList<>(Arrays.asList("WW","WHH","HHHH","WHQQ","HHQQQQ","HQQQQQQ","QQQQQQQQ","WHQII","WHIIII","HHQII")); //durata fatta da noi troppo bella de prisco stai sott


    static  HashMap<String, ArrayList<String>> note = new HashMap<String, ArrayList<String>>();
    static Random ran = new Random();

    public static void hashmapMaker(){
        int patternSize = patternLetters.length();
        int octaveSize = octave.length();
        int notesSize = word.length();
        int currentOctave = octave.charAt(0) - '0';

        for(int i=0;i<patternSize;i++)
            note.put(String.valueOf(patternLetters.charAt(i)),new ArrayList<String>());

        for(int i=0;i< octaveSize*notesSize;i++){
            if(i%7==0 && i!=0) {
                ++currentOctave;
            }

            ArrayList<String> tmp = note.get(String.valueOf(patternLetters.charAt(i%patternSize)));
            tmp.add(word.charAt(i%notesSize)+""+currentOctave);
            note.put(String.valueOf(patternLetters.charAt(i%patternSize)),tmp);
        }
    }

    public static void printNoteMap(Map<String, ArrayList<String>> note) {
        Map<String, ArrayList<String>> sortedNote = new TreeMap<>(note);

        for (Map.Entry<String, ArrayList<String>> entry : sortedNote.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> values = entry.getValue();

            System.out.print("Key: " + key + ", Values: ");
            for (String value : values) {
                System.out.print(value + " ");
            }
            System.out.println(); // Move to the next line after printing values for a key
        }
    }

    public static void main (String[] args){

        Player pl = new Player();

        /*note.put("a", new ArrayList<>(Arrays.asList("E3", "C4", "A5", "F5", "R")));
        note.put("e", new ArrayList<>(Arrays.asList("F3", "D4", "B5", "G5", "R")));
        note.put("i", new ArrayList<>(Arrays.asList("G3", "E4", "C5", "A6")));
        note.put("o", new ArrayList<>(Arrays.asList("A4", "F4", "D5", "B6")));
        note.put("u", new ArrayList<>(Arrays.asList("B4", "G4", "E5", "C6")));
        */

        hashmapMaker();
        printNoteMap(note);

        String music = "";

        int a=0;
        int n_note = ran.nextInt(duration.size());
            //RIFARE DA 0 QUESTA PARTE DELLA SCELTA
        for(int i=0;i<text.length();i++){
            String c = text.charAt(i)+"";
            for(int j=0;j<duration.get(n_note).length();j++){
                if(patternLetters.contains(c)){
                    char d = duration.get(n_note).charAt(j); //prendo l'iesimo carattere dell'iesima scelta
                    int ar = ran.nextInt(armonia.size());
                    int o = ran.nextInt(octave.length());
                    int r = ran.nextInt(note.get(c).size());
                    music = music + " "+note.get(c).get(r)+"+"+armonia.get(a) + d + octave.charAt(o);
                    a++;
                    if (a>=armonia.size()) a=0;
                }
            }
            n_note = ran.nextInt(duration.size());
        }
        /*for (int i=0; i<text.length(); i++) {
            String c = text.charAt(i)+"";
            if (patternLetters.contains(c)) {
                int d = ran.nextInt(duration.length());
                int a = ran.nextInt(armonia.size());
                int o = ran.nextInt(octave.length());
                int r = ran.nextInt(note.get(c).size());
                music = music + " "+note.get(c).get(r)+"+"+armonia.get(j) + duration.charAt(d)+ octave.charAt(o);
                j++;
                if (j>=armonia.size()) j=0;
            }
        }*/
        System.out.println("music = "+ music);
        pl.play(music.toString());
    }
}