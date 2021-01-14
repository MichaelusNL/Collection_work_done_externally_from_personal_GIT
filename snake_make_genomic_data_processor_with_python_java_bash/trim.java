import java.io.*;
import java.lang.*;
import java.util.ArrayList;

public class trim {

    /*
Input: namen van de te trimmen files. En de outputnamen van deze files na trimming.
Werking script: Het script leest beide genomen tegelijk in, dit wordt gedaan om er voor te zorgen dat de paired reads
bij het verwijderen van een slechte read blijven overeenkomen. Dus als er een read wordt uitgegooid, dan wordt dit ook automatisch
gedaan voor zijn pair.

Het script leest eerst forward en kijkt dan naar de phred score van een frame van 4 basen.
Zodra een frame een gemiddelde phred score heeft van 25 of hoger, dan knipt het
script de read op het begin van de frame(omdat de frame met phred >= 25 is goedgekeurd wordt deze frame dus meegenomen in de read).

Dit wordt gedaan omdat natuurlijk alleen slechte frames moeten worden weggeknipt en op het moment dat de read goed is na knippen
dit een teken is van een goede read.

Na te hebben geknipt forwardly wordt de read in reverse gezet en wordt dit process herhaald. Na deze laatste knip, wordt
de string weer gereversed en is de read aan beide kanten geknipt.

Nu de eerste read van de paired reads is getrimmed wordt de tweede read van het pair getrimd, via hetzelfde stappenplan. Op
het moment dat dat klaar is dan wordt er gecheckt op de grootte van de uiteindelijk getrimde reads. Als deze kleiner of gelijk is
aan 20 dan worden beiden reads weggegooid en niet opgenomen in de uiteindelijke trimfile.

Keuze phred 25: Er is gekozen
voor een phred van 25, omdat de meeste programma's zoals cutadapt een phred hanteren van 20, maar ik wilde zelf een wat strengere
waarde, daarom koos ik voor 25. Echter een waarde hoger dan dit zou dan weer te streng zijn, dit zou dan teveel reads weigeren die
wel goed genoeg zijn voor een alignment. Dit is ook getest door zelf met de fred waarde te spelen en het aantal overgehouden reads
te bekijken en door te kijken naar de alignment rate die deze produceerde. Volgens literatuur is de consensus dat 75-85% alignment rate
als goed wordt beschouwen een 81.72% overall alignment rate, is dus met deze fred te beschouwen als zeer goed.

Keuze frame grootte van 4: Er is hier voor 4 gekozen als frame zodat een outlier in een goed gebied niet meteen
de read doet afkeuren. Maar ook om er voor te zorgen dat de frame niet te groot is, wat als gevolg geeft
dat outliers totaal niet uitmaken.

Keuze afkeuren read op lengte 20: Hier is voor 20 gekozen, omdat uit literatuur raadpleging duidelijk werd dat reads
met een lagere lengte op teveel plekken (incorrect) alignen en dat als reads veel groter zijn er juist te weinig alignment is
doordat deze te specifiek worden.

Output: Twee bestanden met de getrimde paired reads.
*/

    private int min_length = 15;
    private int cutoff = 25;
    private int sliding = 4;
    private String test1;
    private String test2;
    private String fn1;
    private String fn2;
    private String nucs;
    private String nucs2;
    private String pathd;
    private String fnd;
    private String fnd2;
    private String pathd2;
    private ArrayList<String> vlines = new ArrayList<String>();
    private ArrayList<String> vlines2 = new ArrayList<String>();


