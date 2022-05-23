class SessionList {
  SessionList({
    required this.content,
    required this.pageable,
    required this.totalPages,
    required this.totalElements,
    required this.last,
    required this.size,
    required this.number,
    required this.sort,
    required this.numberOfElements,
    required this.first,
    required this.empty,
  });
  late final List<Session> content;
  late final Pageable pageable;
  late final int totalPages;
  late final int totalElements;
  late final bool last;
  late final int size;
  late final int number;
  late final Sort sort;
  late final int numberOfElements;
  late final bool first;
  late final bool empty;
  
  SessionList.fromJson(Map<String, dynamic> json){
    content = List.from(json['content']).map((e)=>Session.fromJson(e)).toList();
    pageable = Pageable.fromJson(json['pageable']);
    totalPages = json['totalPages'];
    totalElements = json['totalElements'];
    last = json['last'];
    size = json['size'];
    number = json['number'];
    sort = Sort.fromJson(json['sort']);
    numberOfElements = json['numberOfElements'];
    first = json['first'];
    empty = json['empty'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['content'] = content.map((e)=>e.toJson()).toList();
    _data['pageable'] = pageable.toJson();
    _data['totalPages'] = totalPages;
    _data['totalElements'] = totalElements;
    _data['last'] = last;
    _data['size'] = size;
    _data['number'] = number;
    _data['sort'] = sort.toJson();
    _data['numberOfElements'] = numberOfElements;
    _data['first'] = first;
    _data['empty'] = empty;
    return _data;
  }
}

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
  late final List<String> availableSeats;
  
  Session.fromJson(Map<String, dynamic> json){
    sessionId = json['sessionId'];
    filmTitle = json['filmTitle'];
    sessionDate = json['sessionDate'];
    hallName = json['hallName'];
    active = json['active'];
    availableSeats = List.castFrom<dynamic, List<String>>(json['availableSeats']).cast<String>();
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