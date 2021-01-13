from datetime import date, timedelta, datetime
from dateutil.relativedelta import relativedelta
import datetime
import calendar


class Datum_verwerking:
    def datumFilter(self, datum, tijd, df):
        """
        Deze methode zorgt ervoor dat de juiste begin en eind datum
        wordt genomen bij de gewenste setting van de gebruiker.

        Deze methode begint met het definieren van de variabele 'data'.
        Deze variabele bevat een pandas dataframe met alle
        restaurantdata. Hierna definieert de functie de variabele 'dt'.
        Dit is een datetime variabele die gelijk staat aan de meegegeven
        datum.

        Vervolgens wordt er gekeken of en op welke knop is gedrukt.
        - Indien de parameter 'tijd' gelijk is aan 'week' en wordt de
          variabele 'start' gelijkgezet aan het begin van een week. De
          variabele 'eind' wordt gelijkgezet aan de startdatum + 6, om
          zo uit te komen op een week tussen de start en eind
          variabelen.
        - Indien de parameter 'tijd' gelijk is aan 'maand' wordt de
          variabele 'start' gelijkgezet aan het begin van de maand
          (op basis van de dt variabele) - dit is de eerste dag van de
          maand. De variabele 'eind' wordt gelijkgesteld aan de dag
          laatste dag van de maand.
        - Indien de parameter 'tijd' gelijk is aan 'jaar' wordt de
          variabele 'start' gelijkgezet aan het begin van het jaar
          (op basis van de dt variabele) - dit is de eerste dag van
          de maand. De variabele 'eind' wordt gelijkgesteld aan de
          laatste dag van het jaar.
        - Als geen van de bovenstaande opties zijn gebruikt wordt de
          variabele 'start' gelijkgezet aan de huidige datum met
          "00:00:00" eraan toegevoegd. De variabele 'eind' wordt
          gelijkgezet aan de huidige datum. Hieraan wordt "23:59:59"
          toegevoegd.

        Vervolgens wordt het Pandas dataframe 'filtered_data'
        aangemaakt. De kolommen 'begin_tijd' en 'eind tijd' van deze
        dataframe worden daarna gefiltreerd op de datums in de
        respectievelijke variabelen 'start' en 'eind'.

        :param datum: String met de huidige datum,
        :param tijd: String met daarin de tijdseenheid in weken,
               maanden, jaren of dagen.
        :param df: Pandas dataframe met de restaurantdata.
        :return start: String met daarin de start datum en tijd,
        :return filtered data: Pandas dataframe gefilterd op datum.
        """
        data = df
        dt = datetime.datetime.strptime(datum, '%Y-%m-%d')
        if tijd == "week":
            start = dt - timedelta(days=dt.weekday())
            eind = start + timedelta(days=6)
        elif tijd == "maand":
            start = dt.replace(day=1)
            eind = dt.replace(day=calendar.monthrange(dt.year, dt.month)[1])
        elif tijd == "jaar":
            start = date(dt.year, 1, 1)
            eind = date(dt.year, 12, 31)
        else:
            start = datum + " 00:00:00"
            eind = datum + " 23:59:59"
        filtered_data = data.loc[(data['begin_tijd'] >= str(start)) &
                                 (data['eind_tijd'] <= str(eind))]
        return start, filtered_data

    def datumNavigatie(self, datum, dict_key, setting, df):
        """
        Deze functie definieert de variabele 'datum'. Deze variabele
        staat gelijk aan de meegegeven datum.

        Er worden vervolgens twee dictionaries aangemaakt; 'dict_vorig'
        en 'dict_volgend'.
        - In de 'dict_vorig' dictionary zijn de keys strings die
          overeenkomen met de mogelijke opties van de parameter
          'setting'. De values zijn de berekeningen die de juiste vorige
          datum uitrekent bij de respectievelijke tijdseenheid.
        * In de 'dict_volgend' dictionary zijn de keys strings die
          overeenkomen met de mogelijke opties van de parameter setting.
          De values zijn de berekeningen die de juiste volgende datum
          uitrekent bij de respectievelijke tijdseenheid.

        Vervolgens wordt aan de hand van de parameter 'dict_key' de
        startdatum en dataset bepaalt.
        - Indien dict_key gelijk is aan '0' worden de variabele 'start'
          en 'filtered data' aangemaakt op basis van de dict_vorig
          formule. Aan de dict worden de 'setting' en 'df'
          variabelen meegegeven.
        - Indien dict_key gelijk is aan '1' worden de variabele 'start'
          en 'filtered data' aangemaakt op basis van de dict_volgend
          formule. Aan de dict worden de 'setting' en 'df'
          variabelen meegegeven.

        :param datum: String met de huidige datum,
        :param dict_key: Een integer van 0 of 1, waar 0 vorige
                         aangeeft en 1 volgende.
        :param setting: String met daarin de tijdseenheid in weken,
                        maanden, jaren of dagen.
        :param df: Pandas dataframe met de restaurantdata.
        :return start: String met daarin de start datum en tijd,
        :return filtered data: Pandas dataframe gefilterd op datum.
        """
        datum = datetime.datetime.strptime(datum, '%Y-%m-%d')
        dict_vorig = {'dag': datum - timedelta(days=1),
                      'week': datum - timedelta(days=7),
                      'maand': datum - relativedelta(months=+1),
                      'jaar': datum - relativedelta(years=+1)}
        dict_volgend = {'dag': datum + timedelta(days=1),
                        'week': datum + timedelta(days=7),
                        'maand': datum - relativedelta(months=-1),
                        'jaar': datum - relativedelta(years=-1)}
        if dict_key == 0:
            start, filtered_data = self.datumFilter(str(
                dict_vorig.get(setting))[0:10], setting, df)
        else:
            start, filtered_data = self.datumFilter(str(
                dict_volgend.get(setting))[0:10], setting, df)
        return start, filtered_data

    def datumSelect(self, input):
        """
        De methode datumSelect wordt gebruikt voor het ophalen en
        weergeven van de juiste datum. Dit kan op 3 manieren plaats-
        vinden:
         1. De Previous/Next knoppen rondom de kalender.
         2. De Uitvoeren knop bij een kalender selectie.
         3. De Live knop in het navigatie menu.
        - Indien de Previous/Next knoppen worden gebruikt wordt de
          huidige dag gezet op de waarde van de kalender.
        - Indien een datum wordt geselecteerd in de kalender en
          door middel van de Uitvoeren knop wordt uitgevoerd wordt
          eveneens. De dag gezet op de waarde van de kalender.
        - Indien de Live knop wordt gebruikt in het navigatie menu
          wordt de huidige datum opgevraagt via datetime.today.

        :param input: Request object
        :return day: String met de geselecteerde dag (YYYY-MM-DD)
        """
        if input.get("datum") is not None:
            day = input.get("datum")
        elif input.get('datumselect'):
            day = input.get('datum')
        else:
            day = datetime.datetime.today().strftime("%Y-%m-%d")
        return day

    def userInput(self, input, datum, setting, df, day):
        """
        Deze methode zorgt ervoor dat de gebruiker kan kiezen in welke
        setting (dag/week/maand/jaar) de data getoond wordt.
        - Indien de gebruiker op de knop met 'dag' erop klikt, dan
          wordt de setting veranderd naar 'dag' en wordt de functie
          datumFilter aangeroepen waarbij de 'datum', 'dag' en de
          dataframe aan meegegeven worden.
        - Indien de gebruiker op de knop met 'week' erop klikt, dan
          wordt de setting veranderd naar 'week' en wordt de functie
          datumFilter aangeroepen waarbij de 'datum', 'week' en de
          dataframe aan meegegeven worden.
        - Indien de gebruiker op de knop met 'maand' erop klikt, dan
          wordt de setting veranderd naar 'maand' en wordt de functie
          datumFilter aangeroepen waarbij de 'datum', 'maand' en de
          dataframe aan meegegeven worden.
        - Indien de gebruiker op de knop met 'jaar' erop klikt, dan
          wordt de setting veranderd naar 'jaar' en wordt de functie
          datumFilter aangeroepen waarbij de 'jaar', 'dag' en de
          dataframe aan meegegeven worden.
        - Indien de gebruiker op de knop met 'live' erop klikt, dan
          wordt de datum gelijk gezet naar de dag van vandaag, en de
          setting naar 'dag' gezet. Hierna wordt de functie
          datumFilter aangeroepen waarbij de 'datum', 'dag' en de
          dataframe aan meegegeven worden. Ook wordt de functie
          datumSelect aangeroepen om de kalender naar de datum van
          vandaag te zetten.
        - Als geen van deze bovenstaande criteria worden voldaan, dan
          wordt de functie datumFilter aangeroepen met de huidige
          datum, setting en dataframe.

        :param input: De gebruikers input.
        :param datum: De huidige datum.
        :param setting: De huidige setting.
        :param df: De dataframe waarmee gewerkt wordt.
        :param day: De dag van vandaag.
        :return start: Begin datum van de gekozen setting.
        :return data: De dataframe gefiltreerd op de gekozen setting.
        :return setting: De gekozen setting.
        :return day: De dag van vandaag.
        """
        if input.get('dag'):
            setting = 'dag'
            start, data = self.datumFilter(datum, "dag", df)
        elif input.get('week'):
            setting = 'week'
            start, data = self.datumFilter(datum, "week", df)
        elif input.get('maand'):
            setting = 'maand'
            start, data = self.datumFilter(datum, "maand", df)
        elif input.get('jaar'):
            setting = 'jaar'
            start, data = self.datumFilter(datum, "jaar", df)
        elif input.get('live'):
            setting = 'dag'
            datum = datetime.datetime.today().strftime("%Y-%m-%d")
            start, data = self.datumFilter(datum, "dag", df)
            day = self.datumSelect(input)
        else:
            start, data = self.datumFilter(datum, setting, df)
        return start, data, setting, day

    def vorigeOfvolgend(self, input, day, df, setting):
        """
        Deze methode refereert naar de datumNavigatie methode, en zorgt
        ervoor dat het overal in het programma aangroepen kan worden.

        :param input: Dit is de gebruikers input.
        :param day: Dit is de huidige datum.
        :param df: Dit is de dataframe waarmee gewerkt wordt.
        :param setting: Dit is de huidige setting.
        :return start: Dit is de start datum van de dag/week/maand/jaar.
        :return data: Dit is de dataframe gefiltreerd op dag/week/maand/
        jaar.
        """
        start, data = self.datumNavigatie(day, input, setting, df)
        return start, data
