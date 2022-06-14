export class CreateUserDto {
  nombre: string;
  nickName: string;
  email: string;
  password: string;
  fechaNacimiento: string;

  constructor(nombre:string, nickName:string,email:string, password:string, fechaNacimiento:string) {
      this.nombre = nombre;
      this.nickName = nickName;
      this.email = email;
      this.password = password;
      this.fechaNacimiento=fechaNacimiento;
  }
}
