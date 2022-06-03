import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart';

import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/repository/user_repository/user_repository.dart';

import '../../constants.dart';
import '../../model/User/user_response.dart';

class UserRepositoryImpl extends UserRepository {
  final Client _client = Client();

  @override
  Future<UserResponse> me() async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');

    final response =
        await _client.get(Uri.parse("${Constants.baseUrl}/me"), headers: {
      HttpHeaders.contentTypeHeader: "application/json",
      HttpHeaders.authorizationHeader: "Bearer $token"
    });
    if (response.statusCode == 200) {
      return UserResponse.fromJson(json.decode(response.body));
    } else {
      throw Exception('Fail to load profile');
    }
  }
}
