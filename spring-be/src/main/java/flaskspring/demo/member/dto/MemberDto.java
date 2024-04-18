package flaskspring.demo.member.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String email;

    // 생성자, 게터, 세터, toString 등 필요한 메서드 추가
}
