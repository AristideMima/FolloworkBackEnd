package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findById(long id);
}
