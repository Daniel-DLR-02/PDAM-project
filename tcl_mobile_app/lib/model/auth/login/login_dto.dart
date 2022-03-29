
class LoginDto {
  LoginDto({
    required this.nickName,
    required this.password,
  });
  late final String nickName;
  late final String password;

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nickName'] = nickName;
    _data['password'] = password;
    return _data;
  }
}