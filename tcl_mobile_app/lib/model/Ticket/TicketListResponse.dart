class TicketListResponse {
  TicketListResponse({
    required this.content,
    required this.pageable,
    required this.last,
    required this.totalPages,
    required this.totalElements,
    required this.size,
    required this.number,
    required this.sort,
    required this.first,
    required this.numberOfElements,
    required this.empty,
  });
  late final List<Ticket> content;
  late final Pageable pageable;
  late final bool last;
  late final int totalPages;
  late final int totalElements;
  late final int size;
  late final int number;
  late final Sort sort;
  late final bool first;
  late final int numberOfElements;
  late final bool empty;
  
  TicketListResponse.fromJson(Map<String, dynamic> json){
    content = List.from(json['content']).map((e)=>Ticket.fromJson(e)).toList();
    pageable = Pageable.fromJson(json['pageable']);
    last = json['last'];
    totalPages = json['totalPages'];
    totalElements = json['totalElements'];
    size = json['size'];
    number = json['number'];
    sort = Sort.fromJson(json['sort']);
    first = json['first'];
    numberOfElements = json['numberOfElements'];
    empty = json['empty'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['content'] = content.map((e)=>e.toJson()).toList();
    _data['pageable'] = pageable.toJson();
    _data['last'] = last;
    _data['totalPages'] = totalPages;
    _data['totalElements'] = totalElements;
    _data['size'] = size;
    _data['number'] = number;
    _data['sort'] = sort.toJson();
    _data['first'] = first;
    _data['numberOfElements'] = numberOfElements;
    _data['empty'] = empty;
    return _data;
  }
}

class Ticket {
  Ticket({
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
  
  Ticket.fromJson(Map<String, dynamic> json){
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

class Pageable {
  Pageable({
    required this.sort,
    required this.offset,
    required this.pageNumber,
    required this.pageSize,
    required this.paged,
    required this.unpaged,
  });
  late final Sort sort;
  late final int offset;
  late final int pageNumber;
  late final int pageSize;
  late final bool paged;
  late final bool unpaged;
  
  Pageable.fromJson(Map<String, dynamic> json){
    sort = Sort.fromJson(json['sort']);
    offset = json['offset'];
    pageNumber = json['pageNumber'];
    pageSize = json['pageSize'];
    paged = json['paged'];
    unpaged = json['unpaged'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['sort'] = sort.toJson();
    _data['offset'] = offset;
    _data['pageNumber'] = pageNumber;
    _data['pageSize'] = pageSize;
    _data['paged'] = paged;
    _data['unpaged'] = unpaged;
    return _data;
  }
}

class Sort {
  Sort({
    required this.empty,
    required this.sorted,
    required this.unsorted,
  });
  late final bool empty;
  late final bool sorted;
  late final bool unsorted;
  
  Sort.fromJson(Map<String, dynamic> json){
    empty = json['empty'];
    sorted = json['sorted'];
    unsorted = json['unsorted'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['empty'] = empty;
    _data['sorted'] = sorted;
    _data['unsorted'] = unsorted;
    return _data;
  }
}