part of 'session_bloc.dart';

abstract class SessionsEvent extends Equatable {
  const SessionsEvent();

  @override
  List<Object> get props => [];
}

class FetchFilmSession extends SessionsEvent {
  final String filmUuid;

  const FetchFilmSession(this.filmUuid);

  @override
  List<Object> get props => [];
}

class GetSessionDetails extends SessionsEvent {
  final String sessionUuid;

  const GetSessionDetails(this.sessionUuid);
}
