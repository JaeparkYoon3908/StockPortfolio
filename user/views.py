# user/views.py
import stat

from rest_framework.views import APIView
from rest_framework.response import Response
from .models import UserInfo


class RegistUser(APIView):
    def post(self, request):
        user_email = request.data.get('user_email', "")  # 클라이언트에서 올리는 user_id
        user_name = request.data.get('user_name', "")  # 클라이언트에서 올리는 user_pw

        if UserInfo.objects.filter(user_email=user_email).exists():
            user = UserInfo.objects.filter(user_email=user_email).first()
            data = dict(
                status=600,
                msg="이미 존재하는 이메일입니다."
            )
            return Response(data)

        UserInfo.objects.create(user_email=user_email, user_name=user_name)  # LoginUser 모델에 새로운 object 생성

        # 클라이언트한테 내려줄 데이터 정의
        data = dict(
            user_email=user_email,
            user_name=user_name
        )

        return Response(data=data)
