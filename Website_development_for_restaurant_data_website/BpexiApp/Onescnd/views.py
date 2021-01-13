from .models import RestaurantData
from django.http import HttpResponseRedirect
from django.contrib.auth import authenticate, login, logout
from django.shortcuts import render
from django.core.cache import cache
from datetime import datetime
import datetime
import pandas as pd
import os
import sqlite3
from Onescnd.heatmap_maken import Heatmap_maken
from Onescnd.lijngrafiek_maken import Lijngrafiek_maken
from Onescnd.barplot_maken import Barplot_maken
from Onescnd.datum_verwerking import Datum_verwerking


user = None
queryset = ""
df = ""
setting = 'dag'
day = datetime.datetime.today().strftime("%Y-%m-%d")
l = Lijngrafiek_maken()
h = Heatmap_maken()
b = Barplot_maken()
d = Datum_verwerking()


def start(request):
    """
    Deze methode stuurt het beginscherm van de applicatie aan; het
    loginscherm. De methode start met het aanmaken van een variabele
    waar een string in kan worden gezet. Deze variabele zal dienen als
    waarschuwingstekst wanneer men met ongeldige inloggegevens de
    website wilt betreden.

    Hierna wordt gekeken of er op een knop is gedrukt in het forumulier
    van de respectievelijke webpagina, waarna er wordt gekeken welke
    knop is gebruikt. In dit geval is dat de knop [aanmelden]. Dit is
    de 'log in' knop van de pagina. Als er op deze knop wordt gedrukt
    worden de tekstvakken 'username' en 'password' uitgelezen en diens
    input opgeslagen in de variabelen 'username' en 'password'.

    Deze variabelen worden gebruikt om te controleren of het correcte
    inlog gegevens zijn. De uitkomst hiervan wordt opgeslagen in de
    variabele 'user'.

    Een laatste controle wordt uitgevoerd of de user None is; dit
    gebeurt als de 'username' en 'password' niet goed zijn. Als user
    None is dan zal de 'warning' variabele aangepast worden met een
    waarschuwingstekst. Als user niet None is dan zal de gebruiker
    ingelogd worden en doorgestuurd worden naar de overzichtspagina.
    Hierbij zal eerst, aan de hand van de gebruikersnaam, de juiste
    dataset worden opgehaald.

    :param request: De input die vanuit de html pagina komt.
    :return request: Gerenderde weergave.
    :return start.html: De html pagina waarnaar de functie refereert.
    :return warning: Een string met een waarschuwingstekst.
    """
    warning = ""
    if request.method == 'POST':
        if request.POST.get('aanmelden'):
            global user
            username = request.POST.get('username')
            password = request.POST.get('password')
            user = authenticate(request, username=username, password=password)

            if user is None:
                warning = "• Geen geldig wachtwoord en/of gebruikersnaam"
            else:
                login(request, user)
                get_data(user)
                return HttpResponseRedirect('/overzicht/')
    return render(request, 'start.html', {'warning': warning, })


def get_data(user):
    """
	De methode roept als eerst de globale variabelen 'df', 'queryset'
	en setting aan om deze later aan te passen. Hierna zal aan de hand
	van de gebruikersnaam de corresponderende dataset worden
	opgehaald. Vervolgens worden er headers toegekend aan de dataset.
	Een connectie wordt gemaakt met de database. Indien deze al de
	tabel onescnd_restaurantdata bevat zal deze verwijderd worden.
	Wanneer dit niet het geval is zal deze tabel aangemaakt worden.
	De zojuist opgehaalde dataset wordt hierbij toegevoegd aan de
	tabel, waarna de connectie met de database wordt beeindigd.
	De 'queryset' haalt vervolgens deze gegevens op uit de database,
	waarna deze als dataframe wordt opgeslagen in de variabele 'df'.
	De globale variabele 'setting' wordt als laatst gelijkgezet op
	'dag'.	  

    :param user: Gebruikersnaam.
    """
    global df
    global queryset
    global setting
    complete = pd.read_csv(os.path.join(os.path.join("./static/files/", "tbl_"
                                                     +str(user)+'.csv')))
    complete.columns =["tafel_nr", "wijk", "begin_tijd", "volg_nr",
                       "eind_tijd", "servicetype"]
    # connectie maken:
    path = ""
    cxn = sqlite3.connect(path + 'db.sqlite3')
    # als de tabel al bestaat, verwijder hem dan:
    cxn.cursor().executescript('drop table if exists onescnd_restaurantdata')
    # maak de tabel 'genomes' aan en vul hem met de dataframe
    complete.to_sql('onescnd_restaurantdata', cxn, index=False)
    cxn.close()
    queryset = RestaurantData.objects.values_list(
        "tafel_nr", "wijk", "begin_tijd", "volg_nr", "eind_tijd",
        "servicetype")
    df = pd.DataFrame(list(queryset), columns=[
        "tafel_nr", "wijk", "begin_tijd", "volg_nr", "eind_tijd",
        "servicetype"])
    setting = "dag"


