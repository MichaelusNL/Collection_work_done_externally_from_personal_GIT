# URL Configuration
from django.contrib import admin
from django.urls import path
from Onescnd import views


urlpatterns = [
    path('admin/', admin.site.urls, name='admin'),
    path('', views.start, name='start'),
    path('overzicht/', views.overzicht, name='overzicht'),
    path('vergelijk/', views.vergelijk, name='vergelijk'),
    path('detail/', views.detail, name='detail'),
]
