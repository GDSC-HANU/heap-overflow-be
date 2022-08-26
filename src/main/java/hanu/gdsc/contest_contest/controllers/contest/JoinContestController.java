package hanu.gdsc.contest_contest.controllers.contest;

import hanu.gdsc.coderAuth.services.AuthorizeService;
import hanu.gdsc.contest_contest.services.contest.JoinContestService;
import hanu.gdsc.share.controller.ControllerHandler;
import hanu.gdsc.share.domains.Id;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "Contest" , description = "Rest-API endpoint for Contest")
public class JoinContestController {
    private final JoinContestService joinContestService;
    private final AuthorizeService authorizeService;

    @Operation(
            summary = "join contest",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Entity successfully created. Coder joined this contest.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "Success")
                            )
                    }
            ), @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content()}
            ), @ApiResponse(
                    responseCode = "400",
                    description = "invalid request",
                    content = {@Content()}
            )}
    )
    @PostMapping("/contest/joinContest/{coderId}/{contestId}")
    public ResponseEntity<?> joinContest(@PathVariable String coderId, @PathVariable String contestId) {
        return ControllerHandler.handle(() -> {
            joinContestService.joinContest(new Id(coderId), new Id(contestId));
            return new ControllerHandler.Result(
                    "Success",
                    null
            );
        });
    }

    @Operation(
            summary = "check coder joined contest or not",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Joined",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "false")
                            )
                    }
            ), @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content()}
            ), @ApiResponse(
                    responseCode = "400",
                    description = "invalid request",
                    content = {@Content()}
            )}
    )
    @GetMapping("/contest/checkIfCoderJoinContest/{coderId}")
    public ResponseEntity<?> CheckIfCoderJoinContest(@RequestHeader("access-token") String token, @PathVariable String contestId) {
        return ControllerHandler.handle(() -> {
            //should return page
            Id coderId = authorizeService.authorize(token);
            Boolean check = joinContestService.checkIfCoderJoinContest(coderId, new Id(contestId));
            return new ControllerHandler.Result(
                    "Success",
                    check
            );
        });
    }
}
