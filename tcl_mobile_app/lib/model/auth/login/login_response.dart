class LoginResponse {
  LoginResponse(
      {required this.nickname,
      required this.avatar,
      required this.token,
      required this.email,
      required this.role
      });
  late final String nickname;
  late final dynamic avatar;
  late final String token;
  late final String email;
  late final String role;

  LoginResponse.fromJson(Map<String, dynamic> json) {
    email = json['email'];
    role = json['role'];
    nickname = json['nickname'];
    avatar = json['avatar'];
    token = json['token'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['email'] = email;
    _data['role'] = role;
    _data['nickname'] = nickname;
    _data['avatar'] = avatar;
    _data['token'] = token;
    return _data;
  }
}
