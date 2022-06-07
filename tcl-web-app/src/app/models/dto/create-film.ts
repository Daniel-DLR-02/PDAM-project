export class CreateFilmDto{
  title: string;
  description: string;
  duration: string;
  releaseDate: string;
  expirationDate: string;
  genre: string;

  constructor(title:string, description:string,duration:string, releaseDate:string,expirationDate:string, genre:string) {
    this.title = title;
    this.description = description;
    this.duration = duration;
    this.releaseDate = releaseDate;
    this.expirationDate = expirationDate;
    this.genre = genre;
}
}
