
import java.util.*;
import org.jfugue.player.Player;

public class Composer {
    static String text = "oroscopo del giorno quindici maggio";
    static String patternLetters = "aiou";
    static String word = "ABCDEF";
    static String octave = "345";

    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7", "Emaj"));
    static ArrayList<String> duration = new ArrayList<>(Arrays.asList("WW","WHH","HHHH","WHQQ","HHQQQQ","HQQQQQQ","QQQQQQQQ","WHQII","WHIIII","HHQII"));

    static HashMap<String,String[]> map_consonante = new HashMap<String,String[]>();

 



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

        //HashMap Armonie con note consonanti
        map_consonante.put("Cmaj",new String[] {"C","E","G"});
        map_consonante.put("Amin",new String[] {"A","C","E"});
        map_consonante.put("Fmaj",new String[] {"F","A","C"});
        map_consonante.put("Gmaj",new String[] {"G","B","D"});
        map_consonante.put("Dmin",new String[] {"D","F","A"});
        map_consonante.put("Gdom7",new String[] {"G","B","D","F"});
        map_consonante.put("Emaj",new String[] {"E","G#","B"});

        Player pl = new Player();
        hashmapMaker();
        printNoteMap(note);

        String music = "";

        int a = 0;
        int n_note = ran.nextInt(duration.size());
        int j=0;
        String accordo="";

        for(int i=0;i<text.length();i++) {
            String c = text.charAt(i) + "";

            if (patternLetters.contains(c)) {
                if (j >= duration.get(n_note).length()) { //TERMINAZIONE BATTUTA
                    n_note = ran.nextInt(duration.size());
                    j = 0;
                    music += "+";
                    a = ran.nextInt(armonia.size());
                }
                if (j == 0)// INIZIO BATTUTA
                    accordo = armonia.get(a);

                char d = duration.get(n_note).charAt(j); //prendo l'iesimo carattere dell'iesima scelta
                //String nota = note.get((c)).get(ran.nextInt(note.get((c)).size()));
                String nota = map_consonante.get(accordo)[ran.nextInt(map_consonante.get(accordo).length)] + octave.charAt(ran.nextInt(octave.length())); //nota presa dal hashmap map_consonante + ottava presa randomicamente dalla stringa di ottave

                music += nota + d + accordo+ ((d=='H' || d=='W')?"":" ");

                j++;
            }
        }
        System.out.println(music);
        pl.play(music.toString());
    }
}
