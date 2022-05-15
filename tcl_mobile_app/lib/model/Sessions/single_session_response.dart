class Session {
  Session({
    required this.sessionId,
    required this.filmTitle,
    required this.sessionDate,
    required this.hallName,
    required this.active,
    required this.availableSeats,
  });
  late final String sessionId;
  late final String filmTitle;
  late final String sessionDate;
  late final String hallName;
  late final bool active;
  late final List<List<String>> availableSeats;
  
  Session.fromJson(Map<String, dynamic> json){
    sessionId = json['sessionId'];
    filmTitle = json['filmTitle'];
    sessionDate = json['sessionDate'];
    hallName = json['hallName'];
    active = json['active'];
    availableSeats = List.castFrom<dynamic, List<String>>(json['availableSeats']);
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['sessionId'] = sessionId;
    _data['filmTitle'] = filmTitle;
    _data['sessionDate'] = sessionDate;
    _data['hallName'] = hallName;
    _data['active'] = active;
    _data['availableSeats'] = availableSeats;
    return _data;
  }
}