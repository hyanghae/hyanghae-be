package flaskspring.demo.tag.controller;

import flaskspring.demo.tag.dto.req.ReqTagIndexes;
import flaskspring.demo.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/members/{memberId}/tags")
    public ResponseEntity<?> saveMemberTags(@PathVariable Long memberId, @RequestBody ReqTagIndexes request) {
        tagService.saveMemberTags(memberId, request.getTagIndexes());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
