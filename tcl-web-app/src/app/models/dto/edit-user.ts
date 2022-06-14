export class EditUserDto {
  nombre: string;
  nickName: string;
  email: string;
  fechaNacimiento: string;
  role: string;

  constructor(nombre:string, nickName:string,email:string, fechaNacimiento:string, role :string) {
      this.nombre = nombre;
      this.nickName = nickName;
      this.email = email;
      this.fechaNacimiento=fechaNacimiento;
      this.role = role;
  }
}