def log_out(request):
    """
    Deze methode zorgt ervoor dat de gebruiker wordt uitgelogd. Dit
    gebeurd door de globale variabele 'user' op te vragen. Hierin
    staat het account van de gebruiker.

    Het account zal vervolgens uitgelogt worden, waarbij de user wordt
    overschreven naar een lege string. Het overschrijven van de user
    vindt plaats zodat deze later opnieuw gevuld kan worden met een
    andere gebruiker.

    :param request: De input van de gebruiker.
    """
    global user
    logout(request)
    user = ''
    cache.clear()


def titel_selectie(datum):
    """
    Deze methode selecteerd het juiste datum type van de meegegeven
    datum. De methode importeert eerst de globale variabele die de
    gewenste datum setting mee heeft, waarna gelijk een if-statement
    wordt aangeroepen.
    - Indien de globale variabele setting gelijk staat aan 'dag' wordt
    een dictionary aangemaakt waarin de keys de dagnamen in het
    engels zijn, en de values de respective dagnamen in het
    nederlands zijn. Na de dictionary wordt de datum opgesplitst
    en omgezet naar een datetime zodat de dag geconstateerd kan worden.
    Deze dag is in eerste instantie in het engels en de uitkomst
    wordt als key gebruikt om de overeenkomstige nederlandse dagnaam
    terug te krijgen.
    - Indien de globale variabele setting gelijk staat aan 'week' wordt
    het nummer van de week bepaald door middel van de meegegeven datum.
    - Indien de globale variabele setting gelijk staat aan 'maand' wordt
    een dictionary aangemaakt waarin de keys de maandnamen in het
    engels zijn, en de values de overeenkomstige maandnamen in het
    nederlands zijn. Na het aanmaken van de dictionary wordt de maand
    bepaald aan de hand van de datum die is meegegeven. Deze maand
    is in eerste instantie in het engels, maar wordt omgezet naar het
    nederlands door de verkregen maand als key te gebruiken in de
    dictionary.
    - Indien de gelobale variabele setting gelijk staat aan 'jaar' wordt
    het jaar van de gekozen datum bepaald door middel van de meegegeven
    datum.

    :param datum: De gekozen datum.
    :return titel: Een string met de dag/week/maand/jaar van de gekozen
    datum.
    """
    global setting
    if setting == 'dag':
        dagen = {'Monday': 'Maandag', 'Tuesday': 'Dinsdag',
                 'Wednesday': 'Woensdag', 'Thursday': 'Donderdag',
                 'Friday': 'Vrijdag', 'Saturday': 'Zaterdag',
                 'Sunday': 'Zondag'}
        datum = datum.split()
        datum = datetime.datetime.strptime(datum[0], '%Y-%m-%d')
        titel = 'Dag: '+dagen.get(datum.strftime("%A"))
    elif setting == 'week':
        titel = 'Week: '+datum.strftime("%V")
    elif setting == 'maand':
        maanden = {'January': 'Januari', 'February': 'Februari',
                   'March': 'Maart', 'April': 'April', 'May': 'Mei',
                   'June': 'Juni', 'July': 'Juli', 'August': 'Augustus',
                   'September': 'September', 'October': 'Oktober',
                   'November': 'November', 'December': 'December'}
        titel = 'Maand: '+maanden.get(datum.strftime("%B"))
    else:
        titel = 'Jaar: '+str(datum.year)

    return titel


