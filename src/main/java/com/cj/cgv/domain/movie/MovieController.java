package com.cj.cgv.domain.movie;

import com.cj.cgv.domain.movie.dto.MovieReq;
import com.cj.cgv.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cj.cgv.global.common.StatusCode.*;

@Tag(name = "[영화]", description = "영화 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "영화 생성", description = "1개의 영화를 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<Movie>> createMarket(
            @RequestBody MovieReq movieReq){
        return ResponseEntity
                .status(MOVIE_CREATE.getStatus())
                .body(CommonResponse.from(MOVIE_CREATE.getMessage()
                        ,movieService.createMovie(movieReq)));
    }

    @Operation(summary = "영화 조회", description = "모든 영화를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<Movie>>> getMarketList(){
        return ResponseEntity
                .status(MOVIE_FOUND.getStatus())
                .body(CommonResponse.from(MOVIE_FOUND.getMessage()
                        ,movieService.getMovieList()));
    }

    @Operation(summary = "영화 삭제", description = "해당 영화를 삭제합니다.")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Object> deleteMarket(@PathVariable Long movieId){
        movieService.deleteMovie(movieId);
        return ResponseEntity
                .status(MOVIE_DELETE.getStatus())
                .body(CommonResponse.from(MOVIE_DELETE.getMessage()));
    }
}
