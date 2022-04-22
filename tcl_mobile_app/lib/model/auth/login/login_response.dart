class LoginResponse {
  LoginResponse({
    required this.nickName,
    required this.avatar,
    required this.token,
    required this.email,

  });
  late final String nickName;
  late final String avatar;
  late final String token;
  late final String email;

  LoginResponse.fromJson(Map<String, dynamic> json) {
    email = json['email'];
    nickName = json['nickName'];
    avatar = json['avatar'];
    token = json['token'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['email'] = email;
    _data['nickName'] = nickName;
    _data['avatar'] = avatar;
    _data['token'] = token;
    return _data;
  }
}