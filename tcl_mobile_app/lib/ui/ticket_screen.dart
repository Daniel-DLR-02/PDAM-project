import 'package:flutter/material.dart';
import 'package:coupon_uikit/coupon_uikit.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/repository/ticket_repository/ticket_repository_impl.dart';
import 'package:tcl_mobile_app/repository/ticket_repository/ticket_respository.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';
import 'package:qr_flutter/qr_flutter.dart';

import '../bloc/ticket/tickets_bloc.dart';
import '../model/Ticket/TicketListResponse.dart';
import '../repository/preferences_utils.dart';

class TicketScreen extends StatefulWidget {
  const TicketScreen({Key? key}) : super(key: key);

  @override
  State<TicketScreen> createState() => _TicketScreenState();
}

class _TicketScreenState extends State<TicketScreen> {
  late TicketRepository ticketRepository;
  String? avatar_url;
  String? token = "none";

  @override
  void initState() {
    PreferenceUtils.init();
    ticketRepository = TicketRepositoryImpl();
    // token = PreferenceUtils.getString("token");
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) {
        return TicketsBloc(ticketRepository)..add(const FetchUserTicket());
      },
      child: Scaffold(
        body: _createSeeTickets(context),
      ),
    );
  }
}

Widget _createSeeTickets(BuildContext context) {
  return BlocBuilder<TicketsBloc, TicketsState>(
    builder: (context, state) {
      if (state is TicketsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is TicketFetchError) {
        return ErrorPage(
          message: state.message,
          retry: () {
            context.watch<TicketsBloc>().add(const FetchUserTicket());
          },
        );
      } else if (state is TicketsFetched) {
        return _createPublicView(context, state.tickets.reversed.toList());
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createPublicView(BuildContext context, List<Ticket> tickets) {
  final contentWidth = MediaQuery.of(context).size.width;
  final contentHeight = MediaQuery.of(context).size.height;
  PreferenceUtils.init();
  String? avatarUrl = PreferenceUtils.getString("avatar");
  String? token = PreferenceUtils.getString("token");
  String? nick = PreferenceUtils.getString("nick");
  return RefreshIndicator(
    onRefresh: () async {
      await Future.delayed(const Duration(seconds: 2),
          () => context.read<TicketsBloc>().add(const FetchUserTicket()));
    },
    triggerMode: RefreshIndicatorTriggerMode.onEdge,
    edgeOffset: 10,
    displacement: 50,
    strokeWidth: 3,
    color: const Color(0xFF263238),
    backgroundColor: const Color(0xFF1d1d1d),
    child: SingleChildScrollView(
      child: Column(
        children: <Widget>[
          Container(
            height: 65,
            color: const Color(0xFF1d1d1d),
            child: Row(
              children: const [
                Padding(
                  padding: EdgeInsets.only(top: 7.0, left: 20),
                  child: Text(
                    "Tickets",
                    style: TextStyle(color: Colors.white, fontSize: 15.0),
                  ),
                ),
              ],
            ),
          ),
          Container(
            color: const Color(0xFF263238),
            height: contentHeight,
            child: Padding(
              padding: const EdgeInsets.all(5.0),
              child: ListView.builder(
                itemCount: tickets.length,
                padding: const EdgeInsets.only(bottom: 150.0),
                itemBuilder: (BuildContext context, int index) {
                  return _createPublicViewItem(context, tickets[index]);
                },
                scrollDirection: Axis.vertical,
              ),
            ),
          ),
        ],
      ),
    ),
  );
}

Widget _createPublicViewItem(
  BuildContext context,
  Ticket ticket,
) {
  return InkWell(
    onTap: () => {
      showDialog<void>(
        context: context,
        barrierDismissible: false, // user must tap button!
        builder: (BuildContext context) {
          return AlertDialog(
            title: const Text('Código QR de la entrada'),
            content: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20.0),
              child: QrImage(
                data: ticket.uuid,
                version: QrVersions.auto,
                size: 200.0,
              ),
            ),
            actions: <Widget>[
              TextButton(
                child: const Text('Cerrar'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      )
    },
    child: Padding(
      padding: const EdgeInsets.only(top: 10.0),
      child: CouponCard(
        height: 200,
        backgroundColor: Colors.white,
        clockwise: true,
        curvePosition: 135,
        curveRadius: 30,
        curveAxis: Axis.vertical,
        borderRadius: 10,
        firstChild: Container(
          decoration: const BoxDecoration(
            color: Colors.black,
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        ticket.session.filmTitle,
                        textAlign: TextAlign.center,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const Divider(color: Colors.white54, height: 0),
              Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  children: [
                    Text(
                      "Fecha: " + ticket.session.sessionDate.split("T")[0],
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 11,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    Text(
                      "Hora: " +
                          ticket.session.sessionDate
                              .split("T")[1]
                              .split(":")[0] +
                          ":" +
                          ticket.session.sessionDate
                              .split("T")[1]
                              .split(":")[1],
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        secondChild: Container(
          width: double.maxFinite,
          padding: const EdgeInsets.all(18),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Fila: ' + (ticket.row+1).toString(),
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                  color: Colors.black54,
                ),
              ),
              Text(
                "Asiento: " + (ticket.column+1).toString(),
                textAlign: TextAlign.center,
                style: const TextStyle(
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                  color: Colors.black54,
                ),
              ),
              const SizedBox(height: 4),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20.0),
                child: QrImage(
                  data: ticket.uuid,
                  version: QrVersions.auto,
                  size: 100.0,
                ),
              ),
            ],
          ),
        ),
      ),
    ),
  );
}
