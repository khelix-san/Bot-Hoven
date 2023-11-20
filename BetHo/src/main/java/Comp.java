
import java.util.*;
import org.jfugue.player.Player;

public class Comp {
    static String text = "ciao mamma guarda";
    static String vocal = "aeiou";
    static String word = "ABCDEFG";
    static String ottave = "345";

    static String[] armonia = new String[]{"Cmaj", "Amin", "Fmaj", "Gmaj", "Cmaj", "Amin", "Dmin", "Gdom7"};
    static String durate = "qis";

    static  HashMap<String, String[]> note = new HashMap<String, String[]>();
    static Random ran = new Random();

    public static void main (String[] args){

        Player pl = new Player();

        note.put("a", new String[]{"E3", "C4", "A5", "F5", "R"});
        note.put("e", new String[]{"F3", "D4", "B5", "G5", "R"});
        note.put("i", new String[]{"G3", "E4", "C5", "A6"});
        note.put("o", new String[]{"A4", "F4", "D5", "B6"});
        note.put("u", new String[]{"B4", "G4", "E5", "C6"});


        String music = "";

        int j=0;

        for (int i=0; i<text.length(); i++) {
            String c = text.charAt(i)+"";
            if (vocal.contains(c)) {
                int d = ran.nextInt(durate.length());
                int a = ran.nextInt(armonia.length);
                int o = ran.nextInt(ottave.length());
                int r = ran.nextInt(note.get(c).length);
                music = music + " "+note.get(c)[r]+"+"+armonia[j] + durate.charAt(d)+ottave.charAt(o);
                j++;
                if (j>=armonia.length) j=0;
            }
        }
        System.out.println("music = "+ music);
        pl.play(music.toString());
    }
}