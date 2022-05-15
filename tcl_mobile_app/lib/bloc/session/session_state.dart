part of 'session_bloc.dart';

abstract class SessionsState extends Equatable {
  const SessionsState();

  @override
  List<Object> get props => [];
}

class SessionsInitial extends SessionsState {}

class SessionsFetched extends SessionsState {
  final List<Session> sessions;

  const SessionsFetched(this.sessions);

  @override
  List<Object> get props => [sessions];
}

class SessionFetchError extends SessionsState {
  final String message;
  const SessionFetchError(this.message);

  @override
  List<Object> get props => [message];
}

class SessionSuccessFetched extends SessionsState {
  final SessionResponse session;

  const SessionSuccessFetched(this.session);

  @override
  List<Object> get props => [session];
}

class SessionErrorState extends SessionsState {
  final String message;

  const SessionErrorState(this.message);

  @override
  List<Object> get props => [message];
}
