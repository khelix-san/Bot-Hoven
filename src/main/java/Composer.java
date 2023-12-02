
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.jfugue.player.Player;
import org.staccato.Instruction;

public class Composer {
    static String text = "Combattenti di terra, di mare e dell’aria! Camicie nere della rivoluzione e delle legioni! Uomini e donne d’Italia, dell’Impero e del regno d’Albania! Ascoltate! L’ora segnata dal destino batte nel cielo della nostra patria. L’ora delle decisioni irrevocabili. La dichiarazione di guerra è già stata consegnata agli ambasciatori di Gran Bretagna e di Francia. Scendiamo in campo contro le democrazie plutocratiche e reazionarie dell’Occidente, che, in ogni tempo, hanno ostacolato la marcia, e spesso insidiato l’esistenza medesima del popolo italiano.";
    static String patternLetters = "aeiou";
    static String word = "ABCDEFG";
    static String octave = "645";

    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7", "Emaj"));
    static ArrayList<String> duration = new ArrayList<>(Arrays.asList("WW","WHH","HHHH","WHQQ","HHQQQQ","HQQQQQQ","QQQQQQQQ","WHQII","WHIIII","HHQII"));

    static HashMap<String,String[]> map_consonante = new HashMap<String,String[]>();
    static HashMap<Integer, String> note_altezza = new HashMap<Integer,String>();
    static HashMap<String, String[]> note_consonanti = new HashMap<String, String[]>();

 



    static  HashMap<String, ArrayList<String>> note = new HashMap<String, ArrayList<String>>();
    static Random ran = new Random();

    public static void hashmapMaker(){
        int patternSize = patternLetters.length();
        int octaveSize = octave.length();
        int notesSize = word.length();
        char currentOctave = octave.charAt(0);
        int counter=0;

        for(int i=0;i<patternSize;i++)
            note.put(String.valueOf(patternLetters.charAt(i)),new ArrayList<String>());

        for(int i=0;i< octaveSize*notesSize;i++){
            if(i%7==0 && i!=0) {
                counter++;
                currentOctave=octave.charAt(counter);
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

    public static String randur(String accordo){
        ArrayList<Character> accord2= new ArrayList<Character>();
        String ran_accordo ="";

        for(int i = 0 ; i < accordo.length(); i++) {
            accord2.add(accordo.charAt(i));
        }

        int j = accord2.size();
        int index = 0;

        while ( j > 0){
            index = ran.nextInt(j);
            ran_accordo += accord2.get(index);
            accord2.remove(index);
            j--;
        }

        return ran_accordo;
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

        //HashMap scelta altezza note
        note_altezza.put(1,"132");
        note_altezza.put(2,"423");
        note_altezza.put(3,"567");
        note_altezza.put(4, String.valueOf(ran.nextInt(999999)));

        //HashMap note consonanti
        note_consonanti.put("A",new String[]{"F#","F","E","D"});
        note_consonanti.put("B",new String[]{"G#","Bb","F","E"});
        note_consonanti.put("C",new String[]{"A","D#","G","D"});
        note_consonanti.put("D",new String[]{"F","G","A","E"});
        note_consonanti.put("E",new String[]{"C#","B","F","F#"});
        note_consonanti.put("F",new String[]{"D#","A","G#","A#"});
        note_consonanti.put("G",new String[]{"B","C","E","D"});


        Player pl = new Player();
        hashmapMaker();
        printNoteMap(note);
        String instrument = "Piano";
        String music = "I["+instrument+"] "; //inizializzazione pattern musicale con strumento (scelto dall'utente)

        int a = 0;
        int n_note = ran.nextInt(duration.size());
        int j=0;
        String accordo = "";
        String tmp = randur(duration.get(n_note));


        for(int i=0;i<text.length();i++) {
            String c = text.charAt(i) + "";

            if (patternLetters.contains(c)) {
                if (j >= duration.get(n_note).length()) { //TERMINAZIONE BATTUTA
                    n_note = ran.nextInt(duration.size());
                    tmp = randur(duration.get(n_note));
                    j = 0;
                    music += "+";
                    a = ran.nextInt(armonia.size());
                }
                if (j == 0)// INIZIO BATTUTA
                    accordo = armonia.get(a);

                char d = tmp.charAt(j); //prendo l'iesimo carattere dell'iesima scelta
                //String nota = note.get((c)).get(ran.nextInt(note.get((c)).size()));
                String nota = map_consonante.get(accordo)[ran.nextInt(map_consonante.get(accordo).length)] + note_altezza.get(2).charAt(ran.nextInt(note_altezza.get(2).length())); //nota presa dal hashmap map_consonante + ottava presa randomicamente dalla stringa di ottave

                music += nota + d + accordo + ((d=='H' || d=='W')?"":" "); //controllo pausa, se la nota è già di suo troppo lunga non viene utilizzata la pausa tra una battuta e l'altra.

                j++; //prossima armonia
            }
        }
        System.out.println(music);
        pl.play(music.toString());
    }
}
