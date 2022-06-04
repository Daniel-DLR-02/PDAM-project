part of 'tickets_bloc.dart';

abstract class TicketsEvent extends Equatable {
  const TicketsEvent();

  @override
  List<Object> get props => [];
}

class FetchUserTicket extends TicketsEvent {

  const FetchUserTicket();

  @override
  List<Object> get props => [];
}


