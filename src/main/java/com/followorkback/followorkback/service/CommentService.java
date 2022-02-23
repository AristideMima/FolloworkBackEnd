package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Comment;
import com.followorkback.followorkback.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment saveComment(Comment comment);
    Comment updateComment(Comment comment);
    Boolean deleteComment(UUID uuid, String username);
    Comment getComment(UUID uuid, String username);
    Collection<Comment> getAllComments(int limit);
}