def verschilToevoegen(data):
    """
    Deze methode voegt met behulp van pandas een kolom toe aan het
    dataframe die is geimporteerd uit de database. Deze extra kolom is
    het verschil in tijd tussen begin_tijd en eind_tijd.

    Eerst worden eind_tijd en begin_tijd omgezet naar een datetime
    format zodat ermee gerekend kan worden. Daarna wordt de kolom
    toegevoegd met het verschil tussen die twee kolommen.

    :param data: De geimporteerde dataframe van de database.
    :return data: De gemodificeerde dataframe.
       """
    data['eind_tijd'] = pd.to_datetime(data['eind_tijd'],
                                       errors='coerce')
    data['begin_tijd'] = pd.to_datetime(data['begin_tijd'],
                                        errors='coerce')
    data['verschil'] = (data["eind_tijd"] - data["begin_tijd"])

    return data


def filterOutlier(filter_data):
    """
    Deze methode pakt de gemaakte kolom 'verschil' en zet diens data
    om naar minuten. Dit gebeurt omdat alle datapunten met een
    verschil boven de 30 minuten eruit gefiltreerd moeten worden.
    Hierna worden alle datapunten onder de 30 minuten genomen, waarmee
    het dataframe wordt overschreven.

    Als laatst zal een controle plaatsvinden of er data aanwezig is.
    - Indien de index in de data 0 is betekent dit dat het dataframe
    leeg is. De variabele 'data_none' wordt naar een lege string gezet.
    - Indien de index van de data niet 0 is, is het dataframe niet
    leeg. De variabele 'data_none' zal nu op 0 worden gezet.

    :param filter_data: De data die gefilterd is op datum.
    :return filter_data: De gefilterde data.
    :return data_none: Een variabele die doorgeeft of de dataframe
                        leeg of vol is.
    """
    filter_data["verschil"] = filter_data["verschil"].dt.total_seconds() / 60
    filter_data = filter_data.loc[filter_data["verschil"] < 30]

    if len(filter_data.index) == 0:
        data_none = ""
    else:
        data_none = "0"
    return filter_data, data_none


