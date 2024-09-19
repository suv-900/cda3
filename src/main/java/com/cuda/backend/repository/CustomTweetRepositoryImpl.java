package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.dto.TweetDTO;
import com.cuda.backend.entities.dto.UserDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CustomTweetRepositoryImpl implements CustomTweetRepository{
    private final int batchSize = 5;

    @Autowired
    private SessionFactory sessionFactory;

    public Optional<Tweet> getByIdLoadGraph(Long tweetId){
        Assert.notNull(tweetId,"tweet id cannot be null");

        Session session = sessionFactory.openSession();
        RootGraph<Tweet> graph = session.createEntityGraph(Tweet.class);
        
        graph.addAttributeNode("author");
        graph.addAttributeNode("replies");
        graph.addSubGraph("replies").addAttributeNode("author");
        
        Optional<Tweet> tweet = session.byId(Tweet.class).withFetchGraph(graph).loadOptional(tweetId);

        session.close();

        return tweet;
    }
    
    public TweetDTO read(Long tweetId){
        String hql = """
        select new TweetDTO(t.id,t.tweet,
        new UserDTO(t.author.id,t.author.username,t.author.nickname,t.author.active,
        t.author.bio,t.author.followerCount,t.author.followingCount,t.author.tweetCount),
        t.likeCount,t.viewCount,t.updatedAt,false) from Tweet t where t.id = :tweetId
        """;
        
        Session session = sessionFactory.openSession();
        TweetDTO tweetDto = session.createSelectionQuery(hql,TweetDTO.class)
            .setParameter("tweetId",tweetId)
            .getSingleResultOrNull();
        session.close();

        return tweetDto;
    }

    public List<TweetDTO> getTweetReplies(Long parentTweetId,int pageCount,int pageSize){
        Assert.notNull(parentTweetId,"parent tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = """
        select new TweetDTO(t.id,t.tweet,new UserDTO(t.author.id,t.author.username,t.author.nickname,t.author.active
        ,t.author.bio,t.author.followerCount,t.author.followingCount,t.author.tweetCount),
        t.likeCount,t.viewCount,t.updatedAt,false)
        from Tweet t where t.parentTweet is not null and t.parentTweet.id = :parentTweetId
        order by t.likeCount desc
        """;
          
        Session session = sessionFactory.openSession();
        List<TweetDTO> tweets = session.createSelectionQuery(hql,TweetDTO.class)
            .setParameter("parentTweetId",parentTweetId)
            .setFirstResult(pageCount * pageSize)
            .setMaxResults(pageSize)
            .getResultList();

        session.close();

        return tweets;
    }

    public List<TweetDTO> getTweetRepliesWithUserReactions(Long parentTweetId,Long userId,int pageCount,int pageSize){
        String hql = """
        select new TweetDTO(t.id,t.tweet,new UserDTO(t.author.id,t.author.username,t.author.nickname,t.author.active
        ,t.author.bio,t.author.followerCount,t.author.followingCount,t.author.tweetCount),
        t.likeCount,t.viewCount,t.updatedAt,l.user is not null and l.user.id = :userId)
        from Tweet t left join Like l on l.tweet.id = t.id 
        where t.parentTweet is not null and t.parentTweet.id = :parentTweetId
        order by t.likeCount desc
        """;
       
        Session session = sessionFactory.openSession();
        List<TweetDTO> tweets = session.createSelectionQuery(hql,TweetDTO.class)
            .setParameter("userId",userId)
            .setParameter("parentTweetId",parentTweetId)
            .setFirstResult(pageCount * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        session.close();

        return tweets;
    }

    @Transactional
    public void increaseLikeCount(Long tweetId){
        String hql = "update Tweet t set t.likeCount = t.likeCount + 1 where t.id = :tweetId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();

        session.createMutationQuery(hql)
            .setParameter("tweetId",tweetId)
            .executeUpdate();

        tc.commit();
        session.close();
    }
   
    @Transactional
    public void decreaseLikeCount(Long tweetId){
        String hql = "update Tweet t set t.likeCount = t.likeCount - 1 where t.id = :tweetId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("tweetId",tweetId)
            .executeUpdate();
        tc.commit();
        session.close();
    }

    public List<TweetDTO> getUserTweetsMostLiked(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = """
        select new TweetDTO(t.id,t.tweet,null,t.likeCount,t.viewCount,t.updatedAt)
        from Tweet t where t.author.id = :authorId order by t.likeCount desc
        """;

        Session session = sessionFactory.openSession();
        Query<TweetDTO> query = session.createQuery(hql,TweetDTO.class);
        
        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<TweetDTO> tweets = query.getResultList();
        session.close();

        return tweets;
    }

    public List<TweetDTO> getUserTweetsOldest(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = """
        select new TweetDTO(t.id,t.tweet,null,t.likeCount,t.viewCount,t.updatedAt)
        from Tweet t where t.author.id = :authorId order by t.createdAt asc 
        """;
                
        Session session = sessionFactory.openSession();
        Query<TweetDTO> query = session.createQuery(hql,TweetDTO.class);

        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<TweetDTO> tweets = query.getResultList();
        session.close();

        return tweets;
    }
    public List<TweetDTO> getUserTweetsNewest(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        Assert.isTrue(pageCount < 50,"page count cannot be greater than 50");

        String hql = """
        select new TweetDTO(t.id,t.tweet,null,t.likeCount,t.viewCount,t.updatedAt)
        from Tweet t where t.author.id = :authorId order by t.createdAt desc        
        """;
        Session session = sessionFactory.openSession();
        Query<TweetDTO> query = session.createQuery(hql,TweetDTO.class);

        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<TweetDTO> tweets = query.getResultList();
        session.close();

        return tweets;
    }
    public List<UserDTO> getUsersWhoLikedTweet(Long tweetId,int pageNumber,int pageSize){
        Assert.notNull(tweetId,"tweet id cannot be null");
        Assert.notNull(pageNumber,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        
        String hql = """
        select new UserDTO(u.id,u.username,u.nickname,u.active,u.bio,u.followerCount
        ,u.followingCount,u.tweetCount) 
        from Tweet t join t.userLikes u 
        where t.id = :tweetId """;
        
        Session session = sessionFactory.openSession();
        Query<UserDTO> query = session.createQuery(hql,UserDTO.class);
    
        query.setParameter("tweetId",tweetId);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<UserDTO> users = query.getResultList();
        session.close();

        return users;
    }
    
    public void insertInBatch(Iterable<Tweet> tweets){
        Session session = sessionFactory.openSession();
        int count = 0;
        for(Tweet tweet : tweets){
            if(count % batchSize == 0 ){
                session.flush();
                session.clear();
            }
            
            session.persist(tweet);
            count++; 
        }
        session.flush();
    }
}
