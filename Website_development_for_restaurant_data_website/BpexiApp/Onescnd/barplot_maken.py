from bokeh.embed import components
from bokeh.models import ColumnDataSource
from bokeh.plotting import figure
from bokeh.palettes import Spectral4


class Barplot_maken:
    def bokeh_barplot(self, bb_input):
        """
        De methode start met het aanmaken van een lijst met de namen
        van de verschilende servicetypen en 'totaal'. Voor elk van de
        drie servicetypen wordt het dataframe gefiltered en het aantal
        rijen geteld. Het aantal rijen van de drie servicetypen samen
        wordt opgeslagen in een variabele voor het totaal. Deze vier
        variabelen worden in een lijst opgeslagen.

        Een nieuwe lijst wordt vervolgens gecreeerd met het
        percentuele aantal kliks. Indien geen data aanwezig is zal een
        lijst met nul-waarden worden gecreeerd.

        De functie barplot_maker() wordt aangeroepen om deze lijsten
        met het aantal kliks om te zetten in een barplot. Hierbij
        wordt de lijst met servicetypen, het aantal kliks, een
        y-as reeks en titel meegegeven.

        De methode barplot_maker() keert twee variabelen terug; een
        script en een div. Het aanmaken van de barplots gebeurd voor
        zowel het absolute en percentuele aantaal kliks. De twee
        script's en div's die de methode barplot_maker() genereert
        worden opgeslagen in een lijst en teruggegeven aan detail().

        :parameter: Dataframe van de huidige tafelselectie.
        :return: *bb_lijst: Lijst met alle script's en div's.
        """
        servicetypen = ['Totaal', 'Eten', 'Drinken', 'Betalen']
        e_counts = len(bb_input.loc[bb_input['servicetype'] ==
                                    'eten'].index)
        d_counts = len(bb_input.loc[bb_input['servicetype'] ==
                                    'drinken'].index)
        b_counts = len(bb_input.loc[bb_input['servicetype'] ==
                                    'betalen'].index)
        e_totaal = e_counts + d_counts + b_counts
        counts_ab = [e_totaal, e_counts, d_counts, b_counts]

        try:
            counts_pe = [(e_totaal / e_totaal * 100),
                         (e_counts / e_totaal * 100),
                         (d_counts / e_totaal * 100),
                         (b_counts / e_totaal * 100)]
        except ZeroDivisionError:
            counts_pe = [0, 0, 0, 0]
        bb_lijst = []
        ab_script, ab_div = self.barplot_maker(servicetypen, counts_ab,
                                               (0, e_totaal),
                                               "Aantal kliks (Absoluut)")
        bb_lijst.append(ab_div)
        bb_lijst.append(ab_script)
        pe_script, pe_div = self.barplot_maker(servicetypen, counts_pe,
                                               (0, 100),
                                               "Aantal kliks (Procentueel)")
        bb_lijst.append(pe_div)
        bb_lijst.append(pe_script)
        return (*bb_lijst, )

    def barplot_maker(self, servicetypen, counts, y_range, title):
        """
        De methode begint met het maken van een dataframe, waarbij de
        servicetypen, het aantal kliks en de kleuren in een dictionary
        worden opgeslagen. Vervolgens wordt het figuur aangemaakt voor
        een barplot waarbij de x_range gelijk staat aan het aantal
        servicetypen, de y_range gelijk staat aan het aantal kliks,
        de hoogte op 900px, de breedte op 350px, de titel zoals mee-
        gegeven vanuit bokeh_barplot(), en de toolbar en tools van
        Bokeh worden uitgezet.

        Het figuur wordt gevult met stafen, 1 per servicetype, voor het
        aantal kliks. Bij het aanmaken van deze stafen wordt diens
        servicetype als x-as label en legenda item meegegeven. Het
        aantal kliks dient als counts. De breedt van de stafen wordt
        op 0.9 gezet en de kleur wordt gedefinieerd volgens Bokeh's
        Spectral4. Als laatst wordt het dataframe meegegeven als
        source.

        Het grid van het plot wordt uitgezet. Het y-as startpunt staat
        op 0. De legenda orientatie staat op verticaal en de legenda
        locatie op rechtsboven. De legenda wordt tevens interactief
        gemaakt door middel van diens klik-beleid.

        Het barplot wordt opgesplitst in diens componenten, een script
        en een div, alvorens het wordt teruggegeven aan bokeh_barplot.

        :param servicetypen;  X-as labels.
        :param counts; Een lijst met het totaal aantal kliks.
        :param y_range; Een reeks van cijfers voor de y-as.
        :param title; De titel voor het barplot.
        :return script: De functionaliteiten van het bokeh plot.
        :return div: Het bokeh figuur.
        """
        source = ColumnDataSource(data=dict(servicetypen=servicetypen,
                                            counts=counts, color=Spectral4))
        p = figure(x_range=servicetypen, y_range=y_range, plot_width=900,
                   plot_height=350, title=title,
                   toolbar_location=None, tools="")
        p.vbar(x='servicetypen', top='counts', width=0.9, color='color',
               legend='servicetypen', source=source)
        p.xgrid.grid_line_color = None
        p.y_range.start = 0
        p.legend.orientation = "vertical"
        p.legend.location = "top_right"
        p.legend.click_policy = "hide"
        script, div = components(p)
        return script, div
