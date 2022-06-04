
import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:tcl_mobile_app/model/Ticket/TicketListResponse.dart';
import 'package:tcl_mobile_app/repository/ticket_repository/ticket_respository.dart';

part 'tickets_event.dart';
part 'tickets_state.dart';

class TicketsBloc extends Bloc<TicketsEvent, TicketsState> {
  final TicketRepository ticketRepository;

  TicketsBloc(this.ticketRepository) : super(TicketsInitial()) {
    on<FetchUserTicket>(_userTicketsFetched);
  }

  void _userTicketsFetched(FetchUserTicket event, Emitter<TicketsState> emit) async {
    try {
      final tickets = await ticketRepository.fetchTickets();
      emit(TicketsFetched(tickets));
      return;
    } on Exception catch (e) {
      emit(TicketFetchError(e.toString()));
    }
  }

}
