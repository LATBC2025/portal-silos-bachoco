export class UpdatePasswordModel{
    password:string;
    passwordConfirm:string;

    constructor(password:string,passwordConfirm:string){
        this.password=password;
        this.passwordConfirm=passwordConfirm;
    }
}
