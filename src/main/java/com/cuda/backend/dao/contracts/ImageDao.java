package com.cuda.backend.dao.contracts;

import java.util.Optional;
import java.util.Set;

import com.cuda.backend.entities.Image;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;

public interface ImageDao {
    void add(Image image)throws Exception;

    Set<Image> getImageSetForTweet(Long commentID)throws Exception;

    Optional<Image> getImageForUserProfile(Long userID)throws Exception;

    Image updateImageForUserProfile(Image image,User user)throws Exception;

    Set<Image> updateImageForTweet(Set<Image> images,Tweet comment)throws Exception;

    void deleteImageForComment(Set<Image> images,Tweet comment)throws Exception;
    
    void deleteImageForUserProfile(User user)throws Exception;
}
