part of 'edit_bloc.dart';

abstract class EditState extends Equatable {
  const EditState();

  @override
  List<Object> get props => [];
}

class EditInitialState extends EditState {}

class EditLoadingState extends EditState {}

class EditSuccessState extends EditState {
  final EditResponse editResponse;

  const EditSuccessState(this.editResponse);

  @override
  List<Object> get props => [editResponse];
}

class EditErrorState extends EditState {
  final String message;

  const EditErrorState(this.message);

  @override
  List<Object> get props => [message];
}
