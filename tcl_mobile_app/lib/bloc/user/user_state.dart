part of 'user_bloc.dart';

abstract class UserState extends Equatable {
  const UserState();

  @override
  List<Object> get props => [];
}

class UserInitial extends UserState {}

class UserFetched extends UserState {
  final UserResponse user;

  const UserFetched(this.user);

  @override
  List<Object> get props => [user];
}

class UserFetchError extends UserState {
  final String message;
  const UserFetchError(this.message);

  @override
  List<Object> get props => [message];
}
