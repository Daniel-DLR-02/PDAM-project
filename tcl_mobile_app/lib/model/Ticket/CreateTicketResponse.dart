class CreateTicketResponse {
  CreateTicketResponse({
    required this.uuid,
    required this.user,
    required this.session,
    required this.row,
    required this.column,
  });
  late final String uuid;
  late final User user;
  late final Session session;
  late final int row;
  late final int column;
  
  CreateTicketResponse.fromJson(Map<String, dynamic> json){
    uuid = json['uuid'];
    user = User.fromJson(json['user']);
    session = Session.fromJson(json['session']);
    row = json['row'];
    column = json['column'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['uuid'] = uuid;
    _data['user'] = user.toJson();
    _data['session'] = session.toJson();
    _data['row'] = row;
    _data['column'] = column;
    return _data;
  }
}

class User {
  User({
    required this.uuid,
    required this.nick,
    required this.nombre,
    required this.fechaDeNacimiento,
    required this.email,
    required this.avatar,
  });
  late final String uuid;
  late final String nick;
  late final String nombre;
  late final String fechaDeNacimiento;
  late final String email;
  late final String avatar;
  
  User.fromJson(Map<String, dynamic> json){
    uuid = json['uuid'];
    nick = json['nick'];
    nombre = json['nombre'];
    fechaDeNacimiento = json['fechaDeNacimiento'];
    email = json['email'];
    avatar = json['avatar'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['uuid'] = uuid;
    _data['nick'] = nick;
    _data['nombre'] = nombre;
    _data['fechaDeNacimiento'] = fechaDeNacimiento;
    _data['email'] = email;
    _data['avatar'] = avatar;
    return _data;
  }
}

class Session {
  Session({
    required this.sessionId,
    required this.filmTitle,
    required this.sessionDate,
    required this.hallName,
  });
  late final String sessionId;
  late final String filmTitle;
  late final String sessionDate;
  late final String hallName;
  
  Session.fromJson(Map<String, dynamic> json){
    sessionId = json['sessionId'];
    filmTitle = json['filmTitle'];
    sessionDate = json['sessionDate'];
    hallName = json['hallName'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['sessionId'] = sessionId;
    _data['filmTitle'] = filmTitle;
    _data['sessionDate'] = sessionDate;
    _data['hallName'] = hallName;
    return _data;
  }
}