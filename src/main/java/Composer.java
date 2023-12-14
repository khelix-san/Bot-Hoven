
import java.util.*;


import org.jfugue.player.Player;

public class Composer{

    static String text = "So, so you think you can tell\n" + //wish you were here, Pink Floyd
            "Heaven from hell?\n" +
            "Blue skies from pain?\n" +
            "Can you tell a green field\n" +
            "From a cold steel rail?\n" +
            "A smile from a veil?\n" +
            "Do you think you can tell?\n" +
            "Did they get you to trade\n" +
            "Your heroes for ghosts?\n" +
            "Hot ashes for trees?\n" +
            "Hot air for a cool breeze?\n" +
            "Cold comfort for change?\n" +
            "Did you exchange\n" +
            "A walk-on part in the war\n" +
            "For a lead role in a cage?";
    static String patternLetters = "aeiou";
    static ArrayList<String> armonia = new ArrayList<>(Arrays.asList("Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7", "Emaj"));
    static ArrayList<String> duration = new ArrayList<>(Arrays.asList("WW","WHH","HHHH","WHQQ","HHQQQQ","HQQQQQQ","QQQQQQQQ","WHQII","WHIIII","HHQII"));
    static HashMap<String,String[]> map_consonante = new HashMap<String,String[]>();
    static HashMap<Integer, String> note_altezza = new HashMap<Integer,String>();
    static HashMap<String, String[]> note_consonanti = new HashMap<String, String[]>();
    static HashMap<String,String[]> next_best = new HashMap<String,String[]>();
    static  HashMap<String, ArrayList<String>> note = new HashMap<String, ArrayList<String>>();
    static HashMap<String, String[]> genre_accord = new HashMap<String, String[]>();

    static Random ran = new Random();

    public static void hashmapMaker(){

        //HashMap generi e accordi - da implementare
        genre_accord.put("rock", new String[]{"Cmaj", "G7", "Dm7", "Csus4", "Asus2"}); //dovremmo aggiungere altre note nel hashmap consonanti
        genre_accord.put("pop", new String[]{"Cmaj", "Amin", "Fmaj", "Gmaj", "Dmin"});

        //HashMap scelta altezza note
        note_altezza.put(1,"132");
        note_altezza.put(2,"423");
        note_altezza.put(3,"56");
        note_altezza.put(4,"78");
        note_altezza.put(5, String.valueOf(ran.nextInt(999999)));

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
            System.out.println();
        }
    }

    public static String randur(String accordo){
        ArrayList<Character> accord2= new ArrayList<Character>();
        String ran_accordo ="";

        for(int i = 0 ; i < accordo.length(); i++) {
            accord2.add(accordo.charAt(i));
        }

        Collections.shuffle(accord2);

        for(Character c : accord2)
            ran_accordo+=c;

        return ran_accordo;
    }

    public static String nota_succ(String note, String accordo){
        String[] note_accordo = map_consonante.get(accordo);
        String[] note_succs = note_consonanti.get(note.split("#")[0]);

        Collections.shuffle(Arrays.asList(note_accordo));
        Collections.shuffle(Arrays.asList(note_succs));

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
        String instrument = "Piano";
        String music = "I["+instrument+"] "; //inizializzazione pattern musicale con strumento (scelto dall'utente)
        int n_note = ran.nextInt(duration.size());
        int j=0;
        int oct=3; //cambia per cambiare ottava
        /*-------------------INPUT---------------------------*/
        Scanner sc = new Scanner(System.in);
        System.out.println("Inserisci un testo per comporre la musica");
        //String text = sc.nextLine();
        /*-----------------------------------------------------------*/
        String tmp = randur(duration.get(n_note));

        String accordo=armonia.get(ran.nextInt(armonia.size()));

        String nota = map_consonante.get(accordo)[ran.nextInt(map_consonante.get(accordo).length)];
        for(int i=0;i<text.length();i++) {
            String c = text.charAt(i) + "";

            if (patternLetters.contains(c)) {
                if (j >= duration.get(n_note).length()) { //TERMINAZIONE BATTUTA
                    n_note = ran.nextInt(duration.size());
                    tmp = randur(duration.get(n_note));
                    j = 0;
                }
                if (j == 0) {// INIZIO BATTUTA
                    accordo = next_best.get(accordo)[ran.nextInt(next_best.get(accordo).length)];
                }

                char d = tmp.charAt(j); //prendo l'iesimo carattere dell'iesima scelta
                String componente_musica = nota + note_altezza.get(oct).charAt(ran.nextInt(note_altezza.get(oct).length()));
                nota = nota_succ(nota,accordo);

                music += componente_musica + d +"+"+ accordo+" ";

                j++; //prossima armonia
            }
        }

        music = music.trim();
        org.jfugue.pattern.Pattern pt = new org.jfugue.pattern.Pattern(music);
        System.out.println(pt.toString());
        pl.play(pt);
    }
}
