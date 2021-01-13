# Django model
from django.db import models


class RestaurantData(models.Model):
    tafel_nr = models.IntegerField(blank=True, null=True)
    wijk = models.IntegerField(blank=True, null=True)
    begin_tijd = models.TextField(blank=True, null=True)
    volg_nr = models.IntegerField(blank=True, null=True)
    eind_tijd = models.TextField(blank=True, null=True)
    servicetype = models.TextField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'onescnd_restaurantdata'
