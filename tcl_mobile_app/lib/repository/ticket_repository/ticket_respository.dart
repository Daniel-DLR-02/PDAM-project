import '../../model/Ticket/CreateTicketResponse.dart';

abstract class TicketRepository {

  Future<CreateTicketResponse> createTickets(List<dynamic> seats, String filmUuid,String userUuid);

  //Future<TicketsListResponse> fetchTickets();
  
}