def overzicht(request):
    """
      Deze methode stuurt alles in het overzichtscherm aan. Als eerste
      worden de benodigde globele variabelen geimporteerd.
          - Setting is de huidige setting de gebruiker in zit.
          - day is de datum waar de gebruiker in zit.
          - user is het account van de gebruiker.

      De methode begint daarna gelijk met een if-statement.
      - Indien een knop in de  POST form wordt gebruikt, dan worden de
        volgende if-statements aangeroepen.
          - Indien op de knop vergelijken wordt geklikt,dan wordt de
            gebruiker verwezen naar het vergelijkingscherm.
          - Indien op de knop uitloggen wordt geklikt, dan wordt de
            methode 'log_out' waarbij de gebruiker uitgelogd  wordt.
            Hierna wordt de gebruiker verwezen naar het loginscherm.
          - Indien er direct een datum wordt gekozen in het kalender
            scherm, of de pijltjes knop '<<', of de pijltjes knop '>>'
            wordt gebruikt, dan wordt de methode datumSelect aangeroepen
            in het python bestand datum_verwerken.py. Deze methode zal
            een nieuwe datum terug geven. Deze datum kan zijn:
              - De geselecteerde datum van de gebruiker vanuit het
                kalender scherm.
              - De datum die een dag/week/maand/jaar voor de hudige datum
                zit.
              - De datum die een dag/week/maand/jaar na de huidige datum
                zit.

          Na deze if-statements wordt de functie userInput aangeroepen,
          in het bestand datum_verwerken.py. Deze functie checkt of een
          van de andere knoppen (dag, week, maand, jaar) gebruikt
          worden. Deze methode zal de start datum van de gekozen optie
          terug geven, de dataframe die gefiltreerd is op deze optie, de
          nieuwe setting en de nieuwe datum mocht die er zijn.

          Hierna volgt er een nieuwe if-statement.
          - Indien er op de pijltjes knop '<<' wordt gedrukt, dan wordt
            de methode 'vorigeOfvolgend' aangeroepen in het bestand
            'datum_verwerken.py'. Deze functie zal alle data in het
            scherm aanpassen naar de data van de vorige dag/week/maand/
            jaar.
          - Indien er op de pijltjes knop '>>' wordt gedrukt, dan wordt
            de methode 'vorigeOfvolgend' aangeroepen in het bestand
            'datum_verwerken.py'. Deze functie zal alle data in het
            scherm aanpassen naar de data van de volgende dag/week/
            maand/jaar.
      - Als geen van deze opties zijn aangeklikt in de form, dan wordt
        de methode 'datumFilter' aangeroepen in het bestand
        'datum_verwerken.py'. Deze functie zal alle data in het
        overzichscherm omzetten naar de huidige dag.

      Na deze if-statements wordt de benodigde data nog aangepast, en
      worden de figuren gemaakt die in het overzichtscherm te zien zijn.

      Als eerste wordt de methode 'verschilToevoegen' aangeroepen, die
      een extra kolom toevoegd met de naam 'verschil'. Hierna wordt de
      data gefiltreerd op alle datapunten met een veschil kleiner dan 30
      minuten met de methode 'filterOutlier'.

      Hiernaast moet er een tabel gemaakt worden die alle instanties
      weergeeft met een verschil tussen de 15 en 30 minuten. De methode
      uitschieterlijst maakt een tabel met alle tafels die een verschil
      tussen de 15 en 30 minuten hebben.

      Hierna worden de plots gemaakt. Als eerste wordt de heatmap
      gemaakt. De methode 'heatmap_input', uit het bestand
      'heatmap_maken.py', neemt de gefiltreerde  data aan en zal die
      eerste modificeren zodat de heatmap ermee kan werken. Hierna wordt
      in de methode de heatmap aangemaakt, en geeft dan een lijst terug
      met variabelen die nodig zijn voor het overzichtscherm. Vervolgens
      wordt een dictionary gemaakt die met een for-loop gevult wordt met
      de variabele in de lijst die meegeven is door de 'heatmap_input'
      methode.

      Hier na wordt de lijngrafiek aangemaakt. Als eerste wordt de data
      gemodificeerd zodat de juiste data gepakt kan worden die nodig is
      voor de huidige setting. De methode 'lijngrafiek_data', uit het
      bestand 'lijngrafiek_maken.py', haalt alle overbodige kolommen
      weg, en zet de dataframe om in de data die nodig is voor de
      lijngrafiek. Hierna wordt de methode 'lijngrafiek_input', uit
      hetzelfde bestand, aangeroepen die de juiste x-as en y-as input
      zoekt bij de variabelen die  meegegeven zijn door de methode
      'lijngrafiek_data'. Als laatste wordt de methode
      'bokeh_lijngrafiek', uit wederom het bestand
      'lijngrafiek_maken.py', aangeroepen. Deze methode zal de
      lijngrafiek figuur aanmaken en de juist data erin stoppen. Het zal
      de grafiek de code voor de interactive eigenschappen terug geven
      als variabelen.

      Als laatste wordt de methode 'titel_selectie' aangeroepen. Deze
      methode zorgt ervoor dat elk scherm de juiste dag/week/maand/jaar
      aangeeft bij de gekozen setting van de gebruiker.

      :param request: De input van de gebruiker.
      :return data_none: Indicator die aangeeft of het dataframe
                         leeg is ofniet
      :return user: Het geverifieerde account van de gebruiker.
      :return datum: De dag/week/maand/jaar van het huidige scherm.
      :return vandaag: De begin datum van de dag/week/maand/jaar.
      :return warning: De tabel met alle tafels met een verschil
                        tussen 15 en 30 minuten.
      :return lg_div: Het figuur van de lijngrafiek.
      :return lg_script: De functionaliteiten van de lijngrafiek.
      :return bokeh_hm: Een dictionary met alle figuren en
                        functionaliteiten van de heatmap.
      """
    global setting
    global day
    global user
    global df

    if request.method == 'POST':
        if request.POST.get('vergelijken'):
            return HttpResponseRedirect('/vergelijk/')
        if request.POST.get('logout'):
            log_out(request)
            return HttpResponseRedirect('/')
        if request.POST.get('datumselect') or request.POST.get('previous') or \
                request.POST.get('next'):
            day = d.datumSelect(request.POST)
        start, data, setting, day = d.userInput(request.POST, day, setting,
                                                df, day)
        if request.POST.get('previous'):
            start, data = d.vorigeOfvolgend(0, day, df, setting)
        if request.POST.get('next'):
            start, data = d.vorigeOfvolgend(1, day, df, setting)
    else:
        start, data = d.datumFilter(day, setting, df)

    m_df = verschilToevoegen(data)
    filter_data, data_none = filterOutlier(m_df)
    warning = uitschieterlijst(filter_data, ["tafel_nr"], "o")

    # Maken van de heatmap.
    bokeh_heatmaps = h.heatmap_input(filter_data)
    heatmap_dict = {i: bokeh_heatmaps[i] for i in range(
        0, len(bokeh_heatmaps))}

    # Maken van de lijngrafiek.
    gv_df, tijdLijst, setting = l.lijngrafiek_data(filter_data,
                                                   setting)
    x_as, gemiddelde, xlabel = l.lijngrafiek_input(gv_df,
                                                   tijdLijst, setting)
    lg_script, lg_div = l.bokeh_lijngrafiek(x_as, gemiddelde, xlabel)

    # Toevoegen van de juiste titel bij de datum.
    datum = titel_selectie(start)

    return render(request, 'overzicht.html', {
        'data_none': data_none,
        'user': user,
        'datum': datum,
        'vandaag': str(start)[0:10],
        'warning': warning,
        'lg_div': lg_div,
        'lg_script': lg_script,
        'bokeh_hm': heatmap_dict,
    })


