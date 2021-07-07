#uesr/models.py
from django.db import models


class UserInfo(models.Model):
    user_email = models.CharField(max_length=20, null=False, default=False)
    user_name = models.CharField(max_length=20, null=False, default=False)

    class Meta:
        db_table = 'user_info'
        verbose_name = '유저 정보'