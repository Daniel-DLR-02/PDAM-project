package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.repository.FilmRepository;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.repository.TicketRepository;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.ImgServiceStorage;
import com.pdam.tcl.service.SessionService;
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
    private final ImgServiceStorage imgServiceStorage;
    private final SessionService sessionService;
    private final FilmDtoConverter filmDtoConverter;


    @Override
    public Film save(CreateFilmDto createFilm, MultipartFile file) throws Exception {

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        return filmRepository.save(filmDtoConverter.createFilmToFilm(createFilm,img));

    }

    @Override
    public Page<GetFilmDto> getAllFilms(Pageable pageable){
        Page<GetFilmDto> pageDto = filmRepository.getAllFilms(pageable).map((o)-> GetFilmDto.builder()
                .uuid(o.getUuid())
                .title(o.getTitle())
                .duration(o.getDuration())
                .genre(o.getGenre())
                .expirationDate(o.getExpirationDate())
                .releaseDate(o.getReleaseDate())
                .description(o.getDescription())
                .poster(o.getPoster().split(",")[0])
                .build());

        return pageDto;
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

            sessionService.deleteAllSessionsByFilmId(peliculaABorrar.get().getUuid());

        }
        filmRepository.deleteById(id);
    }

    @Override
    public Film update(UUID id, CreateFilmDto editFilm, MultipartFile file) throws Exception {
        Film film = filmRepository.findById(id).orElseThrow(()-> new RuntimeException("Film not found"));

        imgServiceStorage.delete(film.getPoster().getDeletehash());

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        film.setTitle(editFilm.getTitle());
        film.setDescription(editFilm.getDescription());
        film.setDuration(editFilm.getDuration());
        film.setGenre(editFilm.getGenre());
        film.setPoster(img.getData());
        film.setExpirationDate(editFilm.getExpirationDate());
        film.setReleaseDate(editFilm.getReleaseDate());

        return filmRepository.save(film);
    }

    @Override
    public Film updateNoAvatar(UUID id, CreateFilmDto editFilm) {
        Film film = filmRepository.findById(id).orElseThrow(()-> new RuntimeException("Film not found"));

        film.setTitle(editFilm.getTitle());
        film.setDescription(editFilm.getDescription());
        film.setDuration(editFilm.getDuration());
        film.setGenre(editFilm.getGenre());
        film.setExpirationDate(editFilm.getExpirationDate());
        film.setReleaseDate(editFilm.getReleaseDate());

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
                .expirationDate(o.getExpirationDate())
                .description(o.getDescription())
                .poster(o.getPoster().split(",")[0])
                .build());

        return pageDto;
    }


}