def detail(request):
    """
    Allereerst worden de globale variabelen 'setting', 'day' en
    'user' gedeclareerd. Hier fungeert setting als indicator voor de
    dag/week/maand/jaar weergave, day als startdatum, en user als
    authenticatie om de pagina te mogen bezoeken.

    Vervolgens wordt gekeken of er op een knop is gedrukt in de pagina.
    - Indien er niet op een knop is gedrukt zal de functie datumFilter
    worden aangeroepen. Hierbij worden day, setting en df meegegeven.
    De functie zal de begindatum en dataframe teruggeven.
    - Als er wel op een knop gedrukt is zal gekeken worden welke knop
    gebruikt is.
        - Indien de uitvoer-knop, de volgende- of vorige-knop of de
        overzicht-knop gebruikt is wordt de variabele day(string)
        aangemaakt door de functie datumSelect aan te roepen. Hierbij
        wordt request.POST als variabele meegegeven.
        - Indien de overzicht-knop gebruikt is wordt het
        overzichtsscherm aangeroepen.
        - Indien de uitloggen-knop gebruikt is wordt de functie log_uit
        aangeroepen, waarna de gebruiker teruggaat naar het startscherm.
        - Indien de vorige-knop gebruikt is wordt de vorigeOfVolgende
        methode aangeroepen. Hierbij wordt een indicator, day, df en
        setting meegegeven. De indicator dient om een onderscheidt te
        maken tussen het op- of aftrekken van een dag/week/maand/jaar.
        De methode zal de begindatum en dataframe teruggeven.
        - Indien de volgende-knop gebruikt is wordt eveneens de
        vorigeOfVolgende methode aangeroepen. Ten opzichte van de
        vorige-knop wordt alleen de indicator.

    De functie verschilToevoegen wordt aangeroepen om de wachttijden te
    berekenen. Hierbij wordt het dataframe meegegeven en wordt met een
    extra kolom teruggegeven. Deze dataframe wordt meegegeven aan
    data_tafel om het dataframe te filteren op het gewenste tafelnummer.
    Aan de hand van deze tafeldata wordt door de uitschierlijst
    methode een tabel gemaakt met alle wachttijden boven de 15 minuten.
    Vervolgens worden alle wachttijden boven de 30 minuten verwijderd
    uit het dataframe door filterOutlier. Deze methode geeft naast het
    dataframe een indicator mee die aangeeft of het dataframe data
    bevat.

    Hierna wordt de lijngrafiek aangemaakt. Als eerste wordt de data
    gemodificeerd zodat de juiste data gepakt kan worden die nodig is
    voor de huidige setting. De methode 'lijngrafiek_data', uit het
    bestand 'lijngrafiek_maken.py', haalt alle overbodige kolommen weg
    en zet het dataframe om in de data die nodig is voor de lijngrafiek.
    De methode 'lijngrafiek_input', uit hetzelfde bestand, wordt daarna
    aangeroepen om de juiste x-as en y-as input te zoeken bij de
    variabelen die meegegeven zijn door de methode 'lijngrafiek_data'.
    Als laatste wordt de methode 'bokeh_lijngrafiek', eveneens uit
    het bestand 'lijngrafiek_maken.py', aangeroepen. Deze methode zal
    het figuur aanmaken en de juist data erin plotten. Deze methode zal
    de grafiek en de code voor de interactive eigenschappen terug geven
    als variabelen.

    De functie click_en_wachttijd_data wordt vervolgens aangeroepen om
    data op te vragen voor het detailscherm. Hierbij wordt het dataframe
    met de tafel kliks onder de 30 minuten meegegeven. Uit deze functie
    komen de variabelen:
        t_click: Integer van het totaal aantal clicks,
        b_click: Integer van het aantal clicks voor betalen,
        e_click: Integer van het aantal clicks voor eten,
        d_click: Integer van het aantal clicks voor drinken,
        gemiddelde_w: Afgeronde float van de gemiddelde wachttijd,
        minimale_w: Afgeronde float van de minimale wachttijd,
        maximale_w: Afgeronde float van de maximale wachttijd.

    Vervolgens wordt de variabele datum(string) aangemaakt door de
    functie titel_selectie aan te roepen. Hierbij wordt de variabele
    start(string) meegegeven.

    Als laatst worden de heatmap gemaakt. Hiervoor wordt de methode
    bokeh_barplot aangeroepen, waarbij het dataframe met de tafel kliks
    onder de 30 minuten meegegeven wordt. De methode keert een lijst
    van div en script componenten terug.

    :param request: De input van de gebruiker.
    :return: 'tafel_nr': Het geselecteerde tafelnummer(str).
    :return: 'datum': De geslecteerde datum(str).
    :return: 'lg_script': Script component van de grafiek,
    :return: 'lg_div': Div component van de grafiek,
    :return: 't_click': Totaal aantal kliks van alle servicetypen(int).
    :return: 'b_click': Aantal kliks van servicetype betalen(int).
    :return: 'e_click': Aantal kliks van servicetype eten(int).
    :return: 'd_click': Aantal kliks van servicetype drinken(int).
    :return: 'min_wacht': Minimale wachttijd(float),
    :return: 'max_wacht': Maximale wachttijd(float),
    :return: 'g_wacht': Gemiddelde wachttijd(float),
    :return: 'selection': Lijst met wachttijden van 15+ min (dataframe),
    :return: 'vandaag': De geselecteerde dag(str),
    :return: "tijd': De geselecteerde weergave(str),
    :return: 'geen_data': "nee"(string),
    """
    global setting
    global day
    global user
    if request.method == 'POST':
        if request.POST.get('datumselect') or request.POST.get('previous') or \
                request.POST.get('next') or request.POST.get('overzicht'):
            day = d.datumSelect(request.POST)
        start, data, setting, day = d.userInput(request.POST, day, setting,
                                                df, day)
        if request.POST.get('overzicht'):
            return HttpResponseRedirect('/overzicht/')
        if request.POST.get('logout'):
            log_out(request)
            return HttpResponseRedirect('/')
        if request.POST.get('previous'):
            start, data = d.vorigeOfvolgend(0, day, df, setting)
        if request.POST.get('next'):
            start, data = d.vorigeOfvolgend(1, day, df, setting)
    else:
        start, data = d.datumFilter(day, setting, df)

    data = verschilToevoegen(data)
    tafeldata, tafel_nr = data_tafel(data, request)
    uitschieter_lijst = uitschieterlijst(tafeldata, ["begin_tijd",
                                                     "verschil",
                                                     "servicetype"], "d")
    tafel_clicks_onder30, data_none = filterOutlier(tafeldata)

    # Lijngrafiek.
    gv_df, tijdLijst, setting = l.lijngrafiek_data(tafel_clicks_onder30,
                                                   setting)
    x_as, gemiddelde, xlabel = l.lijngrafiek_input(gv_df,
                                                   tijdLijst, setting)
    lg_script, lg_div = l.bokeh_lijngrafiek(x_as, gemiddelde, xlabel)

    # Ruwe data
    t_click, b_click, e_click, d_click, gemiddelde_w, \
        minimale_w, maximale_w = click_en_wachttijd_data(tafel_clicks_onder30)

    datum = titel_selectie(start)

    bokeh_barplots = b.bokeh_barplot(tafel_clicks_onder30)
    barplot_dict = {i: bokeh_barplots[i] for i in range(
        0, len(bokeh_barplots))}

    return render(request, 'detail.html', {
        'data_none': data_none,
        'user': user,
        'tafel_nr': tafel_nr,
        'datum': datum,
        'lg_script': lg_script,
        'lg_div': lg_div,
        't_click': t_click,
        'b_click': b_click,
        'e_click': e_click,
        'd_click': d_click,
        'min_wacht': minimale_w,
        "max_wacht": maximale_w,
        "g_wacht": gemiddelde_w,
        "selection": uitschieter_lijst,
        'vandaag': str(start)[0:10],
        "tijd": setting,
        "geen_data": "nee",
        'bokeh_bp': barplot_dict,
    })


