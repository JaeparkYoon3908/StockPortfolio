# income_not/urls.py
from django.conf.urls import url
from . import views


urlpatterns = [
    url('income_note', views.IncomeNoteAPI.as_view(), name='income_note'),
]