import '../../model/Films/film_response.dart';

abstract class FilmRepository {
  Future<List<Film>> fetchFilms();
}