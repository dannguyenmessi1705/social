package com.didan.social.service;

import com.didan.social.dto.PostDTO;
import com.didan.social.dto.PostLikeDTO;
import com.didan.social.dto.UserCommentDTO;
import com.didan.social.dto.UserPostDTO;
import com.didan.social.entity.*;
import com.didan.social.entity.keys.UserPostId;
import com.didan.social.payload.request.CreatePostRequest;
import com.didan.social.payload.request.EditPostRequest;
import com.didan.social.repository.*;
import com.didan.social.service.impl.PostServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService implements PostServiceImpl {
    private final PostRepository postRepository;
    private final UserPostRepository userPostRepository;
    private final FileUploadsService fileUploadsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserCommentRepository userCommentRepository;
    private final HttpServletRequest request;
    @Autowired
    public PostService(PostRepository postRepository,
                       UserPostRepository userPostRepository,
                       FileUploadsService fileUploadsService,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       PostLikeRepository postLikeRepository,
                       UserCommentRepository userCommentRepository,
                       HttpServletRequest request
    ){
        this.postRepository = postRepository;
        this.userPostRepository = userPostRepository;
        this.fileUploadsService = fileUploadsService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
        this.userCommentRepository = userCommentRepository;
        this.request = request;
    }
    @Override
    public String createPost(CreatePostRequest createPostRequest) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        if (!StringUtils.hasText(createPostRequest.getTitle())
            || !StringUtils.hasText(createPostRequest.getBody())
        ){
            throw new Exception("Miss some fields");
        }
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Posts post = new Posts();
        UserPosts userPost = new UserPosts();
        UUID postId = UUID.randomUUID();
        post.setPostId(postId.toString());
        post.setTitle(createPostRequest.getTitle());
        if (!createPostRequest.getPostImg().isEmpty()){
            String fileName = fileUploadsService.storeFile(createPostRequest.getPostImg(), "post", postId.toString());
            post.setPostImg("post/"+fileName);
        }
        post.setBody(createPostRequest.getBody());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date nowSql = Timestamp.valueOf(now);
        post.setPostedAt(nowSql);
        userPost.setUserPostId(new UserPostId(postId.toString(), user.getUserId()));
        postRepository.save(post);
        userPostRepository.save(userPost);
        return postId.toString();
    }

    @Override
    public List<PostDTO> getAllPosts() throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Sort sortByPostedAt = Sort.by(Sort.Direction.DESC, "postedAt");
        List<Posts> posts = postRepository.findAll(sortByPostedAt);
        if (posts.size() <= 0) throw new Exception("No posts are here");
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Posts post : posts){
            PostDTO postDTO = new PostDTO();
            postDTO.setUserPosts(new UserPostDTO(user.getUserId(), post.getPostId()));
            postDTO.setTitle(post.getTitle());
            postDTO.setPostImg(post.getPostImg());
            postDTO.setBody(post.getBody());
            postDTO.setPostedAt(post.getPostedAt());
            List<PostLikes> postLikes = postLikeRepository.findByPosts_PostId(post.getPostId());
            List<PostLikeDTO> postLikeDTOs = new ArrayList<>();
            for (PostLikes postLike : postLikes){
                PostLikeDTO postLikeDTO = new PostLikeDTO();
                postLikeDTO.setUserId(postLike.getUsers().getUserId());
                postLikeDTOs.add(postLikeDTO);
            }
            postDTO.setPostLikes(postLikeDTOs);
            postDTO.setLikesQuantity(postLikes.size());
            List<UserComment> userComments = userCommentRepository.findByPosts_PostId(post.getPostId());
            postDTO.setCommentsQuantity(userComments.size());
            List<UserCommentDTO> userCommentDTOs = new ArrayList<>();
            for (UserComment userComment : userComments){
                UserCommentDTO userCommentDTO = new UserCommentDTO();
                userCommentDTO.setUserId(userComment.getUsers().getUserId());
                userCommentDTO.setCommentId(userComment.getComments().getCommentId());
                userCommentDTOs.add(userCommentDTO);
            }
            postDTO.setUserComment(userCommentDTOs);
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    @Override
    public PostDTO getPostById(String postId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Posts post = postRepository.findFirstByPostId(postId);
        if (post == null) throw new Exception("No post is here");
        PostDTO postDTO = new PostDTO();
        postDTO.setUserPosts(new UserPostDTO(user.getUserId(), post.getPostId()));
        postDTO.setTitle(post.getTitle());
        postDTO.setPostImg(post.getPostImg());
        postDTO.setBody(post.getBody());
        postDTO.setPostedAt(post.getPostedAt());
        List<PostLikes> postLikes = postLikeRepository.findByPosts_PostId(post.getPostId());
        List<PostLikeDTO> postLikeDTOs = new ArrayList<>();
        for (PostLikes postLike : postLikes){
            PostLikeDTO postLikeDTO = new PostLikeDTO();
            postLikeDTO.setUserId(postLike.getUsers().getUserId());
            postLikeDTOs.add(postLikeDTO);
        }
        postDTO.setPostLikes(postLikeDTOs);
        postDTO.setLikesQuantity(postLikes.size());
        List<UserComment> userComments = userCommentRepository.findByPosts_PostId(post.getPostId());
        postDTO.setCommentsQuantity(userComments.size());
        List<UserCommentDTO> userCommentDTOs = new ArrayList<>();
        for (UserComment userComment : userComments){
            UserCommentDTO userCommentDTO = new UserCommentDTO();
            userCommentDTO.setUserId(userComment.getUsers().getUserId());
            userCommentDTO.setCommentId(userComment.getComments().getCommentId());
            userCommentDTOs.add(userCommentDTO);
        }
        postDTO.setUserComment(userCommentDTOs);
        return postDTO;
    }

    @Override
    public List<PostDTO> getPostByTitle(String searchName) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        List<Posts> posts = postRepository.findByTitleOrBodyContainingOrderByPostedAtDesc(searchName, searchName);
        if (posts.size() <= 0) throw new Exception("No posts are here");
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Posts post : posts){
            PostDTO postDTO = new PostDTO();
            postDTO.setUserPosts(new UserPostDTO(user.getUserId(), post.getPostId()));
            postDTO.setTitle(post.getTitle());
            postDTO.setPostImg(post.getPostImg());
            postDTO.setBody(post.getBody());
            postDTO.setPostedAt(post.getPostedAt());
            List<PostLikes> postLikes = postLikeRepository.findByPosts_PostId(post.getPostId());
            List<PostLikeDTO> postLikeDTOs = new ArrayList<>();
            for (PostLikes postLike : postLikes){
                PostLikeDTO postLikeDTO = new PostLikeDTO();
                postLikeDTO.setUserId(postLike.getUsers().getUserId());
                postLikeDTOs.add(postLikeDTO);
            }
            postDTO.setPostLikes(postLikeDTOs);
            postDTO.setLikesQuantity(postLikes.size());
            List<UserComment> userComments = userCommentRepository.findByPosts_PostId(post.getPostId());
            postDTO.setCommentsQuantity(userComments.size());
            List<UserCommentDTO> userCommentDTOs = new ArrayList<>();
            for (UserComment userComment : userComments){
                UserCommentDTO userCommentDTO = new UserCommentDTO();
                userCommentDTO.setUserId(userComment.getUsers().getUserId());
                userCommentDTO.setCommentId(userComment.getComments().getCommentId());
                userCommentDTOs.add(userCommentDTO);
            }
            postDTO.setUserComment(userCommentDTOs);
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    @Override
    public boolean likePost(String postId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Posts post = postRepository.findFirstByPostId(postId);
        if (post==null) throw new Exception("No post is here");
        PostLikes postLikes = postLikeRepository.findByPosts_PostIdAndUsers_UserId(postId, user.getUserId());
        if (postLikes != null) throw new Exception("User liked post and do it twice");
        postLikes.setPosts(post);
        postLikes.setUsers(user);
        postLikeRepository.save(postLikes);
        return true;
    }

    @Override
    public boolean unlikePost(String postId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Posts post = postRepository.findFirstByPostId(postId);
        if (post==null) throw new Exception("No post is here");
        PostLikes postLikes = postLikeRepository.findByPosts_PostIdAndUsers_UserId(postId, user.getUserId());
        if (postLikes == null) throw new Exception("User hasn't liked post yet");
        postLikeRepository.delete(postLikes);
        return true;
    }

    @Override
    public PostDTO updatePost(String postId, EditPostRequest editPostRequest) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        UserPosts userPost = userPostRepository.findFirstByPosts_PostIdAndUsers_UserId(postId, user.getUserId());
        if (userPost == null) throw new Exception("The user hasn't this post or not authorized to edit this post");
        Posts post = userPost.getPosts();
        if (StringUtils.hasText(editPostRequest.getTitle())){
            post.setTitle(editPostRequest.getTitle());
        }
        if (StringUtils.hasText(editPostRequest.getBody())){
            post.setBody(editPostRequest.getBody());
        }
        if (!editPostRequest.getPostImg().isEmpty()){
            String fileName = fileUploadsService.storeFile(editPostRequest.getPostImg(), "post", postId.toString());
            post.setPostImg("post/"+fileName);
        }
        PostDTO postDTO = new PostDTO();
        postDTO.setUserPosts(new UserPostDTO(user.getUserId(), post.getPostId()));
        postDTO.setTitle(post.getTitle());
        postDTO.setPostImg(post.getPostImg());
        postDTO.setBody(post.getBody());
        postDTO.setPostedAt(post.getPostedAt());
        List<PostLikes> postLikes = postLikeRepository.findByPosts_PostId(post.getPostId());
        List<PostLikeDTO> postLikeDTOs = new ArrayList<>();
        for (PostLikes postLike : postLikes){
            PostLikeDTO postLikeDTO = new PostLikeDTO();
            postLikeDTO.setUserId(postLike.getUsers().getUserId());
            postLikeDTOs.add(postLikeDTO);
        }
        postDTO.setPostLikes(postLikeDTOs);
        postDTO.setLikesQuantity(postLikes.size());
        List<UserComment> userComments = userCommentRepository.findByPosts_PostId(post.getPostId());
        postDTO.setCommentsQuantity(userComments.size());
        List<UserCommentDTO> userCommentDTOs = new ArrayList<>();
        for (UserComment userComment : userComments){
            UserCommentDTO userCommentDTO = new UserCommentDTO();
            userCommentDTO.setUserId(userComment.getUsers().getUserId());
            userCommentDTO.setCommentId(userComment.getComments().getCommentId());
            userCommentDTOs.add(userCommentDTO);
        }
        postDTO.setUserComment(userCommentDTOs);
        postRepository.save(post);
        return postDTO;
    }

    @Override
    public boolean deletePost(String postId) throws Exception {
        try{
            String accessToken = jwtUtils.getTokenFromHeader(request);
            if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
            String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
            if (email == null) throw new Exception("Have some errors");
            Users user = userRepository.findFirstByEmail(email);
            if (user == null) throw new Exception("User is not found");
            Posts post = postRepository.findFirstByPostId(postId);
            if(post == null) throw new Exception("There isnt post to delete");
            UserPosts userPosts = userPostRepository.findFirstByPosts_PostIdAndUsers_UserId(postId, user.getUserId());
            if (userPosts == null) throw new Exception("The user hasn't this post or not authorized to edit this post");
            userPostRepository.delete(userPosts);
            postRepository.delete(post);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
