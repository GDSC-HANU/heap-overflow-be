package hanu.gdsc.practiceProblem_problemDiscussion.controllers.post;

import hanu.gdsc.practiceProblem_problemDiscussion.services.post.SearchPostService;
import hanu.gdsc.share.controller.ControllerHandler;
import hanu.gdsc.share.domains.Id;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Practice Problem - Discussion Post", description = "Rest-API endpoint for Practice Problem Discussion")
public class SearchPostController {

    private final SearchPostService searchPostService;

    @Operation(
            summary = "Get the post of the practice problem by id",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Entity successfully found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SearchPostService.Output.class)
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
    @GetMapping("/practiceProblem/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable String id) {
        return ControllerHandler.handle(() -> {
            SearchPostService.Output output = searchPostService.getById(new Id(id));
            return new ControllerHandler.Result(
                    "Success",
                    output
            );
        });
    }

    @Operation(
            summary = "List posts of the practice problem",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Entity successfully found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SearchPostService.Output.class)
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
    @GetMapping("/practiceProblem/post")
    public ResponseEntity<?> getPosts(@RequestParam int page, @RequestParam int perPage,
                                      @RequestParam(required = false, name = "problemId") String problemId,
                                      @RequestParam(required = false, name = "coderId") String coderId) {
        return ControllerHandler.handle(() -> {
            Id problem = problemId == null ? null : new Id(problemId);
            Id coder = coderId == null ? null : new Id(coderId);
            List<SearchPostService.Output> output = searchPostService.getPosts(
                    problem,
                    coder,
                    page,
                    perPage
            );
            return new ControllerHandler.Result(
                    "Success",
                    output
            );
        });
    }
}