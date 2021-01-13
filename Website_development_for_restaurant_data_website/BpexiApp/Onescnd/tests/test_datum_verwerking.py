from django.test.client import Client
from unittest import TestCase
from BpexiApp.Onescnd.datum_verwerking import Datum_verwerking
import datetime
import pandas as pd

dv = Datum_verwerking()

df1 = pd.DataFrame({"tafel_nr": [63, 33, 34, 35, 40],
                    "wijk": [1, 2, 3, 4, 5],
                    "begin_tijd": ["2019-05-26 07:19:51", "2018-05-26 "
                                                          "07:19:51",
                                   "2018-05-26 "
                                   "07:19:51", "2018-05-27 07:19:51",
                                   "2018-06-27 "
                                   "07:19:51"],
                    "volg_nr": [0, 1, 2, 3, 4],
                    "eind_tijd": ["2019-05-26 07:20:48", "2018-05-26 "
                                                         "07:19:55",
                                  "2018-05-27 "
                                  "00:00:00",
                                  "2018-06-27 "
                                  "07:19:51", "2019-05-27 07:19:51"],
                    "servicetype": ["betalen", "eten", "betalen",
                                    "drinken", "eten"],
                    "verschil": [0.950000, 0.2, 0.5, 0.7, 0.6],
                    })


class TestDatum_verwerking(TestCase):
    def test_datumFilter(self):
        d = Datum_verwerking()
        dt = "2019-05-26"
        tijd = "dag"
        start, filtered_data = d.datumFilter(dt, tijd, df1)
        self.assertEqual(start, "2019-05-26 00:00:00")
        self.assertEqual(1, filtered_data.shape[0])
        dt = "2018-05-26"
        tijd = "jaar"
        start, filtered_data = d.datumFilter(dt, tijd, df1)
        self.assertEqual(str(start), "2018-01-01")
        self.assertEqual(4, filtered_data.shape[0])
        dt = "2018-06-26"
        tijd = "maand"
        start, filtered_data = d.datumFilter(dt, tijd, df1)
        self.assertEqual(str(start), "2018-06-01 00:00:00")
        self.assertEqual(1, filtered_data.shape[0])
        dt = "2018-05-26"
        tijd = "week"
        start, filtered_data = d.datumFilter(dt, tijd, df1)
        self.assertEqual(str(start), "2018-05-21 00:00:00")
        self.assertEqual(2, filtered_data.shape[0])

    def test_datumNavigatie(self):
        dt = "2018-05-26"
        tijd = "dag"
        volgend = 1
        start, filtered_data = dv.datumNavigatie(dt, volgend, tijd, df1)
        self.assertEqual(str(start), "2018-05-27 00:00:00")
        self.assertEqual(1, filtered_data.shape[0])
        dt = "2018-05-27"
        tijd = "dag"
        vorig = 0
        start, filtered_data = dv.datumNavigatie(dt, vorig, tijd, df1)
        self.assertEqual(str(start), "2018-05-26 00:00:00")
        self.assertEqual(2, filtered_data.shape[0])

    def test_datumSelect(self):
        self.fail()

    def test_userInput(self):
        client = Client()
        response = client.get("http://127.0.0.1:8000/")
        request = response.wsgi_request
        dt = "2019-05-26"
        tijd = "dag"
        live = datetime.datetime.today().strftime("%Y-%m-%d")

        start, data, setting, day = dv.userInput(request.POST, dt, tijd,
                                                 df1, live)
        self.assertEqual(start, "2019-05-26 00:00:00")
        self.assertEqual(1, data.shape[0])

    def test_vorigeOfvolgend(self):
        self.fail()
