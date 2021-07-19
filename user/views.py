# user/views.py
from rest_framework.views import APIView
from rest_framework.response import Response

import user.models
from .models import UserInfo


class RegistUser(APIView):
    def post(self, request):
        user_email = request.data.get('user_email', "") # 클라이언트에서 올리는 user_id
        user_name = request.data.get('user_name', "") # 클라이언트에서 올리는 user_pw
        login_type = request.data.get('login_type', "") # 클라이언트에서 올리는 login_type

        UserInfo.objects.create(user_email=user_email, user_name=user_name, login_type=login_type) # LoginUser 모델에 새로운 object 생성

        # 클라이언트한테 내려줄 데이터 정의
        data = dict(
            status=200,
            data=dict(
                user_email=user_email,
                user_name=user_name,
                login_type=login_type
            )
        )

        return Response(data=data)