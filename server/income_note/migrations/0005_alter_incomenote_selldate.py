# Generated by Django 3.2.3 on 2021-08-18 12:13

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('income_note', '0004_auto_20210818_2102'),
    ]

    operations = [
        migrations.AlterField(
            model_name='incomenote',
            name='sellDate',
            field=models.CharField(default=False, max_length=100),
        ),
    ]