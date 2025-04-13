package com.cj.cgv.domain.movie;

import com.cj.cgv.domain.movie.dto.MovieReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movieRepository;

    @Transactional
    public Movie createMovie(MovieReq movieReq){
        return movieRepository.save(movieReq.toEntity());
    }

    public List<Movie> getMovieList(){
        return movieRepository.findAll();
    }

    @Transactional
    public void deleteMovie(Long movieId){
        movieRepository.deleteById(movieId);
    }
}
