import java.io.*;
import java.lang.reflect.Array;
import java.util.*;




public class inventarisatie {

    /*
Input: Twee files met de paired ongetrimde reads of twee files met de paired getrimde reads.

Werking script: Het script telt alle reads door per iedere 4 regels een op te tellen, berekent de gemiddelde lengte van
de reads door de totaal lengte van de reads te delen door het aantal reads,

berekend de minimum read lengte door iedere
iteratie over een read de kleinste lengte op te slaan, maar alleen als die kleiner is dan de al bekende kleinste waarde,

berekend de de maximum read lengte door  bij iedere iteratie de grootste read lengte op te slaan, maar alleen als deze lengte groter is
dan de al bekende grootste read.

Berekend de GC content van alle reads door de per g of c in een read deze op te tellen en deze te delen door het totale aantal nucleotiden
van deze read en deze uitkomst per read bij elkaar op te tellen.

Berekend GC_content per positie door in een dictionary per positie de GC aantallen op te tellen, en dit ook te doen voor de AT of N waardes per positie.
Door de uiteindelijke GC aantallen per positie te delen door de totale AT of N waardes en GC waardes is zo de GC content per positie bekend.

Output: Twee files met de inventarisatie van twee paired ogetrimde reads files of twee paired getrimde reads files.
     */

    private String bestand1;
    private String bestand2;
    private String fn1;
    private String fn2;
    private String fnd;
    private String pathd;


    public inventarisatie(String[] args) throws IOException{
        bestand1 = args[0];
        fn1 = args[1];
        for (int i = 0; i<fn1.length(); i++){
            if (fn1.charAt(i) == '/'){
                String path = fn1.substring(0, fn1.indexOf("/")) + "/";
                String fnrev = new StringBuffer(fn1).reverse().toString();
                String fnr = fnrev.substring(0, fnrev.indexOf("/"));
                String fn = new StringBuffer(fnr).reverse().toString();
                pathd = path;
                fnd = fn;
            }
            else{
                String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
                pathd = absolutePath;
                fnd = fn1;
            }
        }
        inventarisatie(bestand1, fn1);


    }
    public void inventarisatie (String test, String fn) throws IOException {
        Map<Integer, HashMap<String, Integer>> dict_met_posities = new HashMap<Integer, HashMap<String, Integer>>();
        long line_counter = 0;
        double totaal_lengte_Reads=0;
        double aantal_reads = 0;
        int max_length = 0;
        int min_length = 1000000;
        double gc_content_geheel = 0;
        double gem_length;

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathd, fnd)));

        try (BufferedReader in = new BufferedReader(new FileReader(test))) {
            String line;

            while ((line = in.readLine()) != null) {
                line_counter++;
                float gc_totaal = 0;
                float totaal_nuc = 0;
                if ((line_counter % 4) == 2) {
                    aantal_reads++;
                    totaal_lengte_Reads += line.length();
                    if (line.length() < min_length) {
                        min_length = line.length();
                    }
                    if (line.length() > max_length) {
                        max_length = line.length();
                    }
                    for (int i = 1; i <= line.length(); i++) {
                        totaal_nuc++;
                        HashMap<String, Integer> nucs = new HashMap<String, Integer>();
                        nucs.put("ATN", 0);
                        nucs.put("GC", 0);
                        if (line.charAt(i - 1) == 'G' || line.charAt(i - 1) == 'C') {
                            gc_totaal++;
                            nucs.merge("GC", 1, (a, b) -> a + b);
                            if (dict_met_posities.containsKey(i - 1)) {
                                dict_met_posities.get(i - 1).merge("GC", 1, (a, b) -> a + b);
                            } else {
                                dict_met_posities.put(i - 1, nucs);

                            }
                        } else {
                            nucs.merge("ATN", 1, (a, b) -> a + b);
                            if (dict_met_posities.containsKey(i - 1)) {
                                dict_met_posities.get(i - 1).merge("ATN", 1, (a, b) -> a + b);
                            } else {
                                dict_met_posities.put(i - 1, nucs);
                            }
                        }
                    }
                }
                float gc_content_line = (gc_totaal / totaal_nuc);
                if (Double.isNaN(gc_content_line)) {
                } else {
                    gc_content_geheel += gc_content_line;
                }
            }

            writer.append("Aantal reads: " + aantal_reads + "\n");
            writer.append("Gemiddelde readlengte: " + (totaal_lengte_Reads) / (aantal_reads) + "\n");
            writer.append("Lengte kortste read: " + min_length + "\n");
            writer.append("Lengte langste read: " + max_length + "\n");
            writer.append("Globale GC_content: " + ((gc_content_geheel / aantal_reads) * 100) + "%\n");
            writer.append("GC_content per positie: " + "\n");

            for (int i = 1; i <= dict_met_posities.size(); i++) {
                float gc = dict_met_posities.get(i - 1).get("GC");
                float at = dict_met_posities.get(i - 1).get("ATN");
                writer.append("Positie " + i + ": " + (gc / (gc + at) * 100) + "\n");
            }
            writer.close();
        }
    }}



