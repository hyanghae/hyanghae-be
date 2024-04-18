FROM python:3.9-slim

# 작업 디렉토리 설정
WORKDIR /usr/src/app

# 필요한 파일 복사
COPY . .

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y libgl1-mesa-glx libglib2.0-0 && \
    pip install --no-cache-dir -r requirements.txt

# Flask 애플리케이션 실행
CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0"]
