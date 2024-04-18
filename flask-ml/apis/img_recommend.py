from flask import Blueprint, jsonify, make_response, request
import pickle
import cv2
import numpy as np
import gzip
import os
from keras.applications.vgg16 import preprocess_input
import tensorflow as tf
from keras.models import model_from_json
from sklearn.metrics.pairwise import cosine_similarity


img_recommend = Blueprint("img_recommend", __name__)

# 모델 아키텍처를 불러오기
with open("dataset/model_architecture.json", "r") as json_file:
    loaded_model_json = json_file.read()
# 저장된 파일들의 디렉토리 경로
input_directory = "dataset/split_feature_maps"
# 병합할 딕셔너리
merged_feature_maps_with_names = {}
# 저장된 파일들을 읽어와서 하나의 딕셔너리로 병합
file_names = sorted(os.listdir(input_directory))  # 파일 목록을 정렬

for file_name in file_names:
    if file_name.endswith(".gz"):
        file_path = os.path.join(input_directory, file_name)
        with gzip.open(file_path, 'rb') as f:
            chunk = pickle.load(f)
            merged_feature_maps_with_names.update(chunk)

# 병합된 feature map 확인
print("Merged feature map:", len(merged_feature_maps_with_names))

# 불러온 모델 아키텍처로 모델 구성하기
loaded_model = model_from_json(loaded_model_json)
# 저장된 가중치를 불러오기
loaded_model.load_weights("dataset/model_weights.weights.h5")


@img_recommend.route('/upload-image', methods=['POST'])
def upload_image():
    if 'photo' not in request.files:
        return 'No image part'

    file = request.files['photo']
    if file.filename == '':
        return 'No selected image'

    # 이미지 파일을 바이트로 읽어옴
    image_bytes = file.read()

    sample_feature_vector = extract_feature_vector(image_bytes)
    top_similarities =  find_top_similarities(sample_feature_vector, merged_feature_maps_with_names)

    # JSON 형식으로 데이터 구성
    result = [{"name": name, "similarity": float(similarity)} for name, similarity in top_similarities] #직렬화를 위해 float형식으로

    # JSON 형식으로 반환
    return make_response(jsonify(result), 200)


def extract_feature_vector(image_bytes):
    # 이미지를 numpy 배열로 변환
    nparr = np.frombuffer(image_bytes, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # 이미지 전처리
    img = cv2.resize(img, (224, 224))  
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)  
    img = np.expand_dims(img, axis=0)  
    img = preprocess_input(img)  

    # 특성 벡터 추출
    feature_vector = loaded_model.predict(img)

    return feature_vector


def find_top_similarities(sample_feature_vector, feature_maps_with_names, top_n=3):
    similarities = {}
    for data_name, feature_vector in feature_maps_with_names.items():
        similarity = cosine_similarity(feature_vector.reshape(1, -1), sample_feature_vector.reshape(1, -1))[0][0]
        similarities[data_name] = similarity

    # 코사인 유사도를 기준으로 내림차순으로 정렬
    sorted_similarities = sorted(similarities.items(), key=lambda x: x[1], reverse=True)

    # 상위 top_n개의 유사도와 해당하는 데이터 이름 출력
    top_similarities = sorted_similarities[:top_n]
    return top_similarities