def click_en_wachttijd_data(data):
    """
    De methode begint met het aanmaken van de variabele t-click. Deze
    integer bevat de lengte van het meegekregen dataframe.
    Deze dataframe zal eveneens gebruikt worden om te filteren op en het
    aantal keer dat servicetypen eten, drinken en betalen voorkomt te
    bepalen. Deze uitkomst wordt opgeslagen in respectievelijk e_click,
    d_click en b_click.

    Ook wordt het dataframe gebruikt om de gemiddelde, minimale en
    maximale wachttijd te berekenen. Indien er geen data in het
    dataframe aanwezig is om een van deze wachttijden te bepalen zal
    een 0-waarde worden toegekend. De wachttijden worden opgeslagen in
    respectievelijk gemiddelde_w, minimale_w en maximale_w.

    :param data: Een pandas dataframe met de restaurant data.
    :return t_click: Integer van het totaal aantal clicks.
    :return b_click: Integer van het aantal clicks voor betalen.
    :return e_click: Integer van het aantal clicks voor eten.
    :return d_click: Integer van het aantal clicks voor drinken.
    :return gemiddelde_w: Afgeronde float van de gemiddelde wachttijd.
    :return minimale_w: Afgeronde float van de minimale wachttijd.
    :return maximale_w: afgeronde float van de maximale wachttijd.

    """
    t_click = len(data)
    b_click = len(data.loc[data['servicetype'] == 'betalen'])
    e_click = len(data.loc[data['servicetype'] == 'eten'])
    d_click = len(data.loc[data['servicetype'] == 'drinken'])
    try:
        gemiddelde_w = round(data["verschil"].mean(), 1)
        minimale_w = round(data["verschil"].min(), 1)
        maximale_w = round(data["verschil"].max(), 1)
    except:
        gemiddelde_w = 0
        minimale_w = 0
        maximale_w = 0
    return t_click, b_click, e_click, d_click, gemiddelde_w, \
        minimale_w, maximale_w


