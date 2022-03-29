import 'dart:convert';
import 'dart:io';
import 'dart:ui';
import 'package:http/http.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http_parser/http_parser.dart';

import '../../model/auth/login/login_dto.dart';
import '../../model/auth/login/login_response.dart';
import '../../model/auth/register/register_dto.dart';
import '../../model/auth/register/register_response.dart';
import '../constants.dart';
import 'auth_repository.dart';

class AuthRepositoryImpl extends AuthRepository {
  final Client _client = Client();

  @override
  Future<LoginResponse> login(LoginDto loginDto) async {
    final prefs = await SharedPreferences.getInstance();
    final response =
        await _client.post(Uri.parse("${Constants.baseUrl}/auth/login"),
            headers: {
              'Content-Type': 'application/json',
            },
            body: jsonEncode(loginDto.toJson()));
    if (response.statusCode == 201) {
      prefs.setString(
          'token', LoginResponse.fromJson(json.decode(response.body)).token);
      prefs.setString(
          'avatar', LoginResponse.fromJson(json.decode(response.body)).avatar);
      return LoginResponse.fromJson(json.decode(response.body));
    } else {
      throw Exception('Fail to login');
    }
  }

  @override
  Future<RegisterResponse> register(
      RegisterDto registerDto, String filePath) async {
    try {
      Map<String, String> headers = {"Content-Type": "multipart/form-data"};

      var data = json.encode({
        "nickName": registerDto.nickName,
        "nombre": registerDto.nombre,
        "email": registerDto.email,
        "password": registerDto.password,
        "fechaNacimiento": registerDto.fechaNacimiento,
      });

      var request = http.MultipartRequest(
          'POST', Uri.parse("${Constants.baseUrl}/auth/register"))
        ..files.add(http.MultipartFile.fromString('user', data,
            contentType: MediaType('application', 'json')))
        ..files.add(await http.MultipartFile.fromPath('file', filePath));

      request.headers.addAll(headers);

      var response = await request.send();

      if (response.statusCode == 201) {
        LoginDto loginDto =
            LoginDto(nickName: registerDto.nickName, password: registerDto.password);
        login(loginDto);
        return RegisterResponse.fromJson(
            jsonDecode(await response.stream.bytesToString()));
      } else {
        throw Exception('Fail to register');
      }
    } catch (error) {
      print('Error add project $error');
      throw (error);
    }
  }

  jsonToFormData(http.MultipartRequest request, Map<String, dynamic> data) {
    for (var key in data.keys) {
      request.fields[key] = data[key].toString();
    }
    return request;
  }
}