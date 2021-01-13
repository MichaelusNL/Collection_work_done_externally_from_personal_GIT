from unittest import TestCase
from BpexiApp.Onescnd.barplot_maken import Barplot_maken
import pandas as pd


bm = Barplot_maken()


class TestBarplot_maken(TestCase):
    def test_bokeh_barplot(self):
        # Dataframe aanmaken dmv. dictionary
        df = pd.DataFrame({"tafel_nr": [1, 10, 100],
                           "wijk": [1, 2, 3],
                           "begin_tijd": ["2019-05-26 06:00:00",
                                          "2019-06-27 07:00:00",
                                          "2019-07-28 07:00:00"],
                           "volg_nr": [0, 0, 0],
                           "eind_tijd": ["2019-05-26 06:10:00",
                                         "2019-06-27 07:10:00",
                                         "2019-07-28 07:10:00"],
                           "servicetype": ["eten", "drinken", "betalen"],
                           "verschil": ["10.000000", "10.000000", "10.000000"],
                           })

        # Testen van return statement
        try:
            *test_1, = bm.bokeh_barplot(df)
            self.assertEqual(len(test_1), 4)
            print("\nBokeh_barplot methode bestaat en wordt gebruikt.")
        except AttributeError:
            print("\nBokeh_barplot methode bestaat niet.")

    def test_barplot_maker(self):
        # Parameters aanmaken voor testen
        servicetypen = ['Totaal', 'Eten', 'Drinken', 'Betalen']
        counts = [100, 50, 20, 30]
        y_range = (0, 100)
        title = "plot_title"

        # Testen van return statement
        try:
            *test_1, = bm.barplot_maker(servicetypen, counts, y_range, title)
            self.assertEqual(len(test_1), 2)
            print("\nBarplot_maker methode bestaat en wordt gebruikt.")
        except AttributeError:
            print("\nBarplot_maker methode bestaat niet.")
