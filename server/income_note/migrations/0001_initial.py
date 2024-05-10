# Generated by Django 3.2.3 on 2021-07-25 02:54

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='IncomeNote',
            fields=[
                ('id', models.IntegerField(auto_created=True, primary_key=True, serialize=False)),
                ('user_index', models.IntegerField(default=False)),
                ('subjectName', models.CharField(default=False, max_length=20)),
                ('realPainLossesAmount', models.CharField(default=False, max_length=100)),
                ('sellDate', models.CharField(default=False, max_length=100, null=True)),
                ('gainPercent', models.CharField(default=False, max_length=100)),
                ('purchasePrice', models.CharField(default=False, max_length=100)),
                ('sellPrice', models.CharField(default=False, max_length=100)),
                ('sellCount', models.IntegerField(default=False)),
            ],
            options={
                'verbose_name': '수익노트 테이블',
                'db_table': 'income_note',
            },
        ),
    ]