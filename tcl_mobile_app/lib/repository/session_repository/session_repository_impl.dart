import 'dart:convert';

import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/model/Sessions/single_session_response.dart';
import 'package:tcl_mobile_app/model/Sessions/session_response.dart';
import 'package:tcl_mobile_app/repository/session_repository/session_repository.dart';

import '../../constants.dart';
import 'package:http/http.dart' as http;
import 'dart:io';

class SessionRepositoryImpl extends SessionRepository {
  final Client _client = Client();

  @override
  Future<List<Session>> fetchFilmSessions(String filmUuid) async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');
    final response = await _client.get(
        Uri.parse('${Constants.baseUrl}/session/film/${filmUuid}'),
        headers: {
          HttpHeaders.contentTypeHeader: "application/json",
          HttpHeaders.authorizationHeader: "Bearer $token"
        });
    if (response.statusCode == 200) {
      return SessionList.fromJson(
              json.decode(const Utf8Decoder().convert(response.bodyBytes)))
          .content;
    } else {
      throw Exception('Fail to load sessions');
    }
    ;
  }

  @override
  Future<SessionResponse> fetchSessionDetails(String id) async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');
    final response = await _client
        .get(Uri.parse('${Constants.baseUrl}/session/${id}'), headers: {
      HttpHeaders.contentTypeHeader: "application/json; charset=utf-8",
      HttpHeaders.authorizationHeader: "Bearer $token"
    });
    if (response.statusCode == 200) {
      return SessionResponse.fromJson(
          json.decode(const Utf8Decoder().convert(response.bodyBytes)));
    } else {
      throw Exception('Fail to load session');
    }
  }
}
