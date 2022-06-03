import '../../model/User/user_response.dart';

abstract class UserRepository {
  Future<UserResponse> me();
}
