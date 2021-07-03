"""
WSGI config for server_dev project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/3.2/howto/deployment/wsgi/
"""

import os, sys

from django.core.wsgi import get_wsgi_application

sys.path.append('/home/pi/my_server/ReviewServer/login')

sys.path.append('/home/pi/my_server/ReviewServer/venv/Lib/site-packages')

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'server_dev.settings')

from django.core.wsgi import get_wsgi_application

application = get_wsgi_application()

