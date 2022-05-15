import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/bloc/session/session_bloc.dart';
import 'package:tcl_mobile_app/model/Sessions/single_session_response.dart';
import 'package:tcl_mobile_app/repository/preferences_utils.dart';
import 'package:tcl_mobile_app/repository/session_repository/session_repository.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';

import '../repository/session_repository/session_repository_impl.dart';
import 'widgets/home_app_bar.dart';

class BuyTicketScreen extends StatefulWidget {
  const BuyTicketScreen({ Key? key, required this.filmUuid }) : super(key: key);
  final String filmUuid;

  @override
  State<BuyTicketScreen> createState() => _BuyTicketScreenState();
}

class _BuyTicketScreenState extends State<BuyTicketScreen> {
  late SessionRepository sessionRepository;
  String? token = "none";
  String filmId = "ac106372-80c7-15d7-8180-c737802e0002";
  String sessionId = "ac106372-80c7-15d7-8180-c737802e0002";
  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    sessionRepository = SessionRepositoryImpl();
    token = PreferenceUtils.getString("token");
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) {
        return SessionsBloc(sessionRepository)..add(GetSessionDetails(sessionId));
      },
      child: Scaffold(
        body: _createSeeSession(context, sessionId),
        appBar: const HomeAppBar(),
      ),
    );
  }
}


Widget _createSeeSession(BuildContext context, String uuid) {
  return BlocBuilder<SessionsBloc, SessionsState>(
    builder: (context, state) {
      if (state is SessionsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is SessionErrorState) {
        return ErrorPage(
          message: state.message,
          retry: () {
            context.watch<SessionsBloc>().add(GetSessionDetails(uuid));
          },
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

  return Scaffold(
    backgroundColor: Color(0xFF263238),
    body: Column(
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
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: getSeatView(session.availableSeats),
        ),
      ],
    )
  );
}


Widget getSeatView(List<List<dynamic>> seats) {

  
  List<Widget> seatList = [];
  int rowSeats = seats[0].length;
  double height = (rowSeats * seats[0].length/1.3).toDouble();

  for (var row in seats) {
      for (var seat in row) {
          if(seat == "S"){
            seatList.add(
              Container(
                width: 10,
                height: 10,
                decoration: const BoxDecoration(
                  color: Color(0xFF37474f),
                  borderRadius: BorderRadius.all(Radius.circular(2))
                ),
              )
            );
          }
          else if(seat == "P") {
            seatList.add(
              Container(
                width: 10,
                height: 10,
                decoration: const BoxDecoration(
                  color: Colors.transparent,
                  borderRadius: BorderRadius.all(Radius.circular(2))
                ),
              )
            );
          }
          else if(seat == "O") {
            seatList.add(
              Container(
                width: 10,
                height: 10,
                decoration: const BoxDecoration(
                  color: Color(0xFF78909c),
                  borderRadius: BorderRadius.all(Radius.circular(2))
                ),
              )
            );
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
        Container(
          width: double.infinity,
          height: 10,
          color:Colors.white
        )
      ],
    ),
  );
  
}
