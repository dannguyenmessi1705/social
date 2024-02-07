package com.didan.social.service;

import com.didan.social.dto.CommentDTO;
import com.didan.social.dto.PostDTO;
import com.didan.social.entity.*;
import com.didan.social.entity.keys.CommentLikeId;
import com.didan.social.entity.keys.UserCommentId;
import com.didan.social.entity.keys.UserPostId;
import com.didan.social.payload.request.CreateCommentRequest;
import com.didan.social.payload.request.EditCommentRequest;
import com.didan.social.repository.CommentLikeRepository;
import com.didan.social.repository.CommentRepository;
import com.didan.social.repository.UserCommentRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.CommentServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService implements CommentServiceImpl {
    private final UserCommentRepository userCommentRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FileUploadsService fileUploadsService;
    private final HttpServletRequest request;
    @Autowired
    public CommentService(UserCommentRepository userCommentRepository,
                          CommentRepository commentRepository,
                          CommentLikeRepository commentLikeRepository,
                          JwtUtils jwtUtils,
                          UserRepository userRepository,
                          FileUploadsService fileUploadsService,
                          HttpServletRequest request){
        this.userCommentRepository = userCommentRepository;
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.fileUploadsService = fileUploadsService;
        this.request = request;
    }

    @Override
    public List<CommentDTO> getCommentsInPost(String postId) throws Exception {
        List<UserComment> userComments = userCommentRepository.findByPosts_PostId(postId);
        if(userComments == null) return null;
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (UserComment userComment : userComments){
            Comments comment = commentRepository.findByCommentId(userComment.getComments().getCommentId());
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setUserComments(userComment.getUsers().getUserId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCommentImg(comment.getCommentImg());
            commentDTO.setCommentAt(comment.getCommentAt());
            List<CommentLikes> commentLikes = commentLikeRepository.findAllByComments_CommentId(comment.getCommentId());
            commentDTO.setCommentLikes(commentLikes.size());
            List<String> userLikes = commentLikes.stream().map(commentLike -> commentLike.getUsers().getUserId()).collect(Collectors.toList());
            commentDTO.setUserLikes(userLikes);
            commentDTOs.add(commentDTO);
        }
        Collections.sort(commentDTOs, Comparator.comparing(CommentDTO::getCommentAt).reversed());
        return commentDTOs;
    }

    @Override
    public String postCommentInPost(String postId, CreateCommentRequest createCommentRequest) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        if (!StringUtils.hasText(createCommentRequest.getContent())){
            throw new Exception("Miss some fields");
        }
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Comments comment = new Comments();
        UserComment userComment = new UserComment();
        UUID commentId = UUID.randomUUID();
        comment.setCommentId(commentId.toString());
        comment.setContent(createCommentRequest.getContent());
        if (createCommentRequest.getCommentImg() != null && !createCommentRequest.getCommentImg().isEmpty()){
            String fileName = fileUploadsService.storeFile(createCommentRequest.getCommentImg(), "comment", commentId.toString());
            comment.setCommentImg("comment/"+fileName);
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date nowSql = Timestamp.valueOf(now);
        comment.setCommentAt(nowSql);
        userComment.setUserCommentId(new UserCommentId(postId, commentId.toString(), user.getUserId()));
        commentRepository.save(comment);
        userCommentRepository.save(userComment);
        return commentId.toString();
    }

    @Override
    public CommentDTO getCommentById(String commentId) throws Exception {
        UserComment userComment = userCommentRepository.findFirstByComments_CommentId(commentId);
        if (userComment == null) return null;
        Comments comment = commentRepository.findByCommentId(userComment.getComments().getCommentId());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setUserComments(userComment.getUsers().getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCommentImg(comment.getCommentImg());
        commentDTO.setCommentAt(comment.getCommentAt());
        List<CommentLikes> commentLikes = commentLikeRepository.findAllByComments_CommentId(comment.getCommentId());
        commentDTO.setCommentLikes(commentLikes.size());
        List<String> userLikes = commentLikes.stream().map(commentLike -> commentLike.getUsers().getUserId()).collect(Collectors.toList());
        commentDTO.setUserLikes(userLikes);
        return commentDTO;
    }

    @Override
    public boolean likeComment(String commentId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Comments comment = commentRepository.findByCommentId(commentId);
        if (comment == null) throw new Exception("No comment is here");
        CommentLikes commentLike = commentLikeRepository.findFirstByUsers_UserIdAndComments_CommentId(user.getUserId(), commentId);
        if (commentLike != null) throw new Exception("User liked comment and cannot do it twice");
        CommentLikes newCommentLike = new CommentLikes();
        newCommentLike.setCommentLikeId(new CommentLikeId(commentId, user.getUserId()));
        newCommentLike.setUsers(user);
        newCommentLike.setComments(comment);
        commentLikeRepository.save(newCommentLike);
        return true;
    }

    @Override
    public boolean unlikeComment(String commentId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Comments comment = commentRepository.findByCommentId(commentId);
        if (comment == null) throw new Exception("No comment is here");
        CommentLikes commentLike = commentLikeRepository.findFirstByUsers_UserIdAndComments_CommentId(user.getUserId(), commentId);
        if (commentLike == null) throw new Exception("User hasn't liked post yet");
        commentLikeRepository.delete(commentLike);
        return true;
    }

    @Override
    public CommentDTO updateComment(String commentId, EditCommentRequest editCommentRequest) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        UserComment userComment = userCommentRepository.findFirstByUsers_UserIdAndComments_CommentId(user.getUserId(), commentId);
        if (userComment == null) throw new Exception("The user hasn't this comment or not authorized to edit this comment");
        Comments comment = userComment.getComments();
        if (StringUtils.hasText(editCommentRequest.getContent())){
            comment.setContent(editCommentRequest.getContent());
        }
        if (editCommentRequest.getCommentImg()!= null && !editCommentRequest.getCommentImg().isEmpty()){
            String fileName = fileUploadsService.storeFile(editCommentRequest.getCommentImg(), "comment", commentId);
            comment.setCommentImg("comment/"+fileName);
        }
        commentRepository.save(comment);
        PostDTO postDTO = new PostDTO();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setUserComments(userComment.getUsers().getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCommentImg(comment.getCommentImg());
        commentDTO.setCommentAt(comment.getCommentAt());
        List<CommentLikes> commentLikes = commentLikeRepository.findAllByComments_CommentId(comment.getCommentId());
        commentDTO.setCommentLikes(commentLikes.size());
        List<String> userLikes = commentLikes.stream().map(commentLike -> commentLike.getUsers().getUserId()).collect(Collectors.toList());
        commentDTO.setUserLikes(userLikes);
        return commentDTO;
    }

    @Override
    public boolean deleteComment(String commentId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        Comments comment = commentRepository.findByCommentId(commentId);
        if(comment == null) throw new Exception("There isnt comment to delete");
        UserComment userComment = userCommentRepository.findFirstByUsers_UserIdAndComments_CommentId(user.getUserId(), commentId);
        if (userComment == null) throw new Exception("The user hasn't this comment or not authorized to edit this comment");
        userCommentRepository.delete(userComment);
        if(StringUtils.hasText(comment.getCommentImg())){
            fileUploadsService.deleteFile(comment.getCommentImg());
        }
        commentRepository.delete(comment);
        return true;
    }
}
