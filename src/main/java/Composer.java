
import java.util.*;
import org.jfugue.player.Player;

public class Composer {
    static String text = "ciao mamma guarda";
    static String patternLetters = "aeiou";
    static String word = "ABCDEFG";
    static String octave = "345";

    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7"));
    static String duration = "qis";

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

        int j=0;

        for (int i=0; i<text.length(); i++) {
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
        }
        System.out.println("music = "+ music);
        pl.play(music.toString());
    }
}