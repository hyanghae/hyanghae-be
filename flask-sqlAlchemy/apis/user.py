import logging
from flask import Blueprint, jsonify, make_response, request
from models.models import db, Member

member_bp = Blueprint("member", __name__)

# 로그 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@member_bp.route("", methods=["POST"])
def create_member():
    if request.method == "POST":
        data = request.get_json()
        name = data.get("name")
        email = data.get("email")

        # 새로운 멤버 생성
        new_member = Member(name=name, email=email)

        # 데이터베이스에 멤버 추가
        db.session.add(new_member)
        db.session.commit()

        logger.info("New member created with ID: %s", new_member.id)

        return make_response(jsonify({"message": "Member created successfully!"}), 201)

@member_bp.route("/<int:member_id>", methods=["GET"])
def get_member(member_id):
    member = Member.query.get(member_id)
    if member:
        logger.info("Member retrieved: %s", member.to_dict())
        return make_response(jsonify(member.to_dict()), 200)
    else:
        logger.warning("Member not found with ID: %s", member_id)
        return make_response(jsonify({"error": "Member not found"}), 404)

@member_bp.route("/<int:member_id>", methods=["PUT"])
def update_member(member_id):
    member = Member.query.get(member_id)
    if member:
        data = request.get_json()
        member.name = data.get("name", member.name)
        member.email = data.get("email", member.email)
        db.session.commit()
        logger.info("Member updated: %s", member.to_dict())
        return make_response(jsonify({"message": "Member updated successfully!"}), 200)
    else:
        logger.warning("Member not found with ID: %s", member_id)
        return make_response(jsonify({"error": "Member not found"}), 404)

@member_bp.route("/<int:member_id>", methods=["DELETE"])
def delete_member(member_id):
    member = Member.query.get(member_id)
    if member:
        db.session.delete(member)
        db.session.commit()
        logger.info("Member deleted with ID: %s", member_id)
        return make_response(jsonify({"message": "Member deleted successfully!"}), 200)
    else:
        logger.warning("Member not found with ID: %s", member_id)
        return make_response(jsonify({"error": "Member not found"}), 404)
