import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tcl_mobile_app/constants.dart';
import 'package:tcl_mobile_app/model/Ticket/CreateTicket.dart';
import 'package:tcl_mobile_app/model/Ticket/TicketListResponse.dart';
import 'ticket_respository.dart';

class TicketRepositoryImpl extends TicketRepository {
  final Client _client = Client();

  @override
  Future<void> createTickets(
      List<dynamic> seats, String sessionUuid, String userUuid) async {
    final prefs = await SharedPreferences.getInstance();
    for (seats.length; seats.isNotEmpty; seats.removeAt(0)) {
      final response = await _client.post(
          Uri.parse("${Constants.baseUrl}/ticket/"),
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ${prefs.getString('token')}'
          },
          body: jsonEncode(CreateTicket(
                  sessionUuid: sessionUuid,
                  row: seats[0].split(',')[0],
                  column: seats[0].split(',')[1])
              .toJson()));
      if (response.statusCode != 201) {
        throw Exception('Fail to create ticket');
      }
    }
  }

  @override
  Future<List<Ticket>> fetchTickets() async {
    final prefs = await SharedPreferences.getInstance();

    String? token = prefs.getString('token');
    final response = await _client
        .get(Uri.parse('${Constants.baseUrl}/ticket/user'), headers: {
      HttpHeaders.contentTypeHeader: "application/json",
      HttpHeaders.authorizationHeader: "Bearer $token"
    });
    if (response.statusCode == 200) {
      return TicketListResponse.fromJson(
              json.decode(const Utf8Decoder().convert(response.bodyBytes)))
          .content;
    } else {
      throw Exception('Fail to load tickets');
    }
  }
}
