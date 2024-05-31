import os
import base64
from flask import Flask, Response, request
import json

app = Flask(__name__)

# Ensure the necessary directories exist
os.makedirs('user_data', exist_ok=True)
os.makedirs('login_data', exist_ok=True)
user_data_folder = 'user_data'
user_images_folder = 'user_images'

# 회원가입 엔드포인트
@app.route('/signup', methods=['POST'])
def signup():
    data = request.json
    auth = data.get('auth')
    name = data.get('name')
    ID = data.get('ID')
    password = data.get('password')
    patient_id = data.get('patient_id')

    # ID 중복 확인
    if os.path.exists(f'login_data/{ID}.txt'):
        return '', 400

    if auth == "1":
        # 환자 파일 확인
        if os.path.exists(f'user_data/{patient_id}.txt'):
            # 로그인 데이터 파일 생성
            with open(f'login_data/{ID}.txt', 'w') as file:
                file.write(f'{password}\n{auth}\n{name}')
            return '', 200
        else:
            return '', 404
    elif auth in ["2", "3", "4"]:
        # 환자 ID 확인 없이 로그인 데이터 파일 생성
        with open(f'login_data/{ID}.txt', 'w') as file:
            file.write(f'{password}\n{auth}\n{name}')
        return '', 200
    else:
        return '', 400

# 로그인 엔드포인트
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    ID = data.get('ID')
    password = data.get('password')

    # 로그인 데이터 파일 확인
    if not os.path.exists(f'login_data/{ID}.txt'):
        return '', 400

    # 로그인 데이터 파일 읽기
    with open(f'login_data/{ID}.txt', 'r') as file:
        stored_password = file.readline().strip()
        auth = file.readline().strip()
        name = file.readline().strip()

    if password == stored_password:
        return jsonify({'status_code': 200, 'auth': auth, 'name': name}), 200
    else:
        return '', 400

# 사용자 정보 가져오기 엔드포인트
@app.route('/api/user/<int:user_id>', methods=['GET'])
def get_user_info(user_id):
    user_info_file = os.path.join(user_data_folder, f"{user_id}.txt")
    try:
        with open(user_info_file, 'r', encoding='utf-8') as f:
            user_info = f.readlines()
        user_info_data = [line.strip() for line in user_info]
        image_file = os.path.join(user_images_folder, f"{user_id}.jpg")
        if os.path.exists(image_file):
            with open(image_file, 'rb') as f:
                image_data = f.read()
            image_base64 = base64.b64encode(image_data).decode('utf-8')
            return Response(
                response=json.dumps(
                    {"Code": "200", "name": user_info_data[0].split(': ')[1], "address": user_info_data[1].split(': ')[1], "email": user_info_data[2].split(': ')[1], "birthdate": user_info_data[3].split(': ')[1], "phone_number": user_info_data[4].split(': ')[1], "image": image_base64}
                ),
                status=200,
                mimetype='application/json'
            )
        else:
            return Response(
                response=json.dumps({"Code": "404", "error": "Image not found"}),
                status=404,
                mimetype='application/json'
            )
    except FileNotFoundError:
        return Response(
            response=json.dumps({"Code": "404", "error": "User not found"}),
            status=404,
            mimetype='application/json'
        )

# 모든 사용자 정보 가져오기 엔드포인트
@app.route('/api/user', methods=['GET'])
def get_users():
    user_id = request.args.get('id')
    if user_id:
        return get_user_info(user_id)
    else:
        all_users_info = {}
        for user_id in range(1, 20):  # 1부터 20까지의 사용자 ID에 대해 반복
            user_info_file = os.path.join(user_data_folder, f"{user_id}.txt")
            try:
                with open(user_info_file, 'r', encoding='utf-8') as f:
                    user_info = f.readlines()
                user_info_data = [line.strip() for line in user_info]
                image_file = os.path.join(user_images_folder, f"{user_id}.jpg")
                if os.path.exists(image_file):
                    with open(image_file, 'rb') as f:
                        image_data = f.read()
                    image_base64 = base64.b64encode(image_data).decode('utf-8')
                    all_users_info[user_id] = {"name": user_info_data[0].split(': ')[1], "address": user_info_data[1].split(': ')[1], "email": user_info_data[2].split(': ')[1], "birthdate": user_info_data[3].split(': ')[1], "phone_number": user_info_data[4].split(': ')[1], "image": image_base64}
                else:
                    all_users_info[user_id] = {"name": user_info_data[0].split(': ')[1], "address": user_info_data[1].split(': ')[1], "email": user_info_data[2].split(': ')[1], "birthdate": user_info_data[3].split(': ')[1], "phone_number": user_info_data[4].split(': ')[1], "image": ""}
            except FileNotFoundError:
                all_users_info[user_id] = {"error": "User not found"}
        return Response(
            response=json.dumps(all_users_info, ensure_ascii=False),
            status=200,
            mimetype='application/json'
        )

# aqs.txt 파일 경로
aqs_file_path = './brain/aqs.txt'

# aqs.txt 내용 가져오기 엔드포인트
@app.route('/brain', methods=['GET'])
def get_aqs():
    try:
        with open(aqs_file_path, 'r', encoding='utf-8') as f:
            aqs_data = f.read().strip()
        return jsonify({"Code": 200, "data": aqs_data})
    except FileNotFoundError:
        return jsonify({"Code": 404, "error": "File not found"}), 404
    except UnicodeDecodeError as e:
        return jsonify({"Code": 500, "error": f"Encoding error: {str(e)}"}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)