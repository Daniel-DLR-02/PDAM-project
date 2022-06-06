class UserResponse {
  UserResponse({
    required this.uuid,
    required this.nick,
    required this.nombre,
    required this.fechaDeNacimiento,
    required this.email,
    required this.avatar,
    required this.role,
  });
  late final String uuid;
  late final String nick;
  late final String nombre;
  late final String fechaDeNacimiento;
  late final String email;
  late final String role;
  late final String? avatar;

  UserResponse.fromJson(Map<String, dynamic> json) {
    uuid = json['uuid'];
    nick = json['nick'];
    nombre = json['nombre'];
    fechaDeNacimiento = json['fechaDeNacimiento'];
    role = json['role'];
    email = json['email'];
    avatar = json['avatar'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['uuid'] = uuid;
    _data['nick'] = nick;
    _data['nombre'] = nombre;
    _data['fechaDeNacimiento'] = fechaDeNacimiento;
    _data['role'] = role;
    _data['email'] = email;
    _data['avatar'] = avatar;
    return _data;
  }
}