def data_tafel(data_v, request):
    """
    Deze functie begint met het ophalen van de huidige url. Aan de hand
    van een referentie worden de laatste cijfers opgevraagt, die gelijk
    staat aan een tafelnummer. Het dataframe wordt vervolgens op dit
    tafelnummer gefilterd. Waarna het dataframe en het tafelnummer
    worden teruggegeven.

    :param data_v: Een pandas dataframe met de restaurant data.
    :param request: object van de detail functie.
    :return: data: Een pandas dataframe met gefilterde restaurant
                   data op tafelnummer.
    """
    detail_url = request.build_absolute_uri()
    tafel_nr = detail_url.split("tafel_nr=")[1]
    data = data_v.loc[data_v['tafel_nr'] == int(tafel_nr)]
    return data, tafel_nr


def uitschieterlijst(data, headers, scherm):
    """
    De functie maakt een dataframe aan genaamd 'letop' die alle
    wachttijden die hoger zijn dan 15 minuten bevat. Door middel van
    een meegegeven header worden hieruit de kolommen "begin_tijd,
    "verschil" en "servicetype" genomen.
    Deze header wordt vervolgens aangepast naar respectievelijk
    "Tijd/datum","Wachttijd" en "Service type".

    Een controle wordt uitgevoerd op de meegegeven variabele 'scherm'.
    Indien deze op 'o' staat (voor overzichtscherm) wordt het dataframe
    gesorteerd op tafelnummer en alle duplicaten verwijderd.

    Indien de dataframe geen data bevat zal de variabele 'selection'
    geen waarde hebben. Bij elk ander geval wordt deze gelijk gezet
    aan de variabele 'letop', waarbij deze wordt geconverteerd naar
    html format.

    :param data: Een pandas dataframe die gefilterd is op tafel nummer.
    :param headers: De kolomnamen nodig voor deze functie.
    :param scherm: Geeft aan of de functie aangeroepen wordt vanuit het
                   overzichtscherm of detailscherm.
    :return: Selection: Een naar html geconverteerde pandas dataframe.

    """
    letop = data[(((data["eind_tijd"] - data[
        "begin_tijd"]).dt.total_seconds() / 60) > 15)]
    letop = letop[headers]
    letop = letop.rename(columns={"begin_tijd": "Tijd/datum",
                                  "verschil": "Wachttijd",
                                  "servicetype": "Service type"})

    if scherm == "o":
        letop = letop.sort_values(by=[
            'tafel_nr']).drop_duplicates(subset="tafel_nr")

    if len(letop.index) == 0:
        selection = ""
    else:
        selection = letop.to_html(index=False)

    return selection


