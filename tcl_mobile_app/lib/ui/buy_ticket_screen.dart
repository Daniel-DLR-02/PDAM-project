import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/bloc/session/session_bloc.dart';
import 'package:tcl_mobile_app/model/Films/film_response.dart';
import 'package:tcl_mobile_app/model/Sessions/single_session_response.dart';
import 'package:tcl_mobile_app/repository/preferences_utils.dart';
import 'package:tcl_mobile_app/repository/session_repository/session_repository.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';

import '../model/Sessions/session_response.dart';
import '../repository/session_repository/session_repository_impl.dart';
import 'widgets/home_app_bar.dart';

class BuyTicketScreen extends StatefulWidget {
  const BuyTicketScreen({Key? key, required this.filmUuid, this.sessionUuid})
      : super(key: key);
  final String filmUuid;
  final String? sessionUuid;
  @override
  State<BuyTicketScreen> createState() => _BuyTicketScreenState();
}

class _BuyTicketScreenState extends State<BuyTicketScreen> {
  late SessionRepository sessionRepository;
  String? token = "none";
  String filmId = "ac126ef2-80bc-101b-8180-bcb1c5490000";
  String? sessionId;

  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    sessionRepository = SessionRepositoryImpl();
    token = PreferenceUtils.getString("token");
    sessionId = widget.sessionUuid ?? "none";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: const HomeAppBar(),
      backgroundColor: const Color(0xFF1d1d1d),
      body: Container(
        decoration: const BoxDecoration(color: Color(0xFF263238)),
        child: Column(
          children: [
            Container(
              height: 65,
              color: const Color(0xFF1d1d1d),
              child: Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left: 25.0, top: 5.0),
                    child: GestureDetector(
                      onTap: () {
                        Navigator.pop(context);
                      },
                      child: const Icon(Icons.arrow_back, color: Colors.white),
                    ),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(top: 7.0, left: 20),
                    child: Text(
                      "Selección de asientos",
                      style: TextStyle(color: Colors.white, fontSize: 15.0),
                    ),
                  ),
                ],
              ),
            ),
            BlocProvider(
              create: (context) {
                return SessionsBloc(sessionRepository)
                  ..add(GetSessionDetails(sessionId!));
              },
              child: Container(
                child: _createSeeSession(context, sessionId!),
              ),
            ),
            BlocProvider(
              create: (context) {
                return SessionsBloc(sessionRepository)
                  ..add(FetchFilmSession(filmId));
              },
              child: Container(
                child: _createSeeFilmSessions(context, sessionId!),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

Widget _createSeeFilmSessions(BuildContext context, String filmUuid) {
  return BlocBuilder<SessionsBloc, SessionsState>(
    builder: (context, state) {
      if (state is SessionsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is SessionErrorState) {
        return ErrorPage(
          message: "state.message",
          retry: () {
            context.watch<SessionsBloc>().add(FetchFilmSession(filmUuid));
          },
        );
      } else if (state is SessionsFetched) {
        return _createSessionList(context, state.sessions, filmUuid);
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createSessionList(
    BuildContext context, List<Session> sessions, String filmUuid) {
  final contentWidth = MediaQuery.of(context).size.width - 30;
  final contentHeight = MediaQuery.of(context).size.height;
  PreferenceUtils.init();
  String? token = PreferenceUtils.getString("token");

  return SingleChildScrollView(
    child: Container(
      color: const Color(0xFF1d1d1d),
      child: Column(
        children: [
          const Padding(
            padding: EdgeInsets.only(top: 7.0, left: 20),
            child: Text(
              "Selección de sesión",
              style: TextStyle(color: Colors.white, fontSize: 17.0),
            ),
          ),
          const Divider(
            height: 20,
            thickness: 1,
            indent: 30,
            endIndent: 30,
            color: Colors.white,
          ),
          SizedBox(
            height: contentHeight / 3.07,
            width: contentWidth - 10,
            child: ListView.builder(
              itemBuilder: (BuildContext context, int index) {
                return _getSessionList(context, sessions[index], filmUuid);
              },
              scrollDirection: Axis.vertical,
              itemCount: sessions.length,
            ),
          ),
        ],
      ),
    ),
  );
}

Widget _getSessionList(context, Session session, String filmUuid) {
  return Padding(
    padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 5),
    child: ElevatedButton(
      style: ElevatedButton.styleFrom(
        primary: const Color(0xFF546e7a),
        side: const BorderSide(color: Color(0xFF78909c)),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
      ),
      onPressed: () => Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(
          builder: (context) => BuyTicketScreen(
              filmUuid: filmUuid, sessionUuid: session.sessionId),
        ),
        ModalRoute.withName('/'),
      ),
      child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 10.0),
          child: Text(
            DateTime.parse(session.sessionDate).toString().substring(0, 16),
            style: const TextStyle(color: Colors.white, fontSize: 16.0),
          )),
    ),
  );
}

Widget _createSeeSession(BuildContext context, String uuid) {
  return BlocBuilder<SessionsBloc, SessionsState>(
    builder: (context, state) {
      if (state is SessionsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is SessionErrorState) {
        return Container(
          height: 364.65,
          child: const Padding(
            padding: EdgeInsets.only(top: 180.0),
            child: Text(
              "Seleccione una sesión",
              style: TextStyle(color: Colors.white, fontSize: 20),
            ),
          ),
        );
      } else if (state is SessionSuccessFetched) {
        return _createPublicView(context, state.session);
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createPublicView(BuildContext context, SessionResponse session) {
  final contentWidth = MediaQuery.of(context).size.width - 30;
  final contentHeight = MediaQuery.of(context).size.height;
  PreferenceUtils.init();
  String? token = PreferenceUtils.getString("token");

  return Container(
      child: Column(
    children: [
      Padding(
        padding: const EdgeInsets.all(20.0),
        child: getSeatView(session.availableSeats),
      ),
    ],
  ));
}

Widget getSeatView(List<List<dynamic>> seats) {
  List<Widget> seatList = [];
  int rowSeats = seats[0].length;
  int columnSeats = seats.length;
  double height = (columnSeats * 27).toDouble();

  for (var row in seats) {
    for (var seat in row) {
      if (seat == "S") {
        seatList.add(Container(
          width: 10,
          height: 10,
          decoration: const BoxDecoration(
              color: Color(0xFF37474f),
              borderRadius: BorderRadius.all(Radius.circular(2))),
        ));
      } else if (seat == "P") {
        seatList.add(Container(
          width: 10,
          height: 10,
          decoration: const BoxDecoration(
              color: Colors.transparent,
              borderRadius: BorderRadius.all(Radius.circular(2))),
        ));
      } else if (seat == "O") {
        seatList.add(Container(
          width: 10,
          height: 10,
          decoration: const BoxDecoration(
              color: Color(0xFF78909c),
              borderRadius: BorderRadius.all(Radius.circular(2))),
        ));
      }
    }
  }

  return Container(
    color: Colors.transparent,
    height: height,
    child: Column(
      children: [
        Expanded(
          child: GridView.builder(
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              // number of items per row
              crossAxisCount: rowSeats,
              // vertical spacing between the items
              mainAxisSpacing: 10,
              // horizontal spacing between the items
              crossAxisSpacing: 3,
            ),
            // number of items in your list
            itemCount: seatList.length,

            itemBuilder: (BuildContext context, int index) {
              return seatList[index];
            },
          ),
        ),
        Container(width: double.infinity, height: 10, color: Colors.white),
        Row(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 8.0),
              child: Row(
                children: [
                  Container(
                    width: 15,
                    height: 15,
                    decoration: const BoxDecoration(
                        color: Color(0xFF37474f),
                        borderRadius: BorderRadius.all(Radius.circular(2))),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(left: 9.0),
                    child: Text("Disponible",
                        style: TextStyle(color: Colors.white, fontSize: 13)),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 8),
              child: Row(
                children: [
                  Container(
                    width: 15,
                    height: 15,
                    decoration: const BoxDecoration(
                        color: Color(0xFFeceff1),
                        borderRadius: BorderRadius.all(Radius.circular(2))),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(left: 9.0),
                    child: Text("Seleccionado",
                        style: TextStyle(color: Colors.white, fontSize: 13)),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 8),
              child: Row(
                children: [
                  Container(
                    width: 15,
                    height: 15,
                    decoration: const BoxDecoration(
                        color: Color(0xFF78909c),
                        borderRadius: BorderRadius.all(Radius.circular(2))),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(left: 9.0),
                    child: Text("Reservado",
                        style: TextStyle(color: Colors.white, fontSize: 13)),
                  ),
                ],
              ),
            ),
          ],
        )
      ],
    ),
  );
}
