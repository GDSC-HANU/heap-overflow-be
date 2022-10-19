package hanu.gdsc.coderAuth.controllers;

import hanu.gdsc.coderAuth.domains.User;
import hanu.gdsc.coderAuth.services.AuthorizeService;
import hanu.gdsc.coderAuth.services.SearchUserService;
import hanu.gdsc.share.controller.ControllerHandler;
import hanu.gdsc.share.domains.Id;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "CoderAuth", description = "Rest-API endpoint for CoderAuth")
public class SearchUserController {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private AuthorizeService authorizeService;

    @AllArgsConstructor
    public static class OutputUser {
        public String username;
        public String email;
    }

    @GetMapping("/coderAuth")
    public ResponseEntity<?> getByCoderId(@RequestHeader("access-token") String token) {
        return ControllerHandler.handle(() -> {
            Id coderId = authorizeService.authorize(token);
            User user = searchUserService.getByCoderId(coderId);
            return new ControllerHandler.Result(
                    "Success",
                    new OutputUser(
                            user.getUsername().toString(),
                            user.getEmail().toString()
                    )
            );
        });
    }
}
