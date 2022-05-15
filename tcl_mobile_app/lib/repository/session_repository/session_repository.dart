abstract class FilmRepository {

  Future<List<Session>> fetchFilmSessions();

  Future<SessionResponse> fetchSession(String id);
  
}
