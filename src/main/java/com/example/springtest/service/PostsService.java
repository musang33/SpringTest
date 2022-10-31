package com.example.springtest.service;

import com.example.springtest.domain.posts.PostsRepository;
import com.example.springtest.web.dto.PostsListResponseDto;
import com.example.springtest.web.dto.PostsResponseDto;
import com.example.springtest.web.dto.PostsSaveRequestDto;
import com.example.springtest.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(final PostsSaveRequestDto requestDto) {
        return postsRepository.save( requestDto.toEntity() ).getId();
    }

    @Transactional
    public Long update(final Long id, final PostsUpdateRequestDto requestDto) {
        final var posts = postsRepository.findById( id ).orElseThrow( ()->new IllegalArgumentException("해당 게시글이 없습니다. id : " + id ) );

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById( final Long id ) {
        final var posts = postsRepository.findById( id ).orElseThrow( ()->new IllegalArgumentException("해당 게시글이 없습니다. id : " + id ) );
        return new PostsResponseDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository
                .findAll()
                .stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
//        return postsRepository
//                .findAllDesc()
//                .stream()
//                .map(PostsListResponseDto::new)
//                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(final Long id) {
        final var posts = postsRepository.findById(id).orElseThrow( ()->new IllegalArgumentException("해당 게시글이 없습니다.") );
        postsRepository.delete(posts);
        return id;
    }
}
