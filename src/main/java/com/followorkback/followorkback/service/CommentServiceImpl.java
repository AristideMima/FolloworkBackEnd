package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Comment;
import com.followorkback.followorkback.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRespository;

    @Override
    public Comment saveComment(Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        return null;
    }

    @Override
    public Boolean deleteComment(UUID uuid, String username) {
        return null;
    }

    @Override
    public Comment getComment(UUID uuid, String username) {
        return null;
    }

    @Override
    public Collection<Comment> getAllComments(int limit) {
        return null;
    }
}
