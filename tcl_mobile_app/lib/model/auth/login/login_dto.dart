
class LoginDto {
  LoginDto({
    required this.nickname,
    required this.password,
  });
  late final String nickname;
  late final String password;

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nickname'] = nickname;
    _data['password'] = password;
    return _data;
  }
}