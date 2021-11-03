# income_note/views.py
from decimal import Decimal

from django.core.paginator import Paginator
from django.db.models import Sum
from rest_framework.response import Response
from rest_framework.views import APIView

from income_note.models import IncomeNote
from response_set.response import CustomResponse
from user.models import UserInfo


class IncomeNoteAPI(APIView):
    def post(self, request):
        try:
            user_index = request.META.get('HTTP_USER_INDEX')
            subjectName = request.data.get("subjectName")
            sellDate = request.data.get("sellDate")
            purchasePrice = Decimal(request.data.get("purchasePrice"))
            sellPrice = Decimal(request.data.get("sellPrice"))
            sellCount = Decimal(request.data.get("sellCount"))

            # 계산
            realPainLossesAmount = (sellPrice - purchasePrice) * sellCount
            gainPercent = ((sellPrice / purchasePrice) - 1) * 100

            if UserInfo.objects.filter(user_index=user_index).exists():
                IncomeNote.objects.create(user_index=user_index,
                                          subjectName=subjectName,
                                          realPainLossesAmount=realPainLossesAmount,
                                          sellDate=sellDate,
                                          gainPercent=gainPercent,
                                          purchasePrice=purchasePrice,
                                          sellPrice=sellPrice,
                                          sellCount=sellCount)  # LoginUser 모델에 새로운 object 생성
                data = dict(
                    status=200,
                    msg="성공"
                )
            else:
                data = CustomResponse.no_index

            return Response(data=data)
        except:
            return Response(CustomResponse.exception)

    def get(self, request):
        try:
            user_index = request.META.get('HTTP_USER_INDEX')
            page = request.GET.get('page')
            page_size = request.GET.get('size')
            start_date = request.GET.get('startDate')
            end_date = request.GET.get('endDate')

            if not start_date or not end_date:
                all_income_note = IncomeNote.objects.filter(user_index=user_index)
            else:
                all_income_note = IncomeNote.objects.filter(user_index=user_index,
                                                            sellDate__range=[start_date, end_date])

            total_elements = len(all_income_note)

            paginator = Paginator(all_income_note, page_size)
            income_note_list = paginator.get_page(page).object_list
            income_note = [obj.get_resp_json() for obj in income_note_list]
            if int(page) > 0:
                pre_income_note_list = paginator.get_page(int(page) - 1).object_list
                if income_note_list != pre_income_note_list:
                    data = dict(
                        page_info=dict(
                            page=page,
                            page_size=page_size,
                            total_elements=total_elements
                        ),
                        income_note=income_note
                    )
                    return Response(data=data)
                else:
                    data = dict(
                        page_info=dict(
                            page=page,
                            page_size=page_size,
                            total_elements=total_elements
                        ),
                        income_note=""
                    )
                    return Response(data=data)

            data = dict(
                page_info=dict(
                    page=page,
                    page_size=page_size,
                    total_elements=total_elements
                ),
                income_note=""
            )
            return Response(data=data)
        except:
            return Response(data=CustomResponse.exception)

    def put(self, request):
        try:
            user_index = request.META.get('HTTP_USER_INDEX')
            id = request.data.get("id")
            subjectName = request.data.get("subjectName")
            sellDate = request.data.get("sellDate")
            purchasePrice = Decimal(request.data.get("purchasePrice"))
            sellPrice = Decimal(request.data.get("sellPrice"))
            sellCount = Decimal(request.data.get("sellCount"))

            # 계산
            realPainLossesAmount = (sellPrice - purchasePrice) * sellCount
            gainPercent = ((sellPrice / purchasePrice) - 1) * 100

            income_note = IncomeNote.objects.get(pk=id)
            if UserInfo.objects.filter(user_index=user_index).exists():
                income_note.subjectName = subjectName
                income_note.realPainLossesAmount = realPainLossesAmount
                income_note.sellDate = sellDate
                income_note.gainPercent = gainPercent
                income_note.purchasePrice = purchasePrice
                income_note.sellPrice = sellPrice
                income_note.sellCount = sellCount
                income_note.save()
                data = dict(
                    id=id,
                    subjectName=subjectName,
                    realPainLossesAmount=realPainLossesAmount,
                    sellDate=sellDate,
                    gainPercent=gainPercent,
                    purchasePrice=purchasePrice,
                    sellPrice=sellPrice,
                    sellCount=sellCount
                )
            else:
                data = CustomResponse.no_index

            return Response(data=data)
        except:
            return Response(data=CustomResponse.exception)
class DeleteIncomeNote(APIView):
    def delete(self, request, income_note_id):
        user_index = request.META.get('HTTP_USER_INDEX')
        if UserInfo.objects.filter(user_index=user_index).exists() is False:
            return Response(data=CustomResponse.no_index)
        try:
            income_note = IncomeNote.objects.get(pk=income_note_id)
            income_note.delete()
            data = CustomResponse.ok
            return Response(data=data)
        except:
            return Response(data=CustomResponse.exception)

class TotalGainAPI(APIView):
    def get(self, request):
        try:
            user_index = request.META.get('HTTP_USER_INDEX')
            start_date = request.GET.get('startDate')
            end_date = request.GET.get('endDate')
            if not start_date or not end_date:
                all_income_note = IncomeNote.objects.filter(user_index=user_index)
            else:
                all_income_note = IncomeNote.objects.filter(user_index=user_index,
                                                            sellDate__range=[start_date, end_date])
            total_price = all_income_note.aggregate(Sum('realPainLossesAmount'))
            total_purchase_price = all_income_note.aggregate(Sum('purchasePrice'))
            total_sell_price = all_income_note.aggregate(Sum('sellPrice'))
            total_percent = ((total_sell_price['sellPrice__sum'] / total_purchase_price[
                'purchasePrice__sum']) - 1) * 100
            data = dict(
                total_price=total_price['realPainLossesAmount__sum'],
                total_percent=round(total_percent, 2)
            )

            return Response(data=data)
        except:
            return Response(data=CustomResponse.exception)
