class RegisterDto {
  RegisterDto({
    required this.nombre,
    required this.nickName,
    required this.email,
    required this.password,
    required this.fechaNacimiento,
  });
  late final String nombre;
  late final String nickName;
  late final String email;
  late final String password;
  late final String fechaNacimiento;

  RegisterDto.fromJson(Map<String, dynamic> json) {
    nombre = json['nombre'];
    nickName = json['nickName'];
    email = json['email'];
    password = json['password'];
    fechaNacimiento = json['fechaNacimiento'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nombre'] = nombre;
    _data['nickName'] = nickName;
    _data['email'] = email;
    _data['password'] = password;
    _data['fechaNacimiento'] = fechaNacimiento;
    return _data;
  }
}