import logging
from flask import Blueprint, jsonify, make_response, request
from sklearn.neighbors import KNeighborsClassifier
import pickle
from collections import OrderedDict

# 모델과 스케일러 로드
with open('dataset/place/place_knn_model.pkl', 'rb') as file:
    place_knn_model = pickle.load(file)

with open('dataset/place/place_scaler.pkl', 'rb') as file:
    place_scaler = pickle.load(file)


with open('dataset/place/place_tag_vector.pkl', 'rb') as file:
    place_tag_vector = pickle.load(file)
    print("Loaded famous place vector:")
    for i, row in enumerate(place_tag_vector, start=1):
        print(f"Row {i}: {row}")


similar = Blueprint("similar", __name__)

def get_similar_places(test_data, countCursor, size=10):
    # 데이터 표준화
    test_data_scaled = place_scaler.transform(test_data)
    
    # 충분히 큰 n_neighbors 설정
    total_neighbors = countCursor * size
    distances, indexes = place_knn_model.kneighbors(test_data_scaled, n_neighbors=total_neighbors)
    
    print("전체 이웃 인덱스:", indexes)
    print("샘플과 각 클래스까지의 거리:", distances)
    #print("해당 여행지의 벡터:", distances)
    
    start_index = (countCursor - 1) * size
    end_index = start_index + size

    # 해당 구간의 인덱스와 거리 추출
    selected_indexes = indexes[0][start_index:end_index]
    selected_distances = distances[0][start_index:end_index]

    similar_places = []
    for i in range(len(selected_indexes)):
        place_index = selected_indexes[i]
        distance = selected_distances[i]
        place = "place" + str(place_index + 1)
        print(f"Class: {place}, Distance: {distance}" "Vector:", place_tag_vector[place_index])
        similar_places.append(place)
    
    return similar_places


@similar.route("", methods=["POST"])
def recommend_places_by_famous():
    print("요청 들어옴")

    # 요청 데이터 가져오기
    tag_score = request.json
    print("들어온 인기 여행지 벡터")
    print(tag_score)

    # 딕셔너리의 값들을 가져와 배열로 변환 (순서 유지)
    scores = [tag_score[f"tag{i+1}"] for i in range(24)]
    print(scores)
    # 테스트 데이터 생성
    test_data = [scores]

    # cursor와 size 추출
    cursor = int(request.args.get("cursor", default=0))
    size = int(request.args.get("size", default=10))

    # get_similar_places 함수에 전달하여 인덱스 가져오기
    similar_places = get_similar_places(test_data, cursor, size)

    print(len(similar_places))

    # 가져온 클래스명을 응답 데이터로 구성하여 반환
    response_data = OrderedDict({f"place{i+1}": similar_places[i] for i in range(len(similar_places))})

    return make_response(jsonify(response_data), 200)