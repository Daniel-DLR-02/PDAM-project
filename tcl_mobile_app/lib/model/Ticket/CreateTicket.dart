class CreateTicket {
  CreateTicket({
    required this.sessionUuid,
    required this.row,
    required this.column,
  });
  late final String sessionUuid;
  late final String row;
  late final String column;
  
  CreateTicket.fromJson(Map<String, dynamic> json){
    sessionUuid = json['sessionUuid'];
    row = json['row'];
    column = json['column'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['sessionUuid'] = sessionUuid;
    _data['row'] = row;
    _data['column'] = column;
    return _data;
  }
}