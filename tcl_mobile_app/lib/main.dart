import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
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
      theme: ThemeData(
        scaffoldBackgroundColor: const Color(0xFF1d1d1d),
      fontFamily: GoogleFonts.poppins().fontFamily),
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
