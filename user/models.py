#user/models.py
import string

from django.db import models


class UserInfo(models.Model):
    user_index = models.AutoField(primary_key=True, null=False)
    user_email = models.CharField(max_length=40, null=False, default=False)
    user_name = models.CharField(max_length=20, null=False, default=False)
    login_type = models.CharField(max_length=25, null=True, default=False)

    class Meta:
        db_table = 'user_info'
        verbose_name = '유저정보 테이블'