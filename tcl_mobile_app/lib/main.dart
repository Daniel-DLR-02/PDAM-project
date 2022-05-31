import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tcl_mobile_app/ui/buy_ticket_screen.dart';
import 'package:tcl_mobile_app/ui/film_details.dart';
import 'package:tcl_mobile_app/ui/login_screen.dart';
import 'package:tcl_mobile_app/ui/menu_screen.dart';
import 'package:tcl_mobile_app/ui/register_screen.dart';
import 'package:flutter/services.dart';
import 'package:skeletons/skeletons.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
      DeviceOrientation.portraitDown,
    ]);
    return MaterialApp(
      title: 'TheCinemaLive',
      theme: ThemeData(
          scaffoldBackgroundColor: const Color(0xFF1d1d1d),
          primaryColor: const Color(0xFF1d1d1d),
          colorScheme: ThemeData().colorScheme.copyWith(
                primary: Colors.black,
              ),
          fontFamily: GoogleFonts.poppins().fontFamily),
      debugShowCheckedModeBanner: false,
      initialRoute: '/login',
      routes: {
        '/': (context) => const MenuScreen(initialScreen: 0),
        '/login': (context) => const LoginScreen(),
        '/register': (context) => const RegisterScreen(),
      },
    );
  }
}
