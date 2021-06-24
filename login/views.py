# login/views.py
from rest_framework.views import APIView
from rest_framework.response import Response
from .models import LoginUser


class RegistUser(APIView):
    def post(self, request):
        user_id = request.data.get('user_id', "") # 클라이언트에서 올리는 user_id
        user_pw = request.data.get('user_pw', "") # 클라이언트에서 올리는 user_pw

        LoginUser.objects.create(user_id=user_id, user_pw=user_pw) # LoginUser 모델에 새로운 object 생성

        # 클라이언트한테 내려줄 데이터 정의
        data = dict(
            user_id=user_id,
            user_pw=user_pw
        )

        return Response(data=data)