import logging
from flask import Blueprint, jsonify, make_response, request
import pickle
from sklearn.neighbors import KNeighborsClassifier



recommend = Blueprint("recommend", __name__)

# 로그 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

with open('dataset/knn_model.pkl', 'rb') as file: # with open 의 상대경로는 아마 app.py가 기준인가?
    loaded_model = pickle.load(file)

with open('dataset/ss.pkl', 'rb') as file:
    standart_scaler = pickle.load(file)

def get_nearest_3_famous_place(test_data):
    test_data_scaled = standart_scaler.transform(test_data)
    distances, indexes = loaded_model.kneighbors(test_data_scaled, n_neighbors=3)

    return indexes.tolist()  # 넘파이 배열을 리스트로 변환하여 반환



@recommend.route("", methods=["GET"])
def recoomend_not_famous_place_by_feature():

    # 요청 파라미터에서 특성의 점수 3개를 가져옴
    feature_scores = [request.args.get('feat_a'), request.args.get('feat_b'), request.args.get('feat_c')]
    test_data = [feature_scores]  # 테스트 데이터

    # 인덱스를 가져와서 결과로 리턴
    indexes = get_nearest_3_famous_place(test_data)

    response_data = {
        "firstPlaceId": indexes[0][0],
        "secondPlaceId": indexes[0][1],
        "thirdPlaceId": indexes[0][2]
    }

    return make_response(jsonify(response_data), 200)