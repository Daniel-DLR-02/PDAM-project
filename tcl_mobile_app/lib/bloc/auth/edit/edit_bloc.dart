
import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:tcl_mobile_app/repository/auth/auth_repository.dart';

import '../../../model/auth/edit/edit_dto.dart';
import '../../../model/auth/edit/edit_response.dart';

part 'edit_event.dart';
part 'edit_state.dart';

class EditBloc extends Bloc<EditEvent, EditState> {
  final AuthRepository authRepository;

  EditBloc(this.authRepository) : super(EditInitialState()) {
    on<DoEditEvent>(_doEditEvent);
  }

  void _doEditEvent(
      DoEditEvent event, Emitter<EditState> emit) async {
    try {
      final editResponse =
          await authRepository.edit(event.editDto, event.filePath);
      emit(EditSuccessState(editResponse));
      return;
    } on Exception catch (e) {
      emit(EditErrorState(e.toString()));
    }
  }
}
