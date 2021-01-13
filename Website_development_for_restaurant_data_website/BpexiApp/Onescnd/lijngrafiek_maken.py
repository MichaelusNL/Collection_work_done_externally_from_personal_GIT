from bokeh.embed import components
from bokeh.models import SingleIntervalTicker, LinearAxis
from bokeh.plotting import figure
import pandas as pd


class Lijngrafiek_maken:
    def lijngrafiek_data(self, f_data, setting):
        """
        Deze methode maakt de voorbereidingen voor de data manipulatie
        die nodig is voor de lijngrafiek. Als eerste worden de headers
        van de kolomen gepakt die essentieel zijn voor de lijngrafiek.
        Daarna wordt een dictionary aangemaakt waarin de keys de
        verschillende mogelijkheden van de parameter 'setting' zijn,
        en de values de resample waardes die bij de respective setting
        horen.

        Als eerste wordt het datatype van de kolom 'begin_tijd' in de
        dataframe van variabele 'f_data' omgezet in datetime zodat er
        later mee gewerkt kan worden. Hierna wordt de kolom 'begin_tijd'
        ingesteld als index van de nieuwe dataframe, waarna de dataframe
        wordt geknipt op de kolommen aangegeven in de variabele
        'headers'.

        Hierna wordt het gemiddelde berekend met behulp van de resample
        functie. Dit gemiddelde wordt berekend op basis van de setting
        die de gebruiker heeft gekozen. Als laatste wordt een lijst
        aangemaakt waar de index van deze nieuwe dataframe wordt in
        gezet.

        De nieuwe dataframe en de lijst van indexen worden meegegeven
        aan de lijngrafiek_input functie.

        :param f_data: De op datum gefiltreerde dataframe.
        :param setting: De huidige gekozen setting
        :return x_as: De getallen die weergegeven worden op de x_as.
        :return gemiddelde: Een lijst van gemiddelde per tijdseenheid.
        :return xlabel: Een string met de correcte label voor de x-as.
        """
        headers = ["begin_tijd", "verschil"]
        resample_vars = {'dag': 'H', 'week': 'D', 'maand': '7D',
                         'jaar': 'BM'}
        f_data['begin_tijd'] = pd.to_datetime(f_data['begin_tijd'])
        f_data.index = f_data['begin_tijd']
        f_data = f_data[headers]
        gv_df = f_data.resample(
            resample_vars.get(setting)).mean().fillna(0)
        tijdLijst = gv_df.index.copy()

        return gv_df, tijdLijst, setting

    def lijngrafiek_input(self, gv_df, tijdlijst, setting):
        """
        Deze methode pakt de juiste x- en y-as variabele in het juiste
        format. Hiervoor worden 3 dictionaries aangemaakt.
        * De 'x_as' dictionary bevat de mogelijke opties voor de
          variabele 'setting' als keys, en als values de default
          waarden voor de x-as voor de respective setting.
        * De 'nullijsten' dictionary dictionary bevat de mogelijke
          opties voor de variabele 'setting' als keys. De values zijn
          lijsten met 0 waardes erin die vervangen kunnen worden met
          juiste data.
        * De 'x_labels' dictionary bevat de mogelijke opties voor de
          variabele 'setting' als keys. De values zijn strings van
          de juiste x-as label bij de respective setting optie.

        Als eerste wordt er een lege lijst aangemaakt die gebruikt wordt
        om de data op de juiste plek in de lijngrafiek te stoppen.
        Hierna volgt een for loop met daarin een if-statement.
        - Indien de setting gelijk staat aan 'maand', dan wordt de
          value van de key 'maand' in x_as dictionary direct gevuld
          met de juiste week nummers. De positie lijst word dan gevult
          met een simpele counter.
        - Indien de setting niet gelijk staat aan 'maand', dan wordt
          er eerst een dictionary aangemaakt die de juiste dag/periode
          pakt op basis van de gekozen setting. Hierna wordt de lijst
          'positie' gevult met de tijdseenheden die horen bij de
          gekozen setting.

        Hierna worden de gemiddelde van de dataframe gv_df in een lijst
        gezet. De value van de key die overeen komt met de setting
        van de x_as dictionary worden ook in een variabele gezet.
        Hetzelfde wordt gedaan voor de x_labels dictionary.

        Als laatste wordt er gecheckt of de maand setting niet precies 1
        datapunt heeft met een if-statement.
        -   Indien dit wel het geval is wordt er in de 'x_tijd' lijst
            een extra datapunt gezet met de week die daarna komt. Ook de
            'gemiddelde' lijst wordt een extra datapunt neergezet met
            de waarde 0 zodat de bokeh plot het nog wel plot.

        :param gv_df: De dataframe met gemiddelde per tijdseeneheid.
        :param tijdlijst: Een lijst met de begin tijden van de gekozen
                          periode.
        :param setting: De hudige gekozen setting.
        :return x_as: De getallen die weergegeven worden op de x_as.
        :return gemiddelde: Een lijst van gemiddelde per tijdseenheid.
        :return xlabel: Een string met de correcte label voor de x-as.
        """
        x_as = {'dag': list(range(1, 25)), 'week': list(range(1, 8)),
                'maand': list(range(1, len(tijdlijst) + 1)),
                'jaar': list(range(1, 13))}
        nullijsten = {'dag': [0] * 24, 'week': [0] * 7,
                      'maand': [0] * len(tijdlijst), 'jaar': [0] * 12}
        x_labels = {'dag': 'Uren', 'week': 'Dagen', 'maand': 'Weken',
                    'jaar': 'Maanden'}
        positie = []
        for x in range(len(tijdlijst)):
            if setting == 'maand':
                x_as.get('maand')[x] = tijdlijst[x].week
                positie.append(x + 1)
            else:
                selectie = {'dag': tijdlijst[x].hour,
                            'week': tijdlijst[x].weekday(),
                            'maand': tijdlijst[x].week,
                            'jaar': tijdlijst[x].month}
                positie.append(selectie.get(setting))
        gem = gv_df["verschil"].tolist()
        x_tijd = x_as.get(setting)
        gemiddelde = nullijsten.get(setting)
        xlabel = x_labels.get(setting)
        for y in range(len(gem)):
            gemiddelde[positie[y] - 1] = gem[y]

        if setting == 'maand' and len(x_tijd) == 1:
            x_tijd.append(x_tijd[0]+1)
            gemiddelde.append(0)
        return x_tijd, gemiddelde, xlabel

    def bokeh_lijngrafiek(self, x_tijd, gemiddelde, xlabel):
        """
        Deze methode maakt de lijngrafiek aan voor de gemiddelde
        wachttijd. Als eerste wordt het figuur zelf aangemaakt, en
        daarna de lijn erin geplot. Aangezien het beter was om een
        andere x-as te gebruiken wordt de default x-as uit gezet. Daarna
        wordt er een nieuwe x-as aangemaakt met intervalen van 1 en
        geen tussen intervallen. De x-as krijgt de juiste aantal x
        intervallen mee met de x_tijd variabele. Als laatste worden wat
        basis functionaliteiten van een bokeh plot uit gezet. Hier wordt
        de toolbar en de het logo links van de plot uit gezet,
        en de mogelijkheid de plot te slepen. Als dat klaar is
        wordt de aangepaste x-as op de juiste plek in het figuur gezet
        en dan wordt het juiste label bij de x-as gezet met xlabel.

        :param x_tijd: data voor x-as.
        :param gemiddelde: data voor y-as.
        :param xlabel: De label voor de x-as.
        :return script: De functionaliteiten van de bokeh plot.
        :return div: Het bokeh figuur.
        """
        plot = figure(title="Gemiddelde Wachttijd: ", plot_width=900,
                      plot_height=400)
        plot.line(x_tijd, gemiddelde, line_width=2)
        plot.xaxis.visible = False
        ticker = SingleIntervalTicker(interval=1, num_minor_ticks=0)
        xaxis = LinearAxis(ticker=ticker)
        plot.xaxis.ticker = x_tijd
        plot.toolbar_location = None
        plot.toolbar.logo = None
        plot.toolbar.active_drag = None
        plot.add_layout(xaxis, 'below')
        plot.xaxis.axis_label = xlabel
        plot.yaxis.axis_label = 'Minuten'
        script, div = components(plot)

        return script, div
