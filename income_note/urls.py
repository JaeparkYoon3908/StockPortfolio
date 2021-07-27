# income_not/urls.py
from django.conf.urls import url
from . import views


urlpatterns = [
    url('list', views.IncomeNoteAPI.as_view(), name='income_note'),
]