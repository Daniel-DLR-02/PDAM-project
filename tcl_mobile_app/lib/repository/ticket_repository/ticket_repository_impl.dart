import 'package:tcl_mobile_app/model/Ticket/CreateTicketResponse.dart';
import 'ticket_respository.dart';

class TicketRepositoryImpl extends TicketRepository {
  @override
  Future<CreateTicketResponse> createTickets(List<dynamic> seats, String filmUuid,String userUuid) {
    // TODO: implement createTicket
    throw UnimplementedError();
  }

  /*@override
  Future fetchTickets() {
    // TODO: implement fetchTickets
    throw UnimplementedError();
  }*/

}
