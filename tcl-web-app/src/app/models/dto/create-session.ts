export class CreateSessionDto {
  filmUuid: string;
  sessionDate: string;
  hallUuid: string;
  active: boolean;


  constructor(filmUuid:string, sessionDate:string,hallUuid:string, active:boolean) {
      this.filmUuid = filmUuid;
      this.sessionDate = sessionDate;
      this.hallUuid = hallUuid;
      this.active = active;
  }
}
