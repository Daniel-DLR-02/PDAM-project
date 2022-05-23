import '../../model/Sessions/session_response.dart';
import '../../model/Sessions/single_session_response.dart';

abstract class SessionRepository {

  Future<List<Session>> fetchFilmSessions(String filmUuid);

  Future<SessionResponse> fetchSessionDetails(String id);
  
}
