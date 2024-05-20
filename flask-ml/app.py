from flask import Flask
from flask_sqlalchemy import SQLAlchemy
# from apis.user import member_bp
from apis.recommend import recommend
from apis.img_recommend import img_recommend
from extensions import db


app = Flask(__name__)
# 로컬 환경
# app.config["SQLALCHEMY_DATABASE_URI"] = (
#     "mysql+pymysql://root:1234@127.0.0.1:3306/flaskexample"
# )
# 배포 환경
app.config["SQLALCHEMY_DATABASE_URI"] = (
    "mysql+pymysql://root:root1234@hyanghae.c7smw8oye3a4.ap-northeast-2.rds.amazonaws.com:3306/hyanghae"
)

app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

# app.register_blueprint(member_bp, url_prefix="/ml/api/members")
app.register_blueprint(recommend, url_prefix="/ml/api/recommends")
app.register_blueprint(img_recommend, url_prefix="/ml/api/img-recommends")

db.init_app(app)


with app.app_context():
    db.create_all()

# 사용자 블루프린트 등록

if __name__ == "__main__":
    app.run(debug=True)
