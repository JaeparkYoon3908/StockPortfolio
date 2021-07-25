#user/models.py
import string

from django.db import models


class IncomeNote(models.Model):
    user_index = models.IntegerField(null=False, auto_created=False, default=False)
    subjectName = models.CharField(max_length=20, null=False, default=False)
    realPainLossesAmount = models.CharField(max_length=100, null=False, default=False)
    sellDate = models.CharField(max_length=100, null=False, default=False)
    gainPercent = models.CharField(max_length=100, null=False, default=False)
    purchasePrice = models.CharField(max_length=100, null=False, default=False)
    sellPrice = models.CharField(max_length=100, null=False, default=False)
    sellCount = models.IntegerField(null=False, default=False)

    def as_json(self):
        return dict(
            user_index=self.user_index,
            subjectName=self.subjectName,
            realPainLossesAmount=self.realPainLossesAmount,
            sellDate=self.sellDate,
            gainPercent=self.gainPercent,
            purchasePrice=self.purchasePrice,
            sellPrice=self.sellPrice,
            sellCount=self.sellCount
        )

    class Meta:
        db_table = 'income_note'
        verbose_name = '수익노트 테이블'