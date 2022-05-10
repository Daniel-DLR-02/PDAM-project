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