package com.bondoc.facebookapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<com.bondoc.facebookapp.Post, Long> {
}