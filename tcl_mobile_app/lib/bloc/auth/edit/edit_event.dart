part of 'edit_bloc.dart';

abstract class EditEvent extends Equatable {
  const EditEvent();

  @override
  List<Object> get props => [];
}

class DoEditEvent extends EditEvent {
  final EditDto editDto;
  final String filePath;

  const DoEditEvent(this.editDto, this.filePath);
}
