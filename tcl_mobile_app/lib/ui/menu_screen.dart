import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/ui/profile_screen.dart';
import 'package:tcl_mobile_app/ui/ticket_screen.dart';
import '../constants.dart';
import '../repository/preferences_utils.dart';
import 'home_screen.dart';

class MenuScreen extends StatefulWidget {
  const MenuScreen({Key? key,required this.initialScreen}) : super(key: key);

  final int initialScreen;
  @override
  _MenuScreenState createState() => _MenuScreenState();
}

class _MenuScreenState extends State<MenuScreen> {
  int _currentIndex = 0;
  String? avatar_sin_formato;
  String? avatar_url = "none";
  String? token = "none";

  List<Widget> pages = [
    const HomeScreen(),
    const TicketScreen(),
    const ProfileScreen()
  ];

  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    avatar_sin_formato = PreferenceUtils.getString("avatar");
    avatar_url = avatar_sin_formato!
        .replaceAll("http://localhost:8080", Constants.baseUrl);
    token = PreferenceUtils.getString("token");
    _currentIndex = widget.initialScreen;
    setState(() {
      
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Container(
            margin: MediaQuery.of(context).padding,
            child: pages[_currentIndex]),
        bottomNavigationBar: _buildBottomBar(avatar_url, token));
  }

  Widget _buildBottomBar(avatar_url, token) {
    return Container(
        decoration: const BoxDecoration(
            color: Color(0xFF1d1d1d),
            border: Border(
              top: BorderSide(
                color: Color(0xFF1d1d1d),
                width: 1.0,
              ),
            )),
        padding: const EdgeInsets.symmetric(horizontal: 60.0),
        height: 50,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            GestureDetector(
              child: Icon(Icons.home,
                  color: _currentIndex == 0
                      ? Colors.white
                      : const Color(0xff999999)),
              onTap: () {
                setState(() {
                  _currentIndex = 0;
                });
              },
            ),
            GestureDetector(
              child: Icon(Icons.ballot,
                  color: _currentIndex == 1
                      ? Colors.white
                      : const Color(0xff999999)),
              onTap: () {
                setState(() {
                  _currentIndex = 1;
                });
              },
            ),
            GestureDetector(
              onTap: () {
                setState(() {
                  _currentIndex = 2;
                });
              },
              child: Container(
                padding: const EdgeInsets.all(0),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(100),
                    border: Border.all(
                        color: _currentIndex == 2
                            ? Colors.white
                            : Colors.transparent,
                        width: 3)),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(100),
                  child: CachedNetworkImage(
                    placeholder: (context, url) => const Center(
                      child: CircularProgressIndicator(),
                    ),
                    imageUrl: avatar_url,
                    width: 30,
                    height: 30,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
            )
          ],
        ));
  }
}
