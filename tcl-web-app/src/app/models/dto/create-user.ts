export class CreateUserDto {
  nombre: string;
  nickName: string;
  email: string;
  password: string;
  fechaDeNacimiento: string;

  constructor(nombre:string, nickName:string,email:string, password:string, fechaDeNacimiento:string) {
      this.nombre = nombre;
      this.nickName = nickName;
      this.email = email;
      this.password = password;
      this.fechaDeNacimiento=fechaDeNacimiento;
  }
}
