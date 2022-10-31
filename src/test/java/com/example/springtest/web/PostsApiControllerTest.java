package com.example.springtest.web;

import com.example.springtest.domain.posts.Posts;
import com.example.springtest.domain.posts.PostsRepository;
import com.example.springtest.service.PostsService;
import com.example.springtest.web.dto.PostsSaveRequestDto;
import com.example.springtest.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        // given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(title).content(content).author("author").build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정된다() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.of(2019,6,5,0,0,0);
        Posts savedPosts = postsRepository.save( Posts.builder().title("title").content("content").author("author").build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>>>>>> createDate="+posts.getCreatedAt()+", modifiedDate="+posts.getModifiedDate());
        assertThat(posts.getCreatedAt()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);


        Thread.sleep(1000);

        Long updatedId = savedPosts.getId();
        String expectedTitle = "updatedTitle";
        String expectedContent = "updatecContent";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(expectedTitle).content(expectedContent).build();
        String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;

        HttpEntity<PostsUpdateRequestDto> requestDtoHttpEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestDtoHttpEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);

        posts = postsList.get(0);
        System.out.println(">>>>>>>>>>>>>>> after updated. createDate="+posts.getCreatedAt()+", modifiedDate="+posts.getModifiedDate());
        assertThat(posts.getModifiedDate()).isNotEqualTo(posts.getCreatedAt());
    }

    @Test
    public void BaseTimeEntity_등록() throws InterruptedException {
        // given
        LocalDateTime now = LocalDateTime.of(2019,6,5,0,0,0);
        postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>>>>>> createDate="+posts.getCreatedAt()+", modifiedDate="+posts.getModifiedDate());
        assertThat(posts.getCreatedAt()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

    }
}
