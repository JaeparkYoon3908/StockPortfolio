# user/urls.py
from django.conf.urls import url
from . import views


urlpatterns = [
    url('regist_user', views.RegistUser.as_view(), name='regist_user'),
]