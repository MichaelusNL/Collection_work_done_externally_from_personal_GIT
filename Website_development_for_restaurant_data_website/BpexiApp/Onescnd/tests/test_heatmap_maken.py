from unittest import TestCase
from BpexiApp.Onescnd.heatmap_maken import Heatmap_maken
import pandas as pd


hm = Heatmap_maken()
df_input = pd.DataFrame({"tafel_nr": [1, 10, 100],
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


class TestHeatmap_maken(TestCase):
    def test_heatmap_input(self):
        # Testen van return statement
        try:
            *test_1, = hm.heatmap_input(df_input)
            self.assertEqual(len(test_1), 16)
            print("\nHeatmap_input methode bestaat en wordt gebruikt.")
        except AttributeError:
            print("\nHeatmap_input methode bestaat niet.")

    def test_heatmap_dataframe(self):
        # Testen van return statement
        try:
            *test_1, = hm.heatmap_dataframe(df_input)
            self.assertEqual(len(test_1), 2)
            print("\nHeatmap_dataframe methode bestaat en wordt gebruikt.")
        except AttributeError:
            print("\nHeatmap_dataframe methode bestaat niet.")
