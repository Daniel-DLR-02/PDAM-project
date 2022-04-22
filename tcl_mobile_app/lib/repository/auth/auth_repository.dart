import 'dart:io';
import 'dart:ui';

import 'package:http/http.dart';

import '../../model/auth/login/login_dto.dart';
import '../../model/auth/login/login_response.dart';
import '../../model/auth/register/register_dto.dart';
import '../../model/auth/register/register_response.dart';

abstract class AuthRepository {
  Future<LoginResponse> login(LoginDto loginDto);
  Future<RegisterResponse> register(RegisterDto registerDto, String filePath);
}