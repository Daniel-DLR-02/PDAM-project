package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.repository.FilmRepository;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.ImgServiceStorage;
import com.pdam.tcl.utils.converters.FilmDtoConverter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FilmDtoConverter filmDtoConverter;
    private final ImgServiceStorage imgServiceStorage;
    private final SessionRepository sessionRepository;

    @Override
    public Film save(CreateFilmDto createFilm, MultipartFile file) throws Exception {

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        return filmRepository.save(Film.builder()
                .title(createFilm.getTitle())
                .poster(img.getData())
                .description(createFilm.getDescription())
                .duration(createFilm.getDuration())
                .releaseDate(createFilm.getReleaseDate())
                .expirationDate(createFilm.getExpirationDate())
                .genre(createFilm.getGenre())
                .build());

    }

    @Override
    public Optional<Film> findById(UUID id) {
        return filmRepository.findById(id);
    }

    @Override
    public Film getFilm(UUID id) {
        return filmRepository.getById(id);
    }

    @Override
    public void delete(UUID id) throws IOException {

        Optional<Film> peliculaABorrar = filmRepository.findById(id);
        if(peliculaABorrar.isPresent()) {
            if (peliculaABorrar.get().getPoster() != null) {

                imgServiceStorage.delete(peliculaABorrar.get().getPoster().getDeletehash());
            }
            // sessionRepository.getSessionsByFilmIdRaw(peliculaABorrar.get().getUuid()).forEach(sessionRepository::delete);

        }
        filmRepository.deleteById(id);
    }

    @Override
    public Film update(UUID id, CreateFilmDto createFilm, MultipartFile file) throws Exception {
        Film film = filmRepository.findById(id).orElseThrow(()-> new RuntimeException("Film not found"));

        imgServiceStorage.delete(film.getPoster().getDeletehash());

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        film.setTitle(createFilm.getTitle());
        film.setDescription(createFilm.getDescription());
        film.setDuration(createFilm.getDuration());
        film.setGenre(createFilm.getGenre());
        film.setPoster(img.getData());
        film.setExpirationDate(createFilm.getExpirationDate());
        film.setReleaseDate(createFilm.getReleaseDate());

        return filmRepository.save(film);
    }




    @Override
    public Page<GetFilmDto> getCurrentFilms(Pageable pageable) {
        Page<GetFilmDto> pageDto = filmRepository.findCurrentFilms(pageable).map((o)-> GetFilmDto.builder()
                .uuid(o.getUuid())
                .title(o.getTitle())
                .duration(o.getDuration())
                .genre(o.getGenre())
                .releaseDate(o.getReleaseDate())
                .description(o.getDescription())
                .poster(o.getPoster().split(",")[0])
                .build());

        return pageDto;
    }


}
