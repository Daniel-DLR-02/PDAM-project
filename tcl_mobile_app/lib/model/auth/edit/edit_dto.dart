class EditDto {
  EditDto({
    required this.nombre,
    required this.nickName,
    required this.email,
    required this.fechaNacimiento,
  });
  late final String nombre;
  late final String nickName;
  late final String email;
  late final String fechaNacimiento;
  
  EditDto.fromJson(Map<String, dynamic> json){
    nombre = json['nombre'];
    nickName = json['nickName'];
    email = json['email'];
    fechaNacimiento = json['fechaNacimiento'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['nombre'] = nombre;
    _data['nickName'] = nickName;
    _data['email'] = email;
    _data['fechaNacimiento'] = fechaNacimiento;
    return _data;
  }
}