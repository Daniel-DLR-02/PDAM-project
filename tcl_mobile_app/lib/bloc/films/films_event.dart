part of 'films_bloc.dart';

abstract class FilmsEvent extends Equatable {
  const FilmsEvent();

  @override
  List<Object> get props => [];
}

class FetchFilm extends FilmsEvent {
  const FetchFilm();

  @override
  List<Object> get props => [];
}

class GetFilmDetails extends FilmsEvent {
  final String filmUuid;

  const GetFilmDetails(this.filmUuid);

}