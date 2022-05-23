import '../../model/Films/film_response.dart';
import '../../model/Films/single_film_response.dart';

abstract class FilmRepository {
  Future<List<Film>> fetchFilms();

  Future<FilmResponse> fetchFilmDetails(String uuid);
}
