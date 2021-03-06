// Generated by https://quicktype.io

export interface FilmsResponse {
  content:          Film[];
  pageable:         Pageable;
  totalElements:    number;
  totalPages:       number;
  last:             boolean;
  size:             number;
  number:           number;
  sort:             Sort;
  numberOfElements: number;
  first:            boolean;
  empty:            boolean;
}

export interface Film {
  uuid:        string;
  title:       string;
  poster:      string;
  description: string;
  duration:    string;
  expirationDate: string;
  releaseDate: string;
  genre:       string;
}

export interface Pageable {
  sort:       Sort;
  offset:     number;
  pageNumber: number;
  pageSize:   number;
  paged:      boolean;
  unpaged:    boolean;
}

export interface Sort {
  empty:    boolean;
  sorted:   boolean;
  unsorted: boolean;
}
