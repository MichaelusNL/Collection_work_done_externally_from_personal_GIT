import sys
import time
ok = ["A", "G", "C", "T", "a", "g", "c", "t"]
dict_mutatie = {"A>T":0, "A>C":0, "A>G":0, "T>A":0, "T>C":0, "T>G":0, "C>A":0, "C>G":0, "C>T":0, "G>A":0, "G>C":0, "G>T":0}
listmutaties = ["AT", "AC", "AG", "TA", "TC", "TG", "CA", "CG", "CT", "GA", "GC", "GT"]
def read(input, output):

    """Input: de input file naam die het programma gebruikt, dit is de VCF file uit de variant calling stap. Ook wordt de output filenaam
      opgegeven, dit is de naam van de file met de uiteindelijke SNP aantallen en de verhoudingen/aantallen van de inserties en deleties.

      Werking script: Het script leest kolom 3 en 4 gescheiden op tab. Hier is kolom 3 de kolom met de waarde uit het referentiegenoom.
      kolom 4 is de waarde van het ALT genoom. De SNPs worden bepaald uit alle lijnen die de ref kolom en de alt kolom bevatten. Er wordt
      hier ook rekening gehouden met allel varianten, waardoor echt alle SNPS gevonden worden.
      De inserties en deleties worden bepaald door een verschil in lengte tussen ref en alt kolom, maar alleen als er een INDEL tag zit in
      de gehele lijn waar uit gelezen wordt. Als 3 groter is dan 4, heeft er een
      deletie plaatsgevonden en vise versa. Ook hier worden allel varianten meegenomen(een kolom met waardes waar een komma tussen staat).
      Er wordt ook gekeken naar de overgang van een nucleotide in kolom 3 naar een '.' in kolom 4. Dit geeft weer dat er een deletie
      heeft plaatsgevonden, want na alignment is deze nucleotide niet terug te vinden, wat aan wordt gegeven als '.'.

      Output: De file waar alle SNPS inzitten, de verhouding deleties tegenover inserties en het aantal deleties en inserties"""

    insertie = 0
    deletie = 0
    with open(input) as infile:
        for line in infile:
            lines = line
            line = line.split()
            try:
                if ("," in line[4]):
                    line4 = line[4].split(",")
                    for i in range(len(line4)):
                        if (line[3].upper() + line4[i].upper() in listmutaties):
                            mutatie_combo(line[3].upper() + line4[i].upper())
                        if ("INDEL" in lines and line[0][0] != "#"):
                            if (len(line[3]) > len(line4[i])):
                                deletie += 1
                            elif (len(line[3]) < len(line4[i])):
                                insertie += 1

                elif ((line[0][0]) != "#" and "INDEL" in lines):
                    if (len(line[3])>len(line[4])):
                        deletie+=1
                    elif(len(line[3])<len(line[4])):
                        insertie+=1
                    elif (line[3] != "." and line[4] == "."):
                        deletie+=1
                    elif (line[3] == "." and line[4] != "."):
                        insertie+=1

                if line[0][0] != "#" and line[3] in ok and line[4] in ok:
                    mutatie_combo(line[3].upper()+line[4].upper())


            except:
                pass
    wegschrijf(deletie, insertie, dict_mutatie, output)

def mutatie_combo(waarde):
    dict_mutatie[waarde[0]+">"+waarde[1]] += 1


def wegschrijf(deletie, insertie, dict_mutatie, filename):
    f = open(filename, "w")
    f.write("Mijn data heeft " +str(deletie)+" deleties tegenover het referentiegenoom.\n")
    f.write("Mijn data heeft "+str(insertie)+" inserties tegenover het referentiegenoom.\n")
    f.write("Dit is een verhouding van "+str((deletie / insertie))+ " deleties tegenover inserties.\n")
    f.write("Dit is een verhouding van " + str((insertie / deletie)) + " inserties tegenover deleties.\n")
    for key, value in dict_mutatie.items():
        f.write("Aantallen " + str(key) + " "+str(value)+"\n")


if __name__ == '__main__':
    try:
        if (sys.argv[1] == "-h"):
            print("i. Functie van het script: Dit script scant het gemaakte VCF bestand en vergelijkt hier het alt genoom(gemaakt door alignment) met het referentiegenoom\n"
                  "en zal op basis daarvan per soort SNP optellen hoevaak deze voorkomt."
                  " Ook zal het script het aantal inserties en deleties berekenen met de verhouding waarin deze tegenover elkaar voorkomen. "
                  "\n\nii. Manier van gebruik (aanroepen) van het script met voorbeeld: Dit script is in shell aan te roepen door 'python3 snp_indel_counter.py [naam_vcf bestand].vcf [naam_output_file].txt'"
                  "\nin te tikken, zorg er wel voor dat de input hier een .vcf file is en dat je de output filenaam wel opgeeft, hou de volgorde aan van het voorbeeld anders werkt het niet."
                  "\nVia snakemake kan het aanroepen gedaan woorden door 'snakemake --snakefile Snakefile_s1100884 research_question_answer_generator' "
                  "\nin de shell in te tikken. Daarbij wordt automatisch de vcf file gebruikt die bij de pipeline hoort.")
        else:
            start = time.time()
            read(sys.argv[1], sys.argv[2])
            end = time.time()
            print("That took " + str((end - start) / 60) + " minutes")


    except:
        try:
            start = time.time()
            read(snakemake.input.vcf, snakemake.output.snp_indel)
            end = time.time()
            print("That took " + str((end - start) / 60) + " minutes")
        except:
            print("In command line type: 'python3 snp_indel_counter.py -h' voor een handleiding als het niet lukt. Of als je wilt weten hoe het script werkt.")
