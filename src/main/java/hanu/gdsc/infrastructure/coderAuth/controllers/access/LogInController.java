package hanu.gdsc.infrastructure.coderAuth.controllers.access;

import hanu.gdsc.domain.coderAuth.services.access.LogInService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hanu.gdsc.infrastructure.share.controller.ResponseBody;
import hanu.gdsc.domain.share.exceptions.BusinessLogicException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Coder Auth" , description = "Rest-API endpoint for Coder Auth")
public class LogInController {
    @Autowired
    private LogInService logInService;

    @Schema(title = "LogIn", description = "Data transfer object to log in")
    public static class InputLogIn {
        @Schema(description = "specify the email or username of user to log in", example = "hihi@gmail.com", required = true)
        public String usernameOrEmail;
        @Schema(description = "specify the password to log in", example = "*********", required = true)
        public String password;
    }

    @PostMapping("/coderAuth/logIn")
    public ResponseEntity<?> logIn(@RequestBody InputLogIn input) {
        try {
            LogInService.Output output = logInService.logInService(input.usernameOrEmail, input.password);
            return new ResponseEntity<>(
                    new ResponseBody("Success", output),
                    HttpStatus.OK);
        } catch (Throwable e) {
            if(e instanceof BusinessLogicException) {
                e.printStackTrace();
                return new ResponseEntity<>(new ResponseBody(e.getMessage(), ((BusinessLogicException) e).getCode(), null), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ResponseBody(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
}
