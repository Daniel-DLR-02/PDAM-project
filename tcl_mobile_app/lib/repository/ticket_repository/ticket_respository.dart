import 'package:tcl_mobile_app/model/Ticket/TicketListResponse.dart';


abstract class TicketRepository {

  Future<void> createTickets(List<dynamic> seats, String sessionUuid,String userUuid);

  Future<List<Ticket>> fetchTickets();
  
}
