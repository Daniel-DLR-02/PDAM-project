class SessionResponse {
  SessionResponse({
    required this.sessionId,
    required this.filmUuid,
    required this.filmTitle,
    required this.sessionDate,
    required this.hallUuid,
    required this.hallName,
    required this.active,
    required this.availableSeats,
  });
  late final String sessionId;
  late final String filmUuid;
  late final String filmTitle;
  late final String sessionDate;
  late final String hallUuid;
  late final String hallName;
  late final bool active;
  late final List<List<dynamic>> availableSeats;
  
  SessionResponse.fromJson(Map<String, dynamic> json){
    sessionId = json['sessionId'];
    filmUuid = json['filmUuid'];
    filmTitle = json['filmTitle'];
    sessionDate = json['sessionDate'];
    hallUuid = json['hallUuid'];
    hallName = json['hallName'];
    active = json['active'];
    availableSeats = List.castFrom<dynamic, List<dynamic>>(json['availableSeats']);
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['sessionId'] = sessionId;
    _data['filmUuid'] = filmUuid;
    _data['filmTitle'] = filmTitle;
    _data['sessionDate'] = sessionDate;
    _data['hallUuid'] = hallUuid;
    _data['hallName'] = hallName;
    _data['active'] = active;
    _data['availableSeats'] = availableSeats;
    return _data;
  }

}