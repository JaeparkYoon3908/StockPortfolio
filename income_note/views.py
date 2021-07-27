# income_note/views.py

from django.core.paginator import Paginator
from rest_framework.response import Response
from rest_framework.views import APIView

from income_note.models import IncomeNote
from response_set.response import CustomResponse
from user.models import UserInfo


class IncomeNoteAPI(APIView):
    def post(self, request):
        user_index = request.META.get('HTTP_USER_INDEX')
        subjectName = request.data.get("subjectName")
        realPainLossesAmount = request.data.get("realPainLossesAmount")
        sellDate = request.data.get("sellDate")
        gainPercent = request.data.get("gainPercent")
        purchasePrice = request.data.get("purchasePrice")
        sellPrice = request.data.get("sellPrice")
        sellCount = request.data.get("sellCount")

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

    def get(self, request):
        user_index = request.META.get('HTTP_USER_INDEX')
        page = request.GET.get('page')
        page_size = request.GET.get('size')
        all_income_note = IncomeNote.objects.filter(user_index=user_index)
        total_elements = len(all_income_note)
        paginator = Paginator(all_income_note, page_size)
        income_note_list = paginator.get_page(page).object_list
        income_note = [obj.get_resp_json() for obj in income_note_list]
        if int(page) * int(page_size) > total_elements + int(page_size):
            data = dict(
                page_info=dict(
                    page=page,
                    page_size=page_size,
                    total_elements=total_elements
                ),
                income_note=""
            )
        else:
            data = dict(
                page_info=dict(
                    page=page,
                    page_size=page_size,
                    total_elements=total_elements
                ),
                income_note=income_note
            )

        return Response(data=data)

    def put(self, request):
        user_index = request.META.get('HTTP_USER_INDEX')
        id = request.data.get("id")
        subjectName = request.data.get("subjectName")
        realPainLossesAmount = request.data.get("realPainLossesAmount")
        sellDate = request.data.get("sellDate")
        gainPercent = request.data.get("gainPercent")
        purchasePrice = request.data.get("purchasePrice")
        sellPrice = request.data.get("sellPrice")
        sellCount = request.data.get("sellCount")

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
            data = CustomResponse.ok
        else:
            data = CustomResponse.no_index

        return Response(data=data)

    def delete(self, request):
        user_index = request.META.get('HTTP_USER_INDEX')
        id = request.data.get("id")

        if UserInfo.objects.filter(user_index=user_index).exists() is False:
            data = CustomResponse.no_index
            return Response(data=data)

        try:
            income_note = IncomeNote.objects.get(pk=id)
            income_note.delete()
            data = CustomResponse.ok
            return Response(data=data)
        except:
            data = dict(
                status=601,
                msg="id 값이 존재하지 않습니다."
            )
            return Response(data=data)
