package com.csci3130.group04.Daltweets.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;
import com.csci3130.group04.Daltweets.utils.PostResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostComment;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.FollowersService;
import com.csci3130.group04.Daltweets.service.PostCommentService;
import com.csci3130.group04.Daltweets.service.PostService;
import com.csci3130.group04.Daltweets.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired 
    PostCommentService postCommentService;

    @Autowired
    UserServiceImplementation userService;

    @Autowired
    FollowersService followersService;
    @Autowired
    GroupServiceImpl groupService;

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        if (createdPost == null) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request if user is null
        }
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{username}/all")
    List<PostResponseDTO> getAllPosts(@PathVariable("username") String username) {
        User user = userService.getUserByName(username);
        List<User> following = followersService.getUserFollowing(user);
        List<Post> posts = postService.getPostsByUsers(following);
        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();
        for (Post post: posts)
        {
            int commentCount = postCommentService.getCommentCount(post);
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(post.getPostID());
            postResponseDTO.setCreator(post.getUser().getUsername());
            postResponseDTO.setText(post.getText());
            postResponseDTO.setDateCreated(post.getDateCreated());
            postResponseDTO.setCommentCount(commentCount);
            postResponseDTOs.add(postResponseDTO);
        }

        return postResponseDTOs;
    }
    @GetMapping("/{groupname}/groupPosts")
    ResponseEntity<List<PostResponseDTO>> getAllGroupPosts(@PathVariable("groupname") String groupname ) {
        if ( groupname == null ) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<User> groupmembers = groupService.getGroupMembers(groupname);
        if ( groupmembers == null ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        List<Post> groupposts = postService.getPostsByUsers(groupmembers);
        List<PostResponseDTO> postResponseDTOs = new ArrayList<>();

        for (Post post: groupposts)
        {
            int commentCount = postCommentService.getCommentCount(post);
            PostResponseDTO postResponseDTO = new PostResponseDTO();
            postResponseDTO.setId(post.getPostID());
            postResponseDTO.setCreator(post.getUser().getUsername());
            postResponseDTO.setText(post.getText());
            postResponseDTO.setDateCreated(post.getDateCreated());
            postResponseDTO.setCommentCount(commentCount);
            postResponseDTOs.add(postResponseDTO);
        }

        return new ResponseEntity<>(postResponseDTOs,HttpStatus.OK);
    }

    @PostMapping("/comment/create")
    ResponseEntity<PostComment> createPostComment(@RequestBody Map<String, String> requestBody)
    {
        int postId = Integer.parseInt(requestBody.get("postId"));
        String username = requestBody.get("username");
        String comment = requestBody.get("comment");
        if (postId < 0 || username == null || comment == null ) 
        {
            return ResponseEntity.badRequest().body(null);
        }

        Post post = postService.getPostById(postId);
        User user = userService.getUserByName(username);

        if (post == null || user == null)
        {
            return ResponseEntity.notFound().build();
        }

        PostComment createdPostComment = postCommentService.createPostComment(post,user,comment);
        return ResponseEntity.ok().body(createdPostComment);
    } 

    @GetMapping("/comment/get/{postId}")
    ResponseEntity<List<PostComment>> getPostComments(@PathVariable("postId") int postId ){
        if (postId < 0)
        {
            return ResponseEntity.badRequest().body(null);
        }

        Post post = postService.getPostById(postId);

        List<PostComment> postComments = postCommentService.getPostComments(post);

        if (postComments == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok().body(postComments);
    }

}
