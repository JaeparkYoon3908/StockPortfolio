#user/models.py
import string

from django.db import models


class UserInfo(models.Model):
    user_index = models.IntegerField(primary_key=True, null=False, auto_created=True)
    user_email = models.CharField(max_length=20, null=False, default=False)
    user_name = models.CharField(max_length=20, null=False, default=False)

    class Meta:
        db_table = 'user_info'
        verbose_name = '유저정보 테이블'