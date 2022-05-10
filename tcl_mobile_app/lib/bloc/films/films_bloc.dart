import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:http/retry.dart';
import 'package:http/http.dart' as http;
import 'package:http/retry.dart';

import '../../model/Films/film_response.dart';
import '../../repository/films_repository/films_repository.dart';
part 'films_event.dart';
part 'films_state.dart';

class FilmsBloc extends Bloc<FilmsEvent, FilmsState> {
  final FilmRepository filmRepository;

  FilmsBloc(this.filmRepository) : super(FilmsInitial()) {
    on<FetchFilm>(_filmsFetched);
  }

  void _filmsFetched(FetchFilm event, Emitter<FilmsState> emit) async {
    try {
      final films = await filmRepository.fetchFilms();
      emit(FilmsFetched(films));
      return;
    } on Exception catch (e) {
      emit(FilmFetchError(e.toString()));
    }
  }
}