
import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:tcl_mobile_app/model/Sessions/session_response.dart';
import 'package:tcl_mobile_app/model/Sessions/single_session_response.dart';

import '../../repository/session_repository/session_repository.dart';

part 'session_event.dart';
part 'session_state.dart';

class SessionsBloc extends Bloc<SessionsEvent, SessionsState> {
  final SessionRepository sessionRepository;

  SessionsBloc(this.sessionRepository) : super(SessionsInitial()) {
    on<FetchFilmSession>(_filmSessionsFetched);
    on<GetSessionDetails>(_sessionDetailsFetched);
  }

  void _filmSessionsFetched(FetchFilmSession event, Emitter<SessionsState> emit) async {
    try {
      final sessions = await sessionRepository.fetchFilmSessions(event.filmUuid);
      emit(SessionsFetched(sessions));
      return;
    } on Exception catch (e) {
      emit(SessionFetchError(e.toString()));
    }
  }

  void _sessionDetailsFetched(
      GetSessionDetails event, Emitter<SessionsState> emit) async {
    try {
      final session = await sessionRepository.fetchSessionDetails(event.sessionUuid);
      emit(SessionSuccessFetched(session));
      return;
    } on Exception catch (e) {
      emit(SessionErrorState(e.toString()));
    }
  }
}
