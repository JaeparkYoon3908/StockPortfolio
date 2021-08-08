# server_dev/urls.py
from django.contrib import admin
from django.urls import path, include
from django.contrib import admin
from django.urls import path, include
from rest_framework_jwt.views import obtain_jwt_token, verify_jwt_token, refresh_jwt_token

from income_note import views
urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/token/', obtain_jwt_token),
    path('api/token/verify/', verify_jwt_token),
    path('api/token/refresh/', refresh_jwt_token),
    path('api/user/', include('user.urls')),
    path('api/income_note/', include('income_note.urls')),
    path('api/income_note/delete/<int:income_note_id>', views.DeleteIncomeNote.as_view()),
]
