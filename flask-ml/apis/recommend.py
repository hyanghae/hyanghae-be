import logging
from flask import Blueprint, jsonify, make_response, request
from sklearn.neighbors import KNeighborsClassifier
import pickle

# 모델과 스케일러 로드
with open('dataset/knn_model.pkl', 'rb') as file:
    loaded_model = pickle.load(file)

with open('dataset/scaler.pkl', 'rb') as file:
    standard_scaler = pickle.load(file)


with open('dataset/famous_place_vector.pkl', 'rb') as file:
    famous_place_vector = pickle.load(file)
    print("Loaded famous place vector:")
    for i, row in enumerate(famous_place_vector, start=1):
        print(f"Row {i}: {row}")


recommend = Blueprint("recommend", __name__)

# 로그 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def get_nearest_3_famous_place(test_data):
    test_data_scaled = standard_scaler.transform(test_data)
    distances, indexes = loaded_model.kneighbors(test_data_scaled, n_neighbors=3)
    
    print(indexes)
    
    print("샘플과 각 클래스까지의 거리:")
    nearest_classes = []
    for i in range(len(distances[0])):
        if i == 0:
            print("가장 비슷한 인기 여행지 벡터")
            print(famous_place_vector[indexes[0][i]])
        print("Class:", "place"+str(indexes[0][i]+1), "Distance:", distances[0][i])
        place = "place" + str(indexes[0][i]+1)
        nearest_classes.append(place)
    return nearest_classes



@recommend.route("", methods=["POST"])
def recommend_not_famous_place_by_feature():
    print("요청 들어옴")
   
    tag_score = request.json  # 전체 JSON 데이터 가져오기
    print("들어온 비인기 여행지 벡터")
    print(tag_score)

    # 딕셔너리의 값들을 가져와 배열로 변환 (순서 유지)
    scores = [tag_score[f"tag{i+1}"] for i in range(24)]
    print(scores)
    # 테스트 데이터 생성
    test_data = [scores]

    # get_nearest_3_famous_place 함수에 전달하여 인덱스 가져오기
    nearest_classes = get_nearest_3_famous_place(test_data)

    print(len(nearest_classes))
   
    # 가져온 클래스명을 응답 데이터로 구성하여 반환
    response_data = {
        "firstPlace": nearest_classes[0],
        "secondPlace": nearest_classes[1],
        "thirdPlace": nearest_classes[2]
    }

    return make_response(jsonify(response_data), 200)