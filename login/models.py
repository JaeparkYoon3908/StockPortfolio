#login/models.py
from django.db import models


class LoginUser(models.Model):
    user_id = models.CharField(max_length=20, null=False, default=False)
    user_pw = models.CharField(max_length=20, null=False, default=False)

    class Meta:
        db_table = 'login_user'
        verbose_name = '로그인 테스트 테이블'