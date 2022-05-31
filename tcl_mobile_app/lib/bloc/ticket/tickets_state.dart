part of 'tickets_bloc.dart';

abstract class TicketsState extends Equatable {
  const TicketsState();

  @override
  List<Object> get props => [];
}

class TicketsInitial extends TicketsState {}

class TicketsFetched extends TicketsState {
  final List<Ticket> tickets;

  const TicketsFetched(this.tickets);

  @override
  List<Object> get props => [tickets];
}

class TicketFetchError extends TicketsState {
  final String message;
  const TicketFetchError(this.message);

  @override
  List<Object> get props => [message];
}

class TicketSuccessFetched extends TicketsState {
  final TicketListResponse tickets;

  const TicketSuccessFetched(this.tickets);

  @override
  List<Object> get props => [tickets];

  
}

class TicketErrorState extends TicketsState {
  final String message;

  const TicketErrorState(this.message);

  @override
  List<Object> get props => [message];
}
