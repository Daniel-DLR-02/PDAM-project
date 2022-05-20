package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.repository.FilmRepository;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.ImgServiceStorage;
import com.pdam.tcl.service.StorageService;
import com.pdam.tcl.utils.converters.FilmDtoConverter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final StorageService storageService;
    private final FilmDtoConverter filmDtoConverter;
    private final ImgServiceStorage imgServiceStorage;

    @Override
    public Film save(CreateFilmDto createFilm, MultipartFile file) throws Exception {

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        String uri = img.getData().getLink();
        String delete_hash_img = img.getData().getDeletehash();

        return filmRepository.save(Film.builder()
                .title(createFilm.getTitle())
                .poster(uri)
                .description(createFilm.getDescription())
                .duration(createFilm.getDuration())
                .releaseDate(createFilm.getReleaseDate())
                .expirationDate(createFilm.getExpirationDate())
                .genre(createFilm.getGenre())
                .build());

    }

    @Override
    public Optional<Film>    findById(UUID id) {
        return filmRepository.findById(id);
    }

    @Override
    public Film getFilm(UUID id) {
        return filmRepository.getById(id);
    }

    @Override
    public void delete(UUID id) throws IOException {

        Optional<Film> peliculaABorrar = filmRepository.findById(id);

        if((peliculaABorrar.isPresent()) &&
                (peliculaABorrar.get().getPoster() != null)){

            String filePathString = "./uploads/"+peliculaABorrar.get().getPoster().replace("http://localhost:8080/download/","");
            Path path = Paths.get(filePathString);

            storageService.deleteFile(path);
        }

        filmRepository.deleteById(id);
    }

    @Override
    public Film update(UUID id, CreateFilmDto createFilm, MultipartFile file) throws Exception {
        return null;
    }


    @Override
    public Page<GetFilmDto> getCurrentFilms(Pageable pageable) {
        return filmRepository.findCurrentFilms(pageable);
    }
}