    public trim(String args[]) throws IOException{
        test1 = args[0];
        test2 = args[1];
        fn1 = args[2];
        fn2 = args[3];
        for (int i = 0; i<fn1.length(); i++){
            if (fn1.charAt(i) == '/'){
                String path1 = fn1.substring(0, fn1.indexOf("/")) + "/";
                String fnrev1 = new StringBuffer(fn1).reverse().toString();
                String fnr1 = fnrev1.substring(0, fnrev1.indexOf("/"));
                String fn1 = new StringBuffer(fnr1).reverse().toString();
                pathd = path1;
                fnd = fn1;
            }
            else{
                String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                pathd = absolutePath;
                fnd = fn1;
            }
        }
        for (int i = 0; i<fn2.length(); i++){
            if (fn2.charAt(i) == '/'){
                String path2 = fn2.substring(0, fn2.indexOf("/")) + "/";
                String fnrev2 = new StringBuffer(fn2).reverse().toString();
                String fnr2 = fnrev2.substring(0, fnrev2.indexOf("/"));
                String fn2 = new StringBuffer(fnr2).reverse().toString();
                pathd2 = path2;
                fnd2 = fn2;
            }
            else{
                String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                pathd2 = absolutePath;
                fnd2 = fn2;
            }
        }
        leesin();
    }

    public void leesin () throws IOException {
        int line_counter = 0;
        BufferedReader in1 = new BufferedReader(new FileReader(test1));
        BufferedReader in2 = new BufferedReader(new FileReader(test2));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathd, fnd)));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File(pathd2, fnd2)));
        try{

            while (true) {
                    String line = in1.readLine();
                    String line2 = in2.readLine();

                    if (line == null || line2 == null)
                        break;
                    line_counter++;


                if ((line_counter%4)==2){
                    nucs = line;
                    nucs2 = line2;
                }
                else if ((line_counter%4)==0){
                String line_cut_forward = cut(line,nucs, 1);
                String reverse = new StringBuffer(line_cut_forward).reverse().toString();
                String nucsreverse = new StringBuffer(nucs).reverse().toString();
                String line_cut_fwbw = cut(reverse, nucsreverse, 1);
                String rightway = new StringBuffer(line_cut_fwbw).reverse().toString();
                nucs = new StringBuffer(nucs).reverse().toString();
                vlines.add(1, nucs+"\n");
                vlines.add(rightway+"\n");

                String line_cut_forward2 = cut(line2,nucs2, 2);
                String reverse2 = new StringBuffer(line_cut_forward2).reverse().toString();
                String nucsreverse2 = new StringBuffer(nucs2).reverse().toString();
                String line_cut_fwbw2 = cut(reverse2, nucsreverse2, 2);
                String rightway2 = new StringBuffer(line_cut_fwbw2).reverse().toString();
                nucs2 = new StringBuffer(nucs2).reverse().toString();
                vlines2.add(1, nucs2+"\n");
                vlines2.add(rightway2+"\n");
                if (vlines.get(1).length()<=20 || vlines2.get(1).length()<=20 || vlines.get(3).length() == 5 || vlines2.get(3).length() == 5){
                    vlines.clear();
                    vlines2.clear();
                }
                else{
                writer.append(vlines.get(0));
                writer.append(vlines.get(1));
                writer.append(vlines.get(2));
                writer.append(vlines.get(3));
                writer2.append(vlines2.get(0));
                writer2.append(vlines2.get(1));
                writer2.append(vlines2.get(2));
                writer2.append(vlines2.get(3));
                vlines.clear();
                vlines2.clear();
                }
                }
                else if ((line_counter%4)==1){
                    vlines.add(line+"\n");
                    vlines2.add(line2+"\n");
                }
                else if((line_counter%4)==3){
                    vlines.add(line+"\n");
                    vlines2.add(line2+"\n");

                }

    }writer.close(); writer2.close();
}
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String cut(String line, String nucsv, int getal){
        int line_c = 0;
        for (int i=1; i<=line.length(); i++){
            if (i >= (line.length()-4)){
                break;
            }
            int window1 = line.charAt(i-1);
            int window2 = line.charAt(i);
            int window3 = line.charAt(i+1);
            int window4 = line.charAt(i+2);

            if ((((window1+window2+window3+window4)/4)-33)>=(cutoff)){
                String line2 = line.substring(i-1, line.length());
                if (getal == 1){
                    nucs = nucsv.substring(i-1, nucsv.length());
                }
                else{
                    nucs2 = nucsv.substring(i-1, nucsv.length());
                }
                return line2;

            }
        }String leine = "ngng";
        return leine; }}
