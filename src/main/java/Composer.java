
import java.util.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;


import org.jfugue.player.Player;
import org.staccato.Instruction;

import javax.sound.midi.Instrument;

public class Composer{
    static String text = "Ma comm è bella\n" +
            "sta campagnola\n" +
            "che belli cosce\n" +
            "che tene ‘a campagnola\n" +
            "ha fatt ‘ammore\n";
    static String patternLetters = "aeiou";
    static String word = "ABCDEF";
    static String octave = "345";
    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7", "Emaj"));
    static ArrayList<String> duration = new ArrayList<>(Arrays.asList("WW","WHH","HHHH","WHQQ","HHQQQQ","HQQQQQQ","QQQQQQQQ","WHQII","WHIIII","HHQII"));
    static HashMap<String,String[]> map_consonante = new HashMap<String,String[]>();
    static HashMap<Integer, String> note_altezza = new HashMap<Integer,String>();
    static HashMap<String, String[]> note_consonanti = new HashMap<String, String[]>();
    static HashMap<String,String[]> next_best = new HashMap<String,String[]>();
    static  HashMap<String, ArrayList<String>> note = new HashMap<String, ArrayList<String>>();
    static Random ran = new Random();

    public static void hashmapMaker(){

        //HashMap scelta altezza note
        note_altezza.put(1,"132");
        note_altezza.put(2,"423");
        note_altezza.put(3,"56");
        note_altezza.put(4, String.valueOf(ran.nextInt(999999)));

        //HashMap note consonanti
        note_consonanti.put("A",new String[]{"F#","F","E","D"});
        note_consonanti.put("B",new String[]{"G#","B#","F","E"});
        note_consonanti.put("C",new String[]{"A","D#","G","D"});
        note_consonanti.put("D",new String[]{"F","G","A","E"});
        note_consonanti.put("E",new String[]{"C#","B","F","F#"});
        note_consonanti.put("F",new String[]{"D#","A","G#","A#"});
        note_consonanti.put("G",new String[]{"B","C","E","D"});

        //HashMap Accordi consonanti
        next_best.put("Cmaj",new String[] {"Gmaj","Amin"});
        next_best.put("Amin",new String[] {"Fmaj","Dmin"});
        next_best.put("Fmaj",new String[] {"Gmaj","Dmin"});
        next_best.put("Gmaj",new String[] {"Emaj","Cmaj"});
        next_best.put("Dmin",new String[] {"Gdom7","Amin"});
        next_best.put("Gdom7",new String[] {"Cmaj","Emaj"});
        next_best.put("Emaj",new String[] {"Fmaj","Gmaj"});

        //HashMap Armonie con note consonanti
        map_consonante.put("Cmaj",new String[] {"C","E","G"});
        map_consonante.put("Amin",new String[] {"A","C","E"});
        map_consonante.put("Fmaj",new String[] {"F","A","C"});
        map_consonante.put("Gmaj",new String[] {"G","B","D"});
        map_consonante.put("Dmin",new String[] {"D","F","A"});
        map_consonante.put("Gdom7",new String[] {"G","B","D","F"});
        map_consonante.put("Emaj",new String[] {"E","G#","B"});

        int patternSize = patternLetters.length();
        int octaveSize = octave.length();
        int notesSize = word.length();
        char currentOctave = octave.charAt(0);
        int counter=0;

        for(int i=0;i<patternSize;i++)
            note.put(String.valueOf(patternLetters.charAt(i)),new ArrayList<String>());

        for(int i=0;i < octaveSize*notesSize;i++){
            if(i%6==0 && i!=0) {
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



    public static String nota_succ(String note, String accordo){
        String[] note_accordo = map_consonante.get(accordo);
        String[] note_succs = note_consonanti.get(note.split("#")[0]);
        for(String i : note_accordo){
            for(String j : note_succs){
                if(j.contains(i)){
                    return j;
                }
            }
        }

        return note;
    }


    public static void main (String[] args){

        Player pl = new Player();
        hashmapMaker();
        printNoteMap(note);
        String instrument = "Piano";
        String music = "I["+instrument+"] "; //inizializzazione pattern musicale con strumento (scelto dall'utente)
        int n_note = ran.nextInt(duration.size());
        int j=0;

        String tmp = randur(duration.get(n_note));

        String accordo=armonia.get(ran.nextInt(armonia.size()));

        String nota = map_consonante.get(accordo)[ran.nextInt(map_consonante.get(accordo).length)];
        for(int i=0;i<text.length();i++) {
            String c = text.charAt(i) + "";

            if (patternLetters.contains(c) || true) {
            //if (true) {
                if (j >= duration.get(n_note).length()) { //TERMINAZIONE BATTUTA
                    n_note = ran.nextInt(duration.size());
                    tmp = randur(duration.get(n_note));
                    j = 0;
                    //music += " &mp ";
                }
                if (j == 0) {// INIZIO BATTUTA
                    accordo = next_best.get(accordo)[ran.nextInt(next_best.get(accordo).length)];
                }

                char d = tmp.charAt(j); //prendo l'iesimo carattere dell'iesima scelta
                //String nota = note.get((c)).get(ran.nextInt(note.get((c)).size()));
                //String nota = map_consonante.get(accordo)[ran.nextInt(map_consonante.get(accordo).length)] + note_altezza.get(2).charAt(ran.nextInt(note_altezza.get(2).length())); //nota presa dal hashmap map_consonante + ottava presa randomicamente dalla stringa di ottave
                String componente_musica = nota + note_altezza.get(3).charAt(ran.nextInt(note_altezza.get(3).length()));
                nota = nota_succ(nota,accordo);
                music += componente_musica + d + accordo + ((d=='H' || d=='W')?"":" "); //controllo pausa, se la nota è già di suo troppo lunga non viene utilizzata la pausa tra una battuta e l'altra.

                j++; //prossima armonia
            }
        }

        music = music.trim();
        org.jfugue.pattern.Pattern pt = new org.jfugue.pattern.Pattern(music);
        System.out.println(pt.toString());
        pl.play(pt);
    }
}
