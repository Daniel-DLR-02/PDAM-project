class FilmResponse {
  FilmResponse({
    required this.uuid,
    required this.title,
    required this.poster,
    required this.description,
    required this.duration,
    required this.genre,
  });
  late final String uuid;
  late final String title;
  late final String poster;
  late final String description;
  late final String duration;
  late final String genre;

  FilmResponse.fromJson(Map<String, dynamic> json) {
    uuid = json['uuid'];
    title = json['title'];
    poster = json['poster'];
    description = json['description'];
    duration = json['duration'];
    genre = json['genre'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['uuid'] = uuid;
    _data['title'] = title;
    _data['poster'] = poster;
    _data['description'] = description;
    _data['duration'] = duration;
    _data['genre'] = genre;
    return _data;
  }
}
