part of 'films_bloc.dart';

abstract class FilmsState extends Equatable {
  const FilmsState();

  @override
  List<Object> get props => [];
}

class FilmsInitial extends FilmsState {}

class FilmsFetched extends FilmsState {
  final List<Film> films;

  const FilmsFetched(this.films);

  @override
  List<Object> get props => [films];
}

class FilmFetchError extends FilmsState {
  final String message;
  const FilmFetchError(this.message);

  @override
  List<Object> get props => [message];
}

class FilmSuccessFetched extends FilmsState {
  final FilmResponse film;

  const FilmSuccessFetched(this.film);

  @override
  List<Object> get props => [film];
}

class FilmErrorState extends FilmsState {
  final String message;

  const FilmErrorState(this.message);

  @override
  List<Object> get props => [message];
}