def vergelijk(request):
    """
    De methode start met het ophalen van het pandas dataframe. Deze
    wordt gebruikt om een lijst met unieke tafels en een lijst met
    unieke wijken te creeren. Deze lijsten worden doorgegeven aan
    het template vergelijk.html

    Een connectie wordt gelegd met het forumulier uit het
    vergelijkings scherm. Wanneer op de [Vergelijk] knop
    wordt geklikt worden de volgende gegevens opgehaald:
        van_datum_1: Datum 1 / 'Van' datum van Periode 1
        tot_datum_1: Datum 2 / 'Tot' datum van Periode 1
        van_datum_2: 'Van' datum van Periode 2
        tot_datum_2: 'Tot' datum van Periode 2
        tafels: Een lijst met alle geselecteerde tafels
        wijken: Een lijst met alle geselecteerde wijken
     Na het ophalen van deze gegevens worden ze meegegeven als
     variabelen in een redirect naar het overzichtsscherm.

    :param request: request object
    :return vergelijk.html: Gerenderde weergave van vergelijk.html.
    :return 'tafels': Lijst met alle unieke tafels.
    :return 'wijken': Lijst met alle unieke wijken.
    """
    global df
    global user
    tafels = sorted(df['tafel_nr'].unique())
    wijken = sorted(df['wijk'].unique())

    if request.method == 'POST':
        if request.POST.get('vergelijk_data'):
            van_datum_1 = request.POST.get('van_datum_1')
            tot_datum_1 = request.POST.get('tot_datum_1')
            van_datum_2 = request.POST.get('van_datum_2')
            tot_datum_2 = request.POST.get('tot_datum_2')
            tafels = request.POST.getlist("tafels")
            wijken = request.POST.getlist("wijken")
            return HttpResponseRedirect('/overzicht/')
        if request.POST.get('logout'):
            log_out(request)
            return HttpResponseRedirect('/')

    return render(request, 'vergelijk.html', {
        'user': user,
        'tafels': tafels,
        'wijken': wijken,
    })
