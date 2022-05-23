class RegisterResponse {
  RegisterResponse({
    required this.nick,
    required this.nombre,
    required this.fechaDeNacimiento,
    required this.email,
    required this.avatar,
  });
  late final String nick;
  late final String nombre;
  late final String fechaDeNacimiento;
  late final String email;
  late final String avatar;

  RegisterResponse.fromJson(Map<String, dynamic> json) {
    nick = json['nick'];
    nombre = json['nombre'];
    fechaDeNacimiento = json['fechaDeNacimiento'];
    email = json['email'];
    avatar = json['avatar'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nick'] = nick;
    _data['nombre'] = nombre;
    _data['fechaDeNacimiento'] = fechaDeNacimiento;
    _data['email'] = email;
    _data['avatar'] = avatar;
    return _data;
  }
}
