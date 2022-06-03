import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:tcl_mobile_app/model/User/user_response.dart';
import 'package:tcl_mobile_app/repository/user_repository/user_repository_impl.dart';

part 'user_state.dart';
part 'user_event.dart';

class UserBloc extends Bloc<UserEvent, UserState> {
  final UserRepository userRepository;

  UserBloc(this.userRepository) : super(UserInitial()) {
    on<FetchUser>(_usersFetched);
  }

  void _usersFetched(FetchUser event, Emitter<UserState> emit) async {
    try {
      final user = await userRepository.me();
      return;
    } on Exception catch (e) {
      emit(UserFetchError(e.toString()));
    }
  }
}
