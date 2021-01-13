from bokeh.embed import components
from bokeh.models import LinearColorMapper, BasicTicker,\
    PrintfTickFormatter, ColorBar, TapTool, OpenURL
from bokeh.plotting import figure
import pandas as pd
import itertools


class Heatmap_maken:
    def heatmap_input(self, hi_data):
        """
        De methode start met het definieren van de benodigde kolommen
        en het filteren hiervan op het meegegeven dataframe. Aan de
        hand van deze data worden sub-dataframe's aangemaakt voor elk
        van de mogelijke combinaties tussen servicetypen.

        Vervolgens wordt een lijst aangemaakt met elk initiaal van de
        servicetypen. Deze lijst wordt gebruikt om elke mogelijke
        combinatie te maken tussen servicetypen. Deze combinaties
        worden als string opgeslagen en gebruikt om dynamisch een
        script, div en dataset te maken voor elke heatmap.

        Per combinatie tussen servicetypen wordt heatmap_dataframe()
        aangeroepen, waarbij de bijbehorende dataset wordt meegegeven.

        Uit de heatmap_dataframe() functie komen twee variabelen terug;
        script en div. Indien een foutmelding plaatsvindt doordat er
        geen data aanwezig is wordt een leeg plot aangemaakt en
        opgedeeld in een script en div. De variabelen script en div
        worden vervolgens opgeslagen in een lijst.

        Alle div's en script's uit de lijst worden via een starred
        expression teruggegeven aan de overzicht() functie in views.py.

        :param hi_data: Dataframe van de huidige datum selectie.
        :return: *var_list: Lijst met alle script's en div's.
        """
        # Data filteren
        header = ["tafel_nr", "wijk", "begin_tijd",  "volg_nr",
                  "eind_tijd", "servicetype"]
        data = hi_data.fillna(0)[header]

        # Dataframe's aanmaken
        hi_e = data.loc[data['servicetype'] == 'eten']
        hi_d = data.loc[data['servicetype'] == 'drinken']
        hi_b = data.loc[data['servicetype'] == 'betalen']
        hi_ed = data.loc[(data['servicetype'] == 'eten') |
                         (data['servicetype'] == 'drinken')]
        hi_eb = data.loc[(data['servicetype'] == 'eten') |
                         (data['servicetype'] == 'betalen')]
        hi_db = data.loc[(data['servicetype'] == 'drinken') |
                         (data['servicetype'] == 'betalen')]
        hi_edb = data.loc[(data['servicetype'] == 'eten') |
                          (data['servicetype'] == 'drinken') |
                          (data['servicetype'] == 'betalen')]

        # Heatmaps aanmaken
        hi_namen = ['e', 'd', 'b']
        var_list = []
        for i in range(0, len(hi_namen) + 1):
            for subset in itertools.combinations(hi_namen, i):
                div = ''.join(subset) + '_div'
                script = ''.join(subset) + '_script'
                dataset = 'hi_' + ''.join(subset)

                try:
                    div, script = self.heatmap_dataframe(eval(dataset))
                except Exception:
                    leeg = figure(title="Kliks per tafel",
                                  plot_width=900, plot_height=400)
                    leeg.toolbar.logo = None
                    leeg.toolbar_location = None
                    div, script = components(leeg)

                var_list.append(div)
                var_list.append(script)
        return (*var_list,)

    def heatmap_dataframe(self, hd_dataset):
        """
        De methode start met het bepalen van het hoogste tafelnummer
        van het meegegeven dataframe. Een nieuw dataframe, met 0-9
        als kolomnamen (de eenheden van de tafelnummers), wordt
        vervolgens opgebouwd voor de heatmap. Het laatste cijfer van
        het hoogste tafelnummer wordt verwijderd en bepaalt het aantal
        benodigde rijen (de tientallen van de tafels nummers) voor dit
        dataframe.

        Per 10 tafels wordt vervolgens het aantal kliks voor de
        respectivelijke tafels opgehaald en opgeslagen in een lijst.
        Het ophalen van het aantal kliks per tafel wordt mogelijk
        gemaakt door de rij- en kolomnamen te combineren tot
        tafelnummer en dit als variabele te gebruiken om een optelsom
        hiervan te nemen uit het meegekregen dataframe. Indien een
        tafel niet aanwezig is wordt hiervoor 0 kliks toegewezen.

        Het aantal kliks van deze 10 tafels wordt vervolgens toegevoegd
        aan het dataframe. Dit principe van kliks per tafel ophalen en
        wegschrijven wordt herhaald tot elke rij is gevult.

        Tot slot wordt bokeh_heatmap() aangeroepen, waarbij het
        dataframe wordt meegegeven. Uit de bokeh_heatmap() functie
        komen twee variabelen terug; script en div. Deze twee variabelen
        worden teruggegeven aan heatmap_input().

        :param hd_dataset: Dataframe van de huidige datum selectie.
        :return script: De functionaliteiten van het bokeh plot.
        :return div: Het bokeh figuur.
        """
        max_nr = hd_dataset['tafel_nr'].max()
        df = pd.DataFrame(columns=['T_Tiental', '0', '1', '2', '3',
                                   '4', '5', '6', '7', '8', '9'])
        for i in range(int(str(max_nr)[:-1]) + 1):
            tafel_counts = []
            for j in range(10):
                tafel = int(str(i)+str(j))
                aantal_clicks = hd_dataset[hd_dataset['tafel_nr'] ==
                                           int(tafel)].tafel_nr.count()
                if aantal_clicks != 0:
                    tafel_counts.append(int(aantal_clicks))
                else:
                    tafel_counts.append(0)
            df.loc[i] = [str(i)] + list(tafel_counts)
        df['T_Tiental'] = df['T_Tiental'].astype(str)
        df = df.set_index('T_Tiental')
        script, div = self.bokeh_heatmap(df)
        return script, div

    def bokeh_heatmap(self, bh_input):
        """
        De methode begint met het maken van een kopie van het
        meegekregen dataframe. Hieraan wordt vervolgens een kolomnaam
        toegevoegd.

        Twee variabelen worden vervolgens aangemaakt en gevult met een
        lijst van kolom- en rij-namen; de X- en Y-assen.

        Het dataframe wordt omgezet tot een 1D array, waarna de kleuren
        worden gedefinieerd en toegewezen aan het model.

        De te gebruike bokeh tools worden gedefinieerd waarna het
        figuur waarin de heatmap komt wordt aangemaakt. Hierbij worden
        de assen berekend aan de hand van de tafelnummers en de
        afmetingen ingesteld op 400(l) x 900(b). De Hover- en Taptool
        van bokeh worden gedefinieerd.

        Vervolgens worden enkele instellingen aangepast voor de X- en
        Y-assen zoals lijnkleur (uit), label standoff (0) en grootte
        (10pt). De toolbar en het logo van bokeh worden hierbij op
        inactief gezet, waarna het dataframe wordt ingeladen.

        Bij het inladen van het dataframe in de heatmap worden de X- en
        Y-as gezet op de eerder gecreeerde variabelen hiervoor. De
        afmetingen per cel worden op 1(l) x 1(b) gezet. Verder wordt
        het dataframe meegegeven en de kleuren toegekend zoals eerder
        gedefinieerd.

        Een URL wordt klaargezet voor Bokeh's taptool. Deze URL wordt
        samen met de index van een tafel meegegeven wanneer op een
        cel in de heatmap wordt geklikt.

        De colorbar, die dient als legenda voor de kleuren in de
        heatmap, wordt als laatst gedefinieerd, waarbij font grootte
        (8pt), de ticker(aantal kleuren) en label standoff(6) worden
        gedefinieerd. De colorbar wordt rechts van het plot geplaatst.

        Als laatste wordt het plot onderverdeeld in diens script en div
        componentent, die teruggegeven worden aan heatmap_dataframe().

        :param bh_input: Dataframe van de huidige datum selectie.
        :return script: De functionaliteiten van het bokeh plot.
        :return div: Het bokeh figuur.
        """
        data = bh_input
        data.columns.name = 'T_Eenheid'

        t_eenheid = list(data.index)
        t_tiental = list(data.columns)

        # Reshape to 1D array
        df_h = pd.DataFrame(data.stack(), columns=['rate']).reset_index()
        values = df_h['rate'].max()

        # Kleuren toekennen aan de hand van aantal kliks
        if values == 1:
            colors = ["#006400"]
        elif values == 2 or values == 4 or values == 6:
            colors = ["#006400", "#8B0000"]
        elif values == 3 or values == 5 or values == 7:
            colors = ["#006400", "#FFFF00", "#8B0000"]
        else:
            colors = ["#006400", "#008000", "#7CFC00", "#FFFF00",
                      "#FFA500", "#FF3333", "#ff0000", "#8B0000"]

        if values == 1:
            mapper = LinearColorMapper(palette=colors, low=1,
                                       low_color='white')
        else:
            mapper = LinearColorMapper(palette=colors, low=1,
                                       high=df_h.rate.max(), low_color='white')

        TOOLS = "hover, tap"

        p = figure(title="Kliks per tafel",
                   x_range=t_tiental, y_range=list(reversed(t_eenheid)),
                   x_axis_location="above", plot_width=900, plot_height=400,
                   tools=TOOLS, toolbar_location='below',
                   tooltips=[('Tafel nummer', '$index'),
                             ('Aantal kliks', '@rate')])

        p.grid.grid_line_color = None
        p.axis.axis_line_color = None
        p.axis.major_tick_line_color = None
        p.axis.major_label_standoff = 0
        p.xaxis.axis_label_text_font_size = "10pt"
        p.yaxis.axis_label_text_font_size = "10pt"
        p.toolbar.logo = None
        p.toolbar_location = None

        p.rect(x="T_Eenheid", y="T_Tiental", width=1, height=1,
               source=df_h,
               fill_color={'field': 'rate', 'transform': mapper},
               line_color=None)

        url = "http://127.0.0.1:8000/detail/?tafel_nr=@index"
        taptool = p.select(type=TapTool)
        taptool.callback = OpenURL(url=url)

        color_bar = ColorBar(color_mapper=mapper,
                             major_label_text_font_size="8pt",
                             ticker=BasicTicker(desired_num_ticks=len(colors)),
                             formatter=PrintfTickFormatter(format="%d"),
                             label_standoff=6, border_line_color=None,
                             location=(0, 0))
        p.add_layout(color_bar, 'right')
        script, div = components(p)
        return script, div
