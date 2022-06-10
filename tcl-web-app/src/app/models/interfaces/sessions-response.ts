export interface SessionsResponse {
  content: Session[];
  pageable: Pageable;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export interface Session {
  sessionId: string;
  filmUuid: string;
  filmTitle: string;
  sessionDate: string;
  hallUuid: string;
  hallName: string;
  active: boolean;
  availableSeats: Array<AvailableSeat[]>;
}

export enum AvailableSeat {
  O = 'O',
  P = 'P',
  S = 'S',
}

export interface Pageable {
  sort: Sort;
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}
