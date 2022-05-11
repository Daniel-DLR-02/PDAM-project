import 'package:flutter/material.dart';
import 'package:tcl_mobile_app/ui/login_screen.dart';
import 'package:tcl_mobile_app/ui/menu_screen.dart';
import 'package:tcl_mobile_app/ui/register_screen.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TheCinemaLive',
      theme: ThemeData(scaffoldBackgroundColor: const Color(0xFF263238)),
      debugShowCheckedModeBanner: false,
      initialRoute: '/login',
      routes: {
        '/': (context) => const MenuScreen(),
        '/login': (context) => const LoginScreen(),
        '/register': (context) => const RegisterScreen(),
      },
    );
  }
}
