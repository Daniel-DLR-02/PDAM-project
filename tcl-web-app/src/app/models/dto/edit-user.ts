export class EditUserDto {
  nombre: string;
  nickName: string;
  email: string;
  fechaDeNacimiento: string;

  constructor(nombre:string, nickName:string,email:string, fechaDeNacimiento:string) {
      this.nombre = nombre;
      this.nickName = nickName;
      this.email = email;
      this.fechaDeNacimiento=fechaDeNacimiento;
  }
}
