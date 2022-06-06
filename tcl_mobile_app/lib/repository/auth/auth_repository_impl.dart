import 'dart:convert';
import 'package:http/http.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http_parser/http_parser.dart';
import 'package:tcl_mobile_app/model/auth/edit/edit_response.dart';
import 'package:tcl_mobile_app/model/auth/edit/edit_dto.dart';

import '../../model/auth/login/login_dto.dart';
import '../../model/auth/login/login_response.dart';
import '../../model/auth/register/register_dto.dart';
import '../../model/auth/register/register_response.dart';
import '../../constants.dart';
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
    if (response.statusCode == 200) {
      prefs.setString(
          'token', LoginResponse.fromJson(json.decode(response.body)).token);
      LoginResponse.fromJson(json.decode(response.body)).avatar == ''
          ? prefs.setString('avatar', Constants.defaultUserImage)
          : prefs.setString('avatar',
              LoginResponse.fromJson(json.decode(response.body)).avatar!);
      prefs.setString(
          'nick', LoginResponse.fromJson(json.decode(response.body)).nickname);
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
            contentType: MediaType('application', 'json')));

      if (filePath.isNotEmpty && filePath != null && filePath != "") {
        request..files.add(await http.MultipartFile.fromPath('file', filePath));
      }

      request.headers.addAll(headers);

      var response = await request.send();

      if (response.statusCode == 201) {
        LoginDto loginDto = LoginDto(
            nickname: registerDto.nickName, password: registerDto.password);
        await login(loginDto);
        return RegisterResponse.fromJson(
            jsonDecode(await response.stream.bytesToString()));
      } else {
        throw Exception('Fail to register');
      }
    } catch (error) {
      print('Error add project $error');
      rethrow;
    }
  }

  jsonToFormData(http.MultipartRequest request, Map<String, dynamic> data) {
    for (var key in data.keys) {
      request.fields[key] = data[key].toString();
    }
    return request;
  }

  @override
  Future<EditResponse> edit(EditDto editDto, String filePath) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      String? token = prefs.getString('token');

      var data = json.encode({
        "nombre": editDto.nombre,
        "role": "USER",
        "nickName": editDto.nickName,
        "email": editDto.email,
        "fechaNacimiento": editDto.fechaNacimiento,
      });

      var request =
          http.MultipartRequest('PUT', Uri.parse("${Constants.baseUrl}/user/"))
            ..headers['authorization'] = 'Bearer $token'
            ..headers['content-type'] = 'multipart/form-data'
            ..files.add(http.MultipartFile.fromString('user', data,
                contentType: MediaType('application', 'json')));

      if (filePath.isNotEmpty && filePath != null && filePath != "") {
        request..files.add(await http.MultipartFile.fromPath('file', filePath));
      }

      var response = await request.send();

      if (response.statusCode == 200) {
        return EditResponse.fromJson(
            jsonDecode(await response.stream.bytesToString()));
      } else {
        throw Exception('Fail to edit profile');
      }
    } catch (error) {
      print('Error add project $error');
      rethrow;
    }
  }
}
