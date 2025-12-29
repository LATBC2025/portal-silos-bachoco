export class UpdatePasswordExpiredModel{
    passwordActual:string;
    passwordConfirm:string;

    constructor(passwordActual:string,passwordConfirm:string){
        this.passwordActual=passwordActual;
        this.passwordConfirm=passwordConfirm;
    }
}
