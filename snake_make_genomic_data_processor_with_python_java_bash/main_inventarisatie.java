import java.io.IOException;
import java.util.Scanner;

public class main_inventarisatie {
    public static void main(String[] args) throws IOException {
        if (args[0].equals("-h")) {
            System.out.println("i. Functie van het script: Het inventariseren van het GC_content percentage van alle aangeleverde reads, \n" +
                    "van het GC_content percentage van iedere positie per reads, de minimum readlengte, de maximum readlengte, de gemiddelde readlengte,\n" +
                     "en het totale aantal reads wat aanwezig is.\n\n" +
                    "ii. Manier van gebruik (aanroepen) van het script met voorbeelden: Dit script neemt als eerste input de filenaam van\n" +
                    "de file met reads die moet worden geïnventariseerd en als tweede input de filenaam van de output van de inventarisatie. \n" +
                    "Het script kan worden aangeroepen met het volgende commando: 'java main_inventarisatie [input_file_met_reads_filenaam] [output_inventarisatie_filenaam]'.\n" +
                    "Het script kan ook via snakemake worden aanaangeroepen, dit kan met de volgende commandline: \n" +
                    "'snakemake --snakefile Snakefile_s1100884 first_qc' of 'snakemake --snakefile Snakefile_s1100884 first_qc'. Hierbij wordt de input gebruikt"  +
                    " met de input en outputfile namen van de in snakemake gedefinieerde pipeline.\n");
        }
        else{
            long startTime = System.currentTimeMillis();
            inventarisatie inventariseer = new inventarisatie(args);
            long endTime = System.currentTimeMillis();

        System.out.println("That took " + (((endTime - startTime)/1000)/60) + " minutes");

    }
}}
