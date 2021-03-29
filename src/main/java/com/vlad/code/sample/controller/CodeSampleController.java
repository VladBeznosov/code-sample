package com.vlad.code.sample.controller;

import com.vlad.code.sample.exception.DuplicateUserException;
import com.vlad.code.sample.exception.InvalidEmailException;
import com.vlad.code.sample.exception.UserNotFoundException;
import com.vlad.code.sample.service.CodeSampleService;
import com.vlad.code.sample.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Log4j2
public class CodeSampleController {

    private final CodeSampleService codeSampleService;

    public CodeSampleController(CodeSampleService codeSampleService) {
        this.codeSampleService = codeSampleService;
    }

    @ApiOperation(
            value = "Create a new user.",
            notes = "New user will be created from the information in given request object. " +
                    "Newly created User instance will be returned to the consumer of this end point.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createNewUser(
            @ApiParam(value = "Payload containing new User information", required = true)
            @RequestBody User user) {
        log.info("Received a request to create a new user");
        try {
            return new ResponseEntity<>(codeSampleService.createNewUser(user), HttpStatus.CREATED);
        } catch (DuplicateUserException | InvalidEmailException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "Get all users.",
            notes = "This end point will return a list of all users registered in the application.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Received a request to get a User by email");
        return new ResponseEntity<>(codeSampleService.getAllUsers(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get user by email.",
            notes = "This end point will return a user with specified email. " +
                    "If specified email is not registered in the application a 404 exception will be produced.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(path = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByEmail(
            @ApiParam(value = "The email address of the user.", required = true)
            @RequestParam(name = "email") String email) {
        log.info("Received a request to get a User by email");
        try {
            return new ResponseEntity<>(codeSampleService.getUserByEmail(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error("Attempt to find a User with email {} failed due to the following error: {}", email, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(
            value = "Remove User with specified id.",
            notes = "This end point will attempt to delete a user with specified id. " +
                    "If specified id is not registered in the application a 404 exception will be produced, " +
                    "otherwise a deleted User instance will be returned to the consumer of this end point.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> removeUserById(
            @ApiParam(value = "ID of the user to be deleted", required = true)
            @PathVariable(name = "id") String id) {
        log.info("Received request to delete user with id: {}", id);
        try {
            return new ResponseEntity<>(codeSampleService.removeUserById(id), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error("Attempt to delete a User with id {} failed due to the following error: {}", id, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
