#user/models.py
import string

from django.db import models


class IncomeNote(models.Model):
    user_index = models.IntegerField(null=False, auto_created=False, default=False)
    subjectName = models.CharField(max_length=20, null=False, default=False)
    realPainLossesAmount = models.DecimalField(max_digits=50, decimal_places=2)
    sellDate = models.DateField(auto_now=False)
    gainPercent = models.DecimalField(max_digits=50, decimal_places=2)
    purchasePrice = models.DecimalField(max_digits=50, decimal_places=2)
    sellPrice = models.DecimalField(max_digits=50, decimal_places=2)
    sellCount = models.IntegerField(null=False, default=False)

    def get_resp_json(self):
        return dict(
            id=self.pk,
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
        ordering = ['-id']