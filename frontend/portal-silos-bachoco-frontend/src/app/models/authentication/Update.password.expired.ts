export class UpdatePasswordExpiredRequest{
    username:string;
    passwordActual:string;
    nuevoPassword:string;

    constructor(username:string,passwordActual:string,nuevoPassword:string){
        this.username=username;
        this.passwordActual=passwordActual;
        this.nuevoPassword=nuevoPassword;
    }
}