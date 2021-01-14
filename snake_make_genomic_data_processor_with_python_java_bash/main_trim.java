import java.io.IOException;

public class main_trim {
    public static void main(String[] args) throws IOException {
        if (args[0].equals("-h")) {
            System.out.println("i. Functie van het script: Het trimmen van reads die paired zijn, om er zo voor te zorgen dat er " +
                    "\nbij het mappen aan het referentiegenoom goede alignments zijn en zodoende dus slechte reads die niet goed alignen worden gefilterd. \n\n" +

                    "ii. Manier van gebruik (aanroepen) van het script met voorbeelden: Het script neemt de filenamen van twee files met reads als input om te trimmen en de twee filenamen van de output\n" +
                    "na het trimmen van de reads. Dit zijn er altijd twee, omdat reads paired zijn." +
                    "Het script is in de shell commandline aan te roepen" +
                    "door het volgende in te tikken:\n'java main_trim [input_file1_met_reads_filenaam] [input_file2_met_reads_filenaam] [getrimde_reads_ouput_vanuit_file1_filenaam] [getrimde_reads_ouput_vanuit_file2_filenaam]'.\n" +
                    "Het script kan ook via snakemake worden aangeroepen. Dit kan met de volgende commandline:\n" +
                    "'snakemake --snakefile Snakefile_s1100884 trim'. Hierbij wordt de input gebruikt" +
                    " met de input en output file namen van de in snakemake gedefinieerde pipeline.");
        }
        else{
            long startTime = System.currentTimeMillis();
            trim trim = new trim(args);
            long endTime = System.currentTimeMillis();

        System.out.println("That took " + (((endTime - startTime)/1000)/60) + " minutes");

    }
}}