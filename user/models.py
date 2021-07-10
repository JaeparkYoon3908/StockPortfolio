#user/models.py
from django.db import models


class UserInfo(models.Model):
    user_index = models.IntegerField(primary_key=True, null=False, db_index=True, auto_created=True)
    user_email = models.CharField(max_length=20, null=False, default=False)
    user_name = models.CharField(max_length=20, null=True, default=False)
    # user_token = models.CharField()

    class Meta:
        db_table = 'user_info'
        verbose_name = '유저 정보'