package kr.co.apexsoft.jpaboot._support.global;

import kr.co.apexsoft.jpaboot.post.domain.Post;
import kr.co.apexsoft.jpaboot.post.service.PostService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Class Description
 *
 * @author 김혜연
 * @since 2019-03-05
 */
@RestController
@RequestMapping("/global")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GlobalController {
    @NonNull
    private PostService postService;

    /**
     * 랜딩화면 - 최신 게시글 한 건씩 조회
     * @param
     * @return
     */
    @GetMapping("/posts/{postTypes}")
    public ResponseEntity<Map> findPostOne(@PathVariable("postTypes") String postTypes) {
        return ResponseEntity.ok(this.postService.findPostsForHome(postTypes.split(",")));
    }
}
