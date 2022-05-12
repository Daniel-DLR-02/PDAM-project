
import 'dart:convert';

import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/model/Films/film_response.dart';

import '../../constants.dart';
import 'package:http/http.dart' as http;
import 'dart:io';

import '../../model/Films/single_film_response.dart';
import 'films_repository.dart';

class FilmRepositoryImpl extends FilmRepository {
  final Client _client = Client();

  @override
  Future<List<Film>> fetchFilms() async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');
    final response = await _client
        .get(Uri.parse('${Constants.baseUrl}/films/active'), headers: {
      HttpHeaders.contentTypeHeader: "application/json",
      HttpHeaders.authorizationHeader: "Bearer $token"
    });
    if (response.statusCode == 200) {
      return FilmsReponse.fromJson(json.decode(const Utf8Decoder().convert(response.bodyBytes))).content;
    } else {
      throw Exception('Fail to load posts');
    }
  }

  @override
  Future<FilmResponse> fetchFilmDetails(String uuid) async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');
    final response = await _client
        .get(Uri.parse('${Constants.baseUrl}/films/${uuid}'), headers: {
      HttpHeaders.contentTypeHeader: "application/json; charset=utf-8",
      HttpHeaders.authorizationHeader: "Bearer $token"
    });
    if (response.statusCode == 200) {
      return FilmResponse.fromJson(json.decode(const Utf8Decoder().convert(response.bodyBytes)));
    } else {
      throw Exception('Fail to load posts');
    }
  }

 
}