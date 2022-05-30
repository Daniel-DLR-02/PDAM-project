import '../../model/Ticket/CreateTicketResponse.dart';

abstract class TicketRepository {

  Future<void> createTickets(List<dynamic> seats, String sessionUuid,String userUuid);

  //Future<TicketsListResponse> fetchTickets();
  
}
