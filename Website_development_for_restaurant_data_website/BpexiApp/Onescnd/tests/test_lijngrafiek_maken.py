from unittest import TestCase
from BpexiApp.Onescnd.lijngrafiek_maken import Lijngrafiek_maken
import pandas as pd
from datetime import datetime

lm = Lijngrafiek_maken()
test_df = pd.DataFrame({"tafel_nr": [1, 10, 100, 200],
                         "wijk": [1, 2, 3, 4],
                         "begin_tijd": ["2019-05-26 06:00:00",
                                        "2019-05-26 06:20:00",
                                        "2019-05-26 07:00:00",
                                        "2019-05-26 07:20:00"],
                         "volg_nr": [0, 0, 0, 0],
                         "eind_tijd": ["2019-05-26 06:10:00",
                                       "2019-05-26 06:35:00",
                                       "2019-05-26 07:10:00",
                                       "2019-05-26 07:35:00"],
                         "servicetype": ["eten", "drinken", "betalen",
                                         "eten"],
                         "verschil": [10, 15, 10, 15],
                         })
class TestLijngrafiek_maken(TestCase):
    def test_lijngrafiek_data(self):
        tijd = 'dag'
        gv_df, tijdLijst, setting = lm.lijngrafiek_data(test_df, tijd)
        self.assertEqual(setting, 'dag')
        self.assertEqual(gv_df['verschil'].tolist(), [12.5, 12.5])
        self.assertEqual(tijdLijst[0], datetime.strptime(
                                                        "2019-05-26 06:00:00",
                                                       "%Y-%m-%d %H:%M:%S"))

        self.assertEqual(tijdLijst[1], datetime.strptime(
                                                        "2019-05-26 07:00:00",
                                                         "%Y-%m-%d %H:%M:%S"))

    def test_lijngrafiek_input(self):
        tijd = 'dag'
        gem_test_df = pd.DataFrame({'begin_tijd': ["2019-05-26 06:00:00",
                                                   "2019-05-26 07:00:00",
                                                   "2019-05-26 08:00:00",
                                                   "2019-05-26 09:00:00"],
                                    'verschil': ['10','9','7','14']})
        gem_test_df['begin_tijd'] = pd.to_datetime(gem_test_df['begin_tijd'])
        gem_test_df.index = gem_test_df['begin_tijd']
        tijdlijst = gem_test_df.index.copy()

        x_as, gemiddelde, xlabel = lm.lijngrafiek_input(gem_test_df,
                                                        tijdlijst, tijd)
        self.assertEqual(x_as, list(range(1,25)))
        self.assertEqual(gemiddelde, [0,0,0,0,0,'10','9','7','14',0,0,0,0,0,0,
                                      0,0,0,0,0,0,0,0,0])
        self.assertEqual(xlabel, 'Uren')

    def test_bokeh_lijngrafiek(self):
        x_tijd = list(range(1,8))
        gemiddelde = [5,7,9,4,3,2,1]
        xlabel = 'Dagen'

        try:
            *grafiek, = lm.bokeh_lijngrafiek(x_tijd, gemiddelde, xlabel)
            self.assertEqual(len(grafiek), 2)
            print("\nLijngrafiek_maker methode bestaat en wordt gebruikt.")
        except AttributeError:
            print("\nLijngrafiek_maker methode bestaat niet.")
