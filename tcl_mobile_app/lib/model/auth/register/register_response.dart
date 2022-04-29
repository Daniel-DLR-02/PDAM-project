class RegisterResponse {
  RegisterResponse({
    required this.nickName,
    required this.nombre,
    required this.fechaDeNacimiento,
    required this.email,
    required this.avatar,
  });
  late final String nickName;
  late final String nombre;
  late final String fechaDeNacimiento;
  late final String email;
  late final String avatar;

  RegisterResponse.fromJson(Map<String, dynamic> json) {
    nickName = json['nickName'];
    nombre = json['nombre'];
    fechaDeNacimiento = json['fechaDeNacimiento'];
    email = json['email'];
    avatar = json['avatar'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nickName'] = nickName;
    _data['nombre'] = nombre;
    _data['fechaDeNacimiento'] = fechaDeNacimiento;
    _data['email'] = email;
    _data['avatar'] = avatar;
    return _data;
  }
}
